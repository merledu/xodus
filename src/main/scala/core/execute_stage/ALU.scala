//package xodus.core.execute_stage
//
//import chisel3._,
//       chisel3.util._
//import xodus.configs.Configs,
//       xodus.core.pipeline_regs.RegDE_IO
//
//
//class ALU_IO extends Bundle with Configs {
//  // Input ports
//  val in: Vec[SInt] = Flipped(new RegDE_IO().dataOut)
//  val pc: UInt      = Input(UInt(XLEN.W))
//  val en: Vec[Bool] = Input(Vec(arch("aluEn").length, Bool()))
//
//  // Output ports
//  val out: SInt = Output(SInt(32.W))
//}
//
//
//class ALU extends RawModule with Configs {
//  val io: ALU_IO = IO(new ALU_IO)
//
//  // Wires
//  val sintWires: Map[String, SInt] = (
//    for (i <- 0 to 1)
//      yield s"rs${i + 1}" -> io.in(i)
//  ).toMap ++ Map("imm" -> io.in(2))
//
//  val enWires: Map[String, Bool] = (
//    for (i <- 0 until io.en.length)
//      yield arch("aluEn")(i) -> io.en(i)
//  ).toMap
//
//  val sInputs: Seq[SInt] = Seq(
//    sintWires("rs1"),
//    Mux(enWires("imm"), sintWires("imm"), sintWires("rs2"))
//  )
//
//  val uInputs: Seq[UInt] = Seq(
//    io.pc,
//    Mux(enWires("auipc"), sintWires("imm").asUInt, 4.U)
//  )
//
//  val op: Map[String, SInt] = Seq(
//    sInputs(0) + sInputs(1),                         // s+
//    (sInputs(0) < sInputs(1)).asSInt,                // s<
//    (sInputs(0).asUInt < sInputs(1).asUInt).asSInt,  // u<
//    (sInputs(0) & sInputs(1)).asSInt,                // &
//    (sInputs(0) | sInputs(1)).asSInt,                // |
//    (sInputs(0) ^ sInputs(1)).asSInt,                // ^
//    (sInputs(0) << sInputs(1)(4, 0)).asSInt,         // <<
//    (sInputs(0).asUInt >> sInputs(1)(4, 0)).asSInt,  // >>
//    (sInputs(0) >> sInputs(1)(4, 0)),                // >>>
//    sInputs(1),                                      // lui
//    (uInputs(0) + uInputs(1)).asSInt,                // u+
//    sInputs(0) - sInputs(1)                          // -
//  ).view.zipWithIndex.map(
//    x => arch("aluEn")(x._2) -> x._1
//  ).toMap
//
//  // Output Selection
//  // Default: s+
//  io.out := MuxCase(op("s+"), op.keys.view.slice(1, op.size).map(
//    x => enWires(x) -> op(x)
//  ).toSeq)
//
//
//
//  if (Debug) {
//    val debug_rs1                      : SInt = dontTouch(WireInit(sintWires("rs1")))
//    val debug_rs2                      : SInt = dontTouch(WireInit(sintWires("rs2")))
//    val debug_imm                      : SInt = dontTouch(WireInit(sintWires("imm")))
//    val debug_sInput1                  : SInt = dontTouch(WireInit(sInputs(0)))
//    val debug_sInput2                  : SInt = dontTouch(WireInit(sInputs(1)))
//    val debug_uInput1                  : UInt = dontTouch(WireInit(uInputs(0)))
//    val debug_uInput2                  : UInt = dontTouch(WireInit(uInputs(1)))
//    val debug_signed_addition_en       : Bool = dontTouch(WireInit(enWires("s+")))
//    val debug_signed_less_than_en      : Bool = dontTouch(WireInit(enWires("s<")))
//    val debug_unsigned_less_than_en    : Bool = dontTouch(WireInit(enWires("u<")))
//    val debug_and_en                   : Bool = dontTouch(WireInit(enWires("&")))
//    val debug_or_en                    : Bool = dontTouch(WireInit(enWires("|")))
//    val debug_xor_en                   : Bool = dontTouch(WireInit(enWires("^")))
//    val debug_shift_left_logical_en    : Bool = dontTouch(WireInit(enWires("<<")))
//    val debug_shift_right_logical_en   : Bool = dontTouch(WireInit(enWires(">>")))
//    val debug_shift_right_arithmetic_en: Bool = dontTouch(WireInit(enWires(">>>")))
//    val debug_lui_en                   : Bool = dontTouch(WireInit(enWires("lui")))
//    val debug_unsigned_addition_en     : Bool = dontTouch(WireInit(enWires("u+")))
//    val debug_subtraction_en           : Bool = dontTouch(WireInit(enWires("-")))
//    val debug_imm_en                   : Bool = dontTouch(WireInit(enWires("imm")))
//    val debug_auipc_en                 : Bool = dontTouch(WireInit(enWires("auipc")))
//    val debug_signed_addition          : SInt = dontTouch(WireInit(op("s+")))
//    val debug_signed_less_than         : SInt = dontTouch(WireInit(op("s<")))
//    val debug_unsigned_less_than       : SInt = dontTouch(WireInit(op("u<")))
//    val debug_and                      : SInt = dontTouch(WireInit(op("&")))
//    val debug_or                       : SInt = dontTouch(WireInit(op("|")))
//    val debug_xor                      : SInt = dontTouch(WireInit(op("^")))
//    val debug_shift_left_logical       : SInt = dontTouch(WireInit(op("<<")))
//    val debug_shift_right_logical      : SInt = dontTouch(WireInit(op(">>")))
//    val debug_shift_right_arithmetic   : SInt = dontTouch(WireInit(op(">>>")))
//    val debug_lui                      : SInt = dontTouch(WireInit(op("lui")))
//    val debug_unsigned_addition        : SInt = dontTouch(WireInit(op("u+")))
//    val debug_subtraction              : SInt = dontTouch(WireInit(op("-")))
//  }
//}
