package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DecoderIO


class ControlUnitIO extends Bundle with Configs {
  // Input ports
  val opcode = Flipped(new DecoderIO().opcode)
  val funct3 = Flipped(new DecoderIO().funct3)
  val funct7 = Flipped(new DecoderIO().funct7)
  val imm    = Flipped(new DecoderIO().imm)

  // Output ports
  val en = Output(Vec(arch("cuEn")("aluEn").length, Bool()))
}


class ControlUnit extends Module with Configs {
  val io = IO(new ControlUnitIO)

  // Wire Maps
  val uintWires = Map(
    "funct3_opcode"          -> Cat(io.funct3, io.opcode),
    "funct7_funct3_opcode"   -> Cat(io.funct7, io.funct3, io.opcode),
    "imm31_25_funct3_opcode" -> Cat(io.imm(31, 25), io.funct3, io.opcode)
  )

  val enWires = 

  // Interconnections
  //for (i <- 0 until io.en.length) {
  //  io.en(i) := enWires.values.toSeq(i)
  //}



  // Debug
  if (Debug) {
    //val debug_imm        : Bool = dontTouch(WireInit(enWires("imm")))
    //val debug_addition   : Bool = dontTouch(WireInit(enWires("+")))
    //val debug_signed_lt  : Bool = dontTouch(WireInit(enWires("signed <")))
    //val debug_unsigned_lt: Bool = dontTouch(WireInit(enWires("unsigned <")))
  }
}
