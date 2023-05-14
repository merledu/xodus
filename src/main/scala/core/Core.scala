package xodus.core

import chisel3._
import xodus.core.fetch_stage._,
       xodus.core.decode_stage._,
       xodus.core.pipeline_regs._,
       xodus.core.execute_stage._,
       xodus.memory.MemoryIO


class CoreIO extends Bundle {
  val iMem: MemoryIO = Flipped(new MemoryIO)
  //val dMem: MemoryIO = Flipped(new MemoryIO)
}


class Core extends Module {
  val io: CoreIO = IO(new CoreIO)

  // Modules
  val pc      : PC_IO         = Module(new PC).io
  val iMemJunc: InstMemJuncIO = Module(new InstMemJunc).io

  val regFD: RegFD_IO = Module(new RegFD).io

  val decoder: DecoderIO = Module(new Decoder).io
  val regFile: RegFileIO = Module(new RegFile).io

  val regDE: RegDE_IO = Module(new RegDE).io

  val cu : ControlUnitIO = Module(new ControlUnit).io
  val alu: ALU_IO        = Module(new ALU).io

  val regEM: RegEM_IO = Module(new RegEM).io

  //val regMW: RegMW_IO = Module(new RegMW).io


  /***************
   * Fetch Stage *
   ***************/

  io.iMem.req.en <> iMemJunc.en
  Seq(
    pc.pc            -> regFD.pcIn,
    pc.addr          -> iMemJunc.addrIn,
    iMemJunc.addrOut -> io.iMem.req.addr,
    0.U              -> io.iMem.req.data,
    io.iMem.rsp.data -> iMemJunc.instIn,
    iMemJunc.instOut -> regFD.instIn
  ).map(
    x => x._2 := x._1
  )


  /****************
   * Decode Stage *
   ****************/

  Seq(regFile.rAddr, regDE.rAddrIn).map(
    x => decoder.rAddr <> x
  )
  Seq(
    0.S                 -> regFile.write.bits,
    0.B                 -> regFile.write.valid,
    regFD.instOut       -> decoder.inst,
    decoder.opcode      -> regDE.opcodeIn,
    decoder.funct3      -> regDE.funct3In,
    decoder.funct7_imm7 -> regDE.funct7_imm7In,
    decoder.imm         -> regDE.dataIn(2),
    regFD.pcOut         -> regDE.pcIn
  ).map(
    x => x._2 := x._1
  )
  for (i <- 0 to 1) {
    regDE.dataIn(i) := regFile.read(i)
  }


  /*****************
   * Execute Stage *
   *****************/

  Seq(
    regDE.opcodeOut      -> cu.opcode,
    regDE.funct3Out      -> cu.funct3,
    regDE.funct7_imm7Out -> cu.funct7_imm7,
    regDE.pcOut          -> alu.pc,
    alu.out              -> regEM.aluIn
  ).map(
    x => x._2 := x._1
  )
  Seq(
    regDE.dataOut -> alu.in
  ).map(
    x => x._1 <> x._2
  )
  for (i <- 0 until alu.en.length) {
    alu.en(i) := cu.en(0)
  }


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
  val debug_PC_pc              : UInt = dontTouch(WireInit(pc.pc))
  val debug_PC_addr            : UInt = dontTouch(WireInit(pc.addr))
  val debug_InstMemJunc_addrOut: UInt = dontTouch(WireInit(iMemJunc.addrOut))
  val debug_InstMemJunc_instOut: UInt = dontTouch(WireInit(iMemJunc.instOut))
  val debug_InstMemJunc_en0    : Bool = dontTouch(WireInit(iMemJunc.en(0)))
  val debug_InstMemJunc_en1    : Bool = dontTouch(WireInit(iMemJunc.en(1)))
  val debug_iMem_rsp_data      : UInt = dontTouch(WireInit(io.iMem.rsp.data))

  val debug_RegFD_pcOut   = dontTouch(WireInit(regFD.pcOut))

  val debug_Decoder_opcode      = dontTouch(WireInit(decoder.opcode))
  val debug_Decoder_rd          = dontTouch(WireInit(decoder.rAddr(0)))
  val debug_Decoder_funct3      = dontTouch(WireInit(decoder.funct3))
  val debug_Decoder_rs1         = dontTouch(WireInit(decoder.rAddr(1)))
  val debug_Decoder_rs2         = dontTouch(WireInit(decoder.rAddr(2)))
  val debug_Decoder_funct7_imm7 = dontTouch(WireInit(decoder.funct7_imm7))
  val debug_imm                 = dontTouch(WireInit(decoder.imm))
  val debug_RegFile_rs1_data    = dontTouch(WireInit(regFile.read(0)))
  val debug_RegFile_rs2_data    = dontTouch(WireInit(regFile.read(1)))

  val debug_RegDE_opcodeOut      = dontTouch(WireInit(regDE.opcodeOut))
  val debug_RegDE_rdOut          = dontTouch(WireInit(regDE.rAddrOut(0)))
  val debug_RegDE_funct3Out      = dontTouch(WireInit(regDE.funct3Out))
  val debug_RegDE_rs1Out         = dontTouch(WireInit(regDE.rAddrOut(1)))
  val debug_RegDE_rs2Out         = dontTouch(WireInit(regDE.rAddrOut(2)))
  val debug_RegDE_funct7_imm7Out = dontTouch(WireInit(regDE.funct7_imm7Out))
  val debug_RegDE_immOut         = dontTouch(WireInit(regDE.dataOut(2)))
  val debug_RegDE_rs1_dataOut    = dontTouch(WireInit(regDE.dataOut(0)))
  val debug_RegDE_rs2_dataOut    = dontTouch(WireInit(regDE.dataOut(1)))
  val debug_RegDE_pcOut          = dontTouch(WireInit(regDE.pcOut))

  val debug_ControlUnit_addition_en = dontTouch(WireInit(cu.en(0)))
}
