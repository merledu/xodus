package xodus.core

import chisel3._
import xodus.configs.Configs,
       xodus.core.fetch_stage._,
       /*xodus.core.decode_stage._,
       xodus.core.execute_stage._,*/
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

  //val decoder: DecoderIO = Module(new Decoder).io
  //val regFile: RegFileIO = Module(new RegFile).io

  //val regDE: RegDE_IO = Module(new RegDE).io

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
  io.iMem <> iMemJunc.iMemReqResp


  /****************
   * Decode Stage *
   ****************/

  //Seq(regFile.rAddr, regDE.rAddrIn).map(
  //  x => decoder.rAddr <> x
  //)
  //Seq(
  //  0.S                 -> regFile.write.bits,
  //  0.B                 -> regFile.write.valid,
  //  regFD.instOut       -> decoder.inst,
  //  decoder.opcode      -> regDE.opcodeIn,
  //  decoder.funct3      -> regDE.funct3In,
  //  decoder.funct7_imm7 -> regDE.funct7_imm7In,
  //  decoder.imm         -> regDE.dataIn(2),
  //  regFD.pcOut         -> regDE.pcIn
  //).map(
  //  x => x._2 := x._1
  //)
  //for (i <- 0 to 1) {
  //  regDE.dataIn(i) := regFile.read(i)
  //}


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
    io.debug.get.regFD.out        <> regFD.out
  }
}
