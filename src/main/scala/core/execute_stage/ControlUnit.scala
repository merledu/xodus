package xodus.core.execute_stage

import chisel3._, chisel3.util._
import xodus.configs.Configs, xodus.core.decode_stage.DecoderIO


class ControlUnitIO extends Bundle with Configs {
  // Input ports
  val opcode: UInt = Flipped(new DecoderIO().opcode)
  val funct3: UInt = Flipped(new DecoderIO().funct3)
  val funct7: UInt = Flipped(new DecoderIO().funct7)

  // Output ports
  val aluEn: Vec[Bool] = Output(Vec(10, Bool()))
}


class ControlUnit extends Module with Configs {
  val io: ControlUnitIO = IO(new ControlUnitIO)

  // Wire Maps
  val enWires: Map[String, Bool] = Map(  // Other
    "imm" -> opcodes("I").values.map(
      x => io.opcode === x.U
    ).reduce(
      (x, y) => x || y
    )
  ) ++ Map(  // funct3 + opcode
    "+"          -> "addi",
    "signed <"   -> "slti",
    "unsigned <" -> "sltiu",
    "&"          -> "andi",
    "|"          -> "ori",
    "^"          -> "xori",
    "<<"         -> "slli",
    ">>"         -> "srli",
    ">>>"        -> "srai"
  ).map(
    x => x._1 -> (
      Cat(io.funct3, io.opcode) === Cat(insts(x._2)("funct3").U, insts(x._2)("opcode").U)
    )
  )

  // Interconnections
  for (i <- 0 until io.aluEn.length) {
    io.aluEn(i) := enWires.values.toSeq(i)
  }



  // Debug
  if (Debug) {
    val debug_imm        : Bool = dontTouch(WireInit(enWires("imm")))
    val debug_addition   : Bool = dontTouch(WireInit(enWires("+")))
    val debug_signed_lt  : Bool = dontTouch(WireInit(enWires("signed <")))
    val debug_unsigned_lt: Bool = dontTouch(WireInit(enWires("unsigned <")))
  }
}
