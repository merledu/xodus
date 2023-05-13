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

  // Wires
  val sintWires = (
    for (i <- 0 to 1)
      yield s"rs${i + 1}" -> io.in(i)
  ).toMap ++ Map("imm" -> io.in(2))

  val enWires = (
    for (i <- 0 until io.en.length)
      yield arch("aluEn")(i) -> io.en(i)
  ).toMap

  val sInputs = Seq(
    sintWires("rs1"),
    Mux(enWires("imm"), sintWires("imm"), sintWires("rs2"))
  )

  val uInputs = Seq(
    io.pc,
    Mux(enWires("auipc"), sintWires("imm").asUInt, 4.U)
  )

  val op = Seq(
    sInputs(0) + sInputs(1),                         // s+
    (sInputs(0) < sInputs(1)).asSInt,                // s<
    (sInputs(0).asUInt < sInputs(1).asUInt).asSInt,  // u<
    (sInputs(0) & sInputs(1)).asSInt,                // &
    (sInputs(0) | sInputs(1)).asSInt,                // |
    (sInputs(0) ^ sInputs(1)).asSInt,                // ^
    (sInputs(0) << sInputs(1)(4, 0)).asSInt,         // <<
    (sInputs(0).asUInt >> sInputs(1)(4, 0)).asSInt,  // >>
    (sInputs(0) >> sInputs(1)(4, 0)),                // >>>
    sInputs(1),                                      // lui
    (uInputs(0) + uInputs(1)).asSInt,                // u+
    sInputs(0) - sInputs(1)                          // -
  ).view.zipWithIndex.map(
    x => arch("aluEn")(x._2) -> x._1
  ).toMap

  // Output Selection
  // Default: s+
  io.out := MuxCase(op("s+"), op.keys.view.slice(1, op.size).map(
    x => enWires(x) -> op(x)
  ).toSeq)



  if (Debug) {
    val debug_rs1                       = dontTouch(WireInit(sintWires("rs1")))
    val debug_rs2                       = dontTouch(WireInit(sintWires("rs2")))
    val debug_imm                       = dontTouch(WireInit(sintWires("imm")))
    val debug_sInput1                   = dontTouch(WireInit(sInputs(0)))
    val debug_sInput2                   = dontTouch(WireInit(sInputs(1)))
    val debug_uInput1                   = dontTouch(WireInit(uInputs(0)))
    val debug_uInput2                   = dontTouch(WireInit(uInputs(1)))
    val debug_signed_addition_en        = dontTouch(WireInit(enWires("s+")))
    val debug_signed_less_than_en       = dontTouch(WireInit(enWires("s<")))
    val debug_unsigned_less_than_en     = dontTouch(WireInit(enWires("u<")))
    val debug_and_en                    = dontTouch(WireInit(enWires("&")))
    val debug_or_en                     = dontTouch(WireInit(enWires("|")))
    val debug_xor_en                    = dontTouch(WireInit(enWires("^")))
    val debug_shift_left_logical_en     = dontTouch(WireInit(enWires("<<")))
    val debug_shift_right_logical_en    = dontTouch(WireInit(enWires(">>")))
    val debug_shift_right_arithmetic_en = dontTouch(WireInit(enWires(">>>")))
    val debug_lui_en                    = dontTouch(WireInit(enWires("lui")))
    val debug_unsigned_addition_en      = dontTouch(WireInit(enWires("u+")))
    val debug_subtraction_en            = dontTouch(WireInit(enWires("-")))
    val debug_imm_en                    = dontTouch(WireInit(enWires("imm")))
    val debug_auipc_en                  = dontTouch(WireInit(enWires("auipc")))
    val debug_signed_addition           = dontTouch(WireInit(op("s+")))
    val debug_signed_less_than          = dontTouch(WireInit(op("s<")))
    val debug_unsigned_less_than        = dontTouch(WireInit(op("u<")))
    val debug_and                       = dontTouch(WireInit(op("&")))
    val debug_or                        = dontTouch(WireInit(op("|")))
    val debug_xor                       = dontTouch(WireInit(op("^")))
    val debug_shift_left_logical        = dontTouch(WireInit(op("<<")))
    val debug_shift_right_logical       = dontTouch(WireInit(op(">>")))
    val debug_shift_right_arithmetic    = dontTouch(WireInit(op(">>>")))
    val debug_lui                       = dontTouch(WireInit(op("lui")))
    val debug_unsigned_addition         = dontTouch(WireInit(op("u+")))
    val debug_subtraction               = dontTouch(WireInit(op("-")))
  }
}
