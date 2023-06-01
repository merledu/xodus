package xodus.core

import chisel3._
import xodus.configs.Configs,
       xodus.core.fetch_stage._,
       xodus.core.decode_stage._,
       /*xodus.core.execute_stage._,*/
       xodus.core.pipeline_regs._,
       xodus.memory.MemoryIO,
       xodus.debug_io.DebugCore


class CoreIO extends Bundle with Configs {
  val iMem: MemoryIO = Flipped(new MemoryIO)
  //val dMem: MemoryIO = Flipped(new MemoryIO)


  val debug: Option[DebugCore] = if (Debug) Some(new DebugCore) else None
}


class Core extends Module with Configs {
  val io: CoreIO = IO(new CoreIO)

  // Modules
  val pc      : PCIO       = Module(new PC).io
  val iMemJunc: IMemJuncIO = Module(new IMemJunc).io

  val regFD = Module(new RegFD).io

  val decoder: DecoderIO = Module(new Decoder).io
  val regFile: RegFileIO = Module(new RegFile).io

  val regDE = Module(new RegDE).io

  //val cu : ControlUnitIO = Module(new ControlUnit).io
  //val alu: ALU_IO        = Module(new ALU).io

  //val regEM: RegEM_IO = Module(new RegEM).io

  //val regMW: RegMW_IO = Module(new RegMW).io


  /***************
   * Fetch Stage *
   ***************/

  iMemJunc.addr := pc.addr
  regFD.in.pc   := pc.pc
  regFD.in.inst := iMemJunc.inst
  io.iMem       <> iMemJunc.iMemReqResp


  /****************
   * Decode Stage *
   ****************/

  decoder.inst         := regFD.out.inst
  regFile.write.bits   := 0.S
  regFile.write.valid  := 0.B
  regDE.in.pc          := regFD.out.pc
  regDE.in.opcode      := decoder.opcode
  regDE.in.funct3      := decoder.funct3
  regDE.in.funct7_imm7 := decoder.funct7_imm7
  regDE.in.data(2)     := decoder.imm
  Seq(regFile.rAddr, regDE.in.rAddr).map(
    x => x <> decoder.rAddr
  )
  for (i <- 0 to 1) {
    regDE.in.data(i) := regFile.read(i)
  }


  /*****************
   * Execute Stage *
   *****************/

  //Seq(
  //  regDE.opcodeOut      -> cu.opcode,
  //  regDE.funct3Out      -> cu.funct3,
  //  regDE.funct7_imm7Out -> cu.funct7_imm7,
  //  regDE.pcOut          -> alu.pc,
  //  alu.out              -> regEM.aluIn
  //).map(
  //  x => x._2 := x._1
  //)
  //Seq(
  //  regDE.dataOut -> alu.in
  //).map(
  //  x => x._1 <> x._2
  //)
  //for (i <- 0 until alu.en.length) {
  //  alu.en(i) := cu.en(0)
  //}


  /****************
   * Memory Stage *
   ****************/

  //Seq(
  //  regEM.aluOut -> regMW.aluIn
  //).map(
  //  x => x._2 := x._1
  //)


  /********************
   * Write Back Stage *
   ********************/

  //Seq(
  //  regMW.aluOut -> regFile.write.bits
  //).map(
  //  x => x._2 := x._1
  //)



  // Debug
  if (Debug) {
    io.debug.get.pc               <> pc

    io.debug.get.iMemJunc.iMemReq <> iMemJunc.iMemReqResp.req
    io.debug.get.iMemJunc.inst    := iMemJunc.inst

    io.debug.get.regFD.out <> regFD.out

    io.debug.get.decoder.opcode      := decoder.opcode
    io.debug.get.decoder.rAddr       <> decoder.rAddr
    io.debug.get.decoder.funct3      := decoder.funct3
    io.debug.get.decoder.funct7_imm7 := decoder.funct7_imm7
    io.debug.get.decoder.imm         := decoder.imm

    io.debug.get.regFile.read <> regFile.read

    io.debug.get.regDE.out <> regDE.out
  }
}
