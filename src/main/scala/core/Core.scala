package xodus.core

import chisel3._
import xodus.core.fetch_stage._,
       xodus.core.decode_stage._,
       xodus.core.pipeline_regs._,
       xodus.core.execute_stage._


class Core extends Module {
  // Modules
  val pc: PC_IO = Module(new PC).io

  val regFD: RegFD_IO = Module(new RegFD).io

  val decoder: DecoderIO = Module(new Decoder).io
  val regFile: RegFileIO = Module(new RegFile).io

  val regDE: RegDE_IO = Module(new RegDE).io

  //val cu : ControlUnitIO = Module(new ControlUnit).io
  //val alu: ALU_IO        = Module(new ALU).io

  //val regEM: RegEM_IO = Module(new RegEM).io

  //val regMW: RegMW_IO = Module(new RegMW).io

  // Fetch Stage
  Seq(
    pc.pc   -> regFD.pcIn,
    pc.addr -> regFD.addrIn
  ).map(
    x => x._2 := x._1
  )

  // Decode Stage
  Seq(regFile.rAddr, regDE.rAddrIn).map(
    x => decoder.rAddr <> x
  )
  Seq(
    0.S            -> regFile.write.bits,
    0.B            -> regFile.write.valid,
    0x01900293.U   -> decoder.inst,
    decoder.opcode -> regDE.opcodeIn,
    decoder.funct3 -> regDE.funct3In,
    decoder.funct7 -> regDE.funct7In,
    decoder.imm    -> regDE.dataIn(2),
    regFD.pcOut    -> regDE.pcIn
  ).map(
    x => x._2 := x._1
  )
  for (i <- 0 to 1) {
    regDE.dataIn(i) := regFile.read(i)
  }

  // Execute Stage
  //Seq(
  //  regDE.opcodeOut  -> cu.opcode,
  //  regDE.funct3Out  -> cu.funct3,
  //  regDE.funct7Out  -> cu.funct7,
  //  regDE.dataOut(2) -> cu.imm,
  //  regDE.pcOut      -> alu.pc,
  //  alu.out          -> regEM.aluIn
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

  // Memory Stage
  //Seq(
  //  regEM.aluOut -> regMW.aluIn
  //).map(
  //  x => x._2 := x._1
  //)

  // Write-Back Stage
  //Seq(
  //  regMW.aluOut -> regFile.write.bits
  //).map(
  //  x => x._2 := x._1
  //)
}
