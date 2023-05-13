package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.pipeline_regs.RegDE_IO


class ALU_IO extends Bundle with Configs {
  // Input ports
  val in = Flipped(new RegDE_IO().dataOut)
  val pc = Flipped(new RegDE_IO().pcOut)
  val en = Input(Vec(arch("aluEn").length, Bool()))

  // Output ports
  val out = Output(SInt(32.W))
}


class ALU extends Module with Configs {
  val io = IO(new ALU_IO)

  // Wire Maps
  val sintWires = (
    for (i <- 0 to 1)
      yield s"rs${i + 1}" -> io.in(i)
  ).toMap ++ Map("imm" -> io.in(2))

  val boolWires = (
    for (i <- 0 until io.en.length)
      yield arch("aluEn")(i) -> io.en(i)
  ).toMap

  val inputs = Seq(
    sintWires("rs1"),
    Mux(boolWires("imm"), sintWires("imm"), sintWires("rs2"))
  )

  val op = Seq(
    inputs(0) + inputs(1),                         // +
    (inputs(0) < inputs(1)).asSInt,                // s<
    (inputs(0).asUInt < inputs(1).asUInt).asSInt,  // u<
    (inputs(0) & inputs(1)).asSInt,                // &
    (inputs(0) | inputs(1)).asSInt,                // |
    (inputs(0) ^ inputs(1)).asSInt,                // ^
    (inputs(0) << inputs(1)(4, 0)).asSInt,         // <<
    (inputs(0).asUInt >> inputs(1)(4, 0)).asSInt,  // >>
    (inputs(0) >> inputs(1)(4, 0)),                // >>>
    inputs(1),                                     // lui
    (io.pc + inputs(1).asUInt).asSInt,             // auipc
    inputs(0) - inputs(1)                          // -
  ).view.zipWithIndex.map(
    x => arch("aluEn")(x._2) -> x._1
  ).toMap

  // Output Selection
  // Default: +
  io.out := MuxCase(op("+"), op.keys.view.slice(1, op.size).map(
    x => boolWires(x) -> op(x)
  ).toSeq)



  if (Debug) {
    val debug_rs1                       = dontTouch(WireInit(sintWires("rs1")))
    val debug_rs2                       = dontTouch(WireInit(sintWires("rs2")))
    val debug_imm                       = dontTouch(WireInit(sintWires("imm")))
    val debug_input1                    = dontTouch(WireInit(inputs(0)))
    val debug_input2                    = dontTouch(WireInit(inputs(1)))
    val debug_addition_en               = dontTouch(WireInit(boolWires("+")))
    val debug_signed_less_than_en       = dontTouch(WireInit(boolWires("s<")))
    val debug_unsigned_less_than_en     = dontTouch(WireInit(boolWires("u<")))
    val debug_and_en                    = dontTouch(WireInit(boolWires("&")))
    val debug_or_en                     = dontTouch(WireInit(boolWires("|")))
    val debug_xor_en                    = dontTouch(WireInit(boolWires("^")))
    val debug_shift_left_logical_en     = dontTouch(WireInit(boolWires("<<")))
    val debug_shift_right_logical_en    = dontTouch(WireInit(boolWires(">>")))
    val debug_shift_right_arithmetic_en = dontTouch(WireInit(boolWires(">>>")))
    val debug_lui_en                    = dontTouch(WireInit(boolWires("lui")))
    val debug_auipc_en                  = dontTouch(WireInit(boolWires("auipc")))
    val debug_subtraction_en            = dontTouch(WireInit(boolWires("-")))
    val debug_addition                  = dontTouch(WireInit(op("+")))
    val debug_signed_less_than          = dontTouch(WireInit(op("s<")))
    val debug_unsigned_less_than        = dontTouch(WireInit(op("u<")))
    val debug_and                       = dontTouch(WireInit(op("&")))
    val debug_or                        = dontTouch(WireInit(op("|")))
    val debug_xor                       = dontTouch(WireInit(op("^")))
    val debug_shift_left_logical        = dontTouch(WireInit(op("<<")))
    val debug_shift_right_logical       = dontTouch(WireInit(op(">>")))
    val debug_shift_right_arithmetic    = dontTouch(WireInit(op(">>>")))
    val debug_lui                       = dontTouch(WireInit(op("lui")))
    val debug_auipc                     = dontTouch(WireInit(op("auipc")))
    val debug_subtraction               = dontTouch(WireInit(op("-")))
  }
}
