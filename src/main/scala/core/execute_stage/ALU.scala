package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.pipeline_regs.RegDE_IO


class ALU_IO extends Bundle with Configs {
  // Input ports
  val in: Vec[SInt] = Flipped(new RegDE_IO().dataOut)
  val pc: UInt      = Flipped(new RegDE_IO().pcOut)
  val en: Vec[Bool] = Input(Vec(12, Bool()))

  // Output ports
  val out: SInt = Output(SInt(32.W))
}


class ALU extends Module with Configs {
  val io: ALU_IO = IO(new ALU_IO)

  val en: Seq[String] = new ControlUnit().aluEn

  // Wire Maps
  val sintWires: Map[String, SInt] = (
    for (i <- 0 to 1)
      yield s"rs${i + 1}" -> io.in(i)
  ).toMap ++ Map("imm" -> io.in(2))

  val boolWires: Map[String, Bool] = (
    for (i <- 0 until io.en.length)
      yield en(i) -> io.en(i)
  ).toMap

  val inputs: Seq[SInt] = Seq(
    sintWires("rs1"),
    Mux(boolWires("imm"), sintWires("imm"), sintWires("rs2"))
  )

  val op: Map[String, SInt] = Seq(
    inputs(0) + inputs(1),
    (inputs(0) < inputs(1)).asSInt,
    (inputs(0).asUInt < inputs(1).asUInt).asSInt,
    (inputs(0) & inputs(1)).asSInt,
    (inputs(0) | inputs(1)).asSInt,
    (inputs(0) ^ inputs(1)).asSInt,
    (inputs(0) << inputs(1)(4, 0)).asSInt,
    (inputs(0).asUInt >> inputs(1)(4, 0)).asSInt,
    (inputs(0) >> inputs(1)(4, 0)),
    inputs(1),
    inputs(0) - inputs(1),
  ).zipWithIndex.map(
    x => en(x._2) -> x._1
  ).toMap

  // Interconnections
  io.out := MuxCase(op("+"), op.keys.slice(1, op.size).map(
    x => boolWires(x) -> op(x)
  ).toSeq)



  if (Debug) {
    val debug_addition   : SInt = dontTouch(WireInit(op("+")))
    val debug_signed_lt  : SInt = dontTouch(WireInit(op("signed <")))
    val debug_unsigned_lt: SInt = dontTouch(WireInit(op("unsigned <")))
  }
}
