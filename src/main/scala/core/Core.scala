package xodus.core

import chisel3._
import xodus.core.decode_stage._, xodus.core.pipeline_regs._, xodus.core.execute_stage._


class Core extends Module {
  // Modules
  val decoder: DecoderIO = Module(new Decoder).io
  val regFile: RegFileIO = Module(new RegFile).io

  val regDE: RegDE_IO = Module(new RegDE).io

  val cu : ControlUnitIO = Module(new ControlUnit).io
  val alu: ALU_IO        = Module(new ALU).io

  val regEM: RegEM_IO = Module(new RegEM).io

  // Interconnections
  // - Decode Stage
  Seq(regFile.rAddr, regDE.rAddrIn).map(
    x => x <> decoder.rAddr
  )
  Seq(
    (regFile.write.bits, 0.S),
    (regFile.write.valid, 0.B),
    (decoder.inst, 0x01900293.U),
    (regDE.opcodeIn, decoder.opcode),
    (regDE.funct3In, decoder.funct3),
    (regDE.funct7In, decoder.funct7),
    (regDE.dataIn(2), decoder.imm)
  ).map(
    x => x._1 := x._2
  )
  for (i <- 0 to 1) {
    regDE.dataIn(i) := regFile.read(i)
  }

  // - Execute Stage
  Seq(
    (cu.opcode, regDE.opcodeOut),
    (cu.funct3, regDE.funct3Out),
    (cu.funct7, regDE.funct7Out),
    (regEM.aluIn, alu.out)
  ).map(
    x => x._1 := x._2
  )
  Seq(
    (alu.in, regDE.dataOut),
    (alu.en, cu.aluEn)
  ).map(
    x => x._1 <> x._2
  )
}
