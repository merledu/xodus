package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DecoderIO


class ControlUnitIO extends Bundle with Configs {
  // Input ports
  val opcode      = Flipped(new DecoderIO().opcode)
  val funct3      = Flipped(new DecoderIO().funct3)
  val funct7_imm7 = Flipped(new DecoderIO().funct7_imm7)

  // Output ports
  val en = Output(Vec(arch("cuEn").length, Bool()))
}


class ControlUnit extends Module with Configs {
  val io = IO(new ControlUnitIO)

  // Wire Maps
  val idWires = Map(
    "opcode"                          -> io.opcode,
    "funct3_opcode"                   -> Cat(io.funct3, io.opcode),
    "funct7/imm(11, 5)_funct3_opcode" -> Cat(io.funct7_imm7, io.funct3, io.opcode),
  )

  // Enable Wires
  //val instEn: Map[String, Bool] = Map(
  //  "funct3_opcode"          -> (0, 21),
  //  "imm31_25_funct3_opcode" -> (21, 24),
  //  "opcode"                 -> (24, 27),
  //  "funct7_funct3_opcode"   -> (27, isa("insts").size)
  //).map(
  //  x => x._1 -> isa("insts").slice(x._2._1, x._2._2)
  //).map(
  //  x => x._1 -> x._2.map(
  //    y => y._1 -> y._2.values.map(
  //      z => z.U
  //    ).reduce(
  //      (a, b) => Cat(a, b)
  //    )
  //  )
  //).map(
  //  x => .
  //  )

  // Interconnections
  //for (i <- 0 until io.en.length) {
  //  io.en(i) := enWires(i)
  //}



  // Debug
  if (Debug) {
    //val debug_imm        : Bool = dontTouch(WireInit(enWires("imm")))
    //val debug_addition   : Bool = dontTouch(WireInit(enWires("+")))
    //val debug_signed_lt  : Bool = dontTouch(WireInit(enWires("signed <")))
    //val debug_unsigned_lt: Bool = dontTouch(WireInit(enWires("unsigned <")))
  }
}
