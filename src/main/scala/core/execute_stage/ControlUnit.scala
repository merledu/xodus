package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.{DecoderIO, RegFileIO}


class ControlUnitIO extends Bundle with Configs {
  // Input ports
  val opcode: UInt = Flipped(new DecoderIO().opcode)
  val funct3: UInt = Flipped(new DecoderIO().funct3)
  val funct7: UInt = Flipped(new DecoderIO().funct7)
  val imm   : SInt = Flipped(new DecoderIO().imm)

  // Output ports
  val en: Vec[Bool] = Output(Vec(13, Bool()))
}


class ControlUnit extends Module with Configs {
  val io: ControlUnitIO = IO(new ControlUnitIO)

  val aluEn: Seq[String] = Seq(
    "+",     "signed <", "unsigned <", "&",    "|",
    "^",     "<<",       ">>",         ">>>",  "lui",
    "auipc", "-",        "imm"
  )

  // Wire Maps
  val uintWires: Map[String, UInt] = Map(
    "funct3_opcode"        -> Cat(io.funct3, io.opcode),
    "funct7_funct3_opcode" -> Cat(io.funct7, io.funct3, io.opcode),
    "imm31_25_funct3_opcode" -> Cat(io.imm(31, 25), io.funct3, io.opcode)
  )

  val enWires: Map[String, Map[String, Bool]] = Map(
    "aluEn" -> Seq((
      Seq("addi", "load", "store").map(
        x => x -> Cat(insts(x)("funct3"), insts(x)("opcode"))
      ) ++ Seq(
        "add" -> Cat(, insts("add")("funct3"), insts("add")("opcode"))
      )).map(
        x => x._2 === uintWires("")
      )
    )
  )

  // Interconnections
  for (i <- 0 until io.en.length) {
    io.en(i) := enWires.values.toSeq(i)
  }



  // Debug
  if (Debug) {
    val debug_imm        : Bool = dontTouch(WireInit(enWires("imm")))
    val debug_addition   : Bool = dontTouch(WireInit(enWires("+")))
    val debug_signed_lt  : Bool = dontTouch(WireInit(enWires("signed <")))
    val debug_unsigned_lt: Bool = dontTouch(WireInit(enWires("unsigned <")))
  }
}
