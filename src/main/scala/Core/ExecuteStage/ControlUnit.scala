package ExecuteStage

import chisel3._
import chisel3.util._


class ControlUnitIO(
  params  :Map[String, Int],
  numCtrl :Int
) extends Bundle {
  // Input pins
  val opcode : UInt = Input(UInt(params("opcodeLen").W))
  val func3  : UInt = Input(UInt(params("f3Len").W))
  val func7  : UInt = Input(UInt(params("f7Len").W))
  val imm    : SInt = Input(SInt(params("XLEN").W))
  val stallEn: Bool = Input(Bool())

  // Output pins
  val en: Vec[Bool] = Output(Vec(numCtrl, Bool()))
}


class ControlUnit(
  params  :Map[String, Int],
  opcodes :Map[String, Map[String, Int]],
  opID    :Map[String, Map[String, Map[String, Int]]],
  ctrlSeq :Seq[String],
  debug   :Boolean
) extends Module {
  val numCtrl: Int = ctrlSeq.length
  val io: ControlUnitIO = IO(new ControlUnitIO(
    params,
    numCtrl+1
  ))

  // Wires
  val idWires: Map[String, UInt] = Map(
    "f7_f3_opcode"  -> Cat(io.func7, io.func3, io.opcode),
    "f3_opcode"     -> Cat(io.func3, io.opcode),
    "imm_f3_opcode" -> Cat(io.imm(11, 5), io.func3, io.opcode)
  )

  val cmpWires: Map[String, Map[String, Bool]] = Map(
    ctrlSeq(0) -> Map(
      "add"   -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "addi"  -> (idWires("f3_opcode"), opID("I")("math")),
      "load"  -> (io.opcode, opcodes("I")),
      "store" -> (io.opcode, opcodes("S"))
    ),
    ctrlSeq(1) -> Map(
      "sub" -> (idWires("f7_f3_opcode"), opID("R")("math"))
    ),
    ctrlSeq(2) -> Map(
      "sll" -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "slli" -> (idWires("imm_f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(3) -> Map(
      "slt"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "slti" -> (idWires("f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(4) -> Map(
      "sltu" -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "sltiu" -> (idWires("f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(5) -> Map(
      "xor"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "xori" -> (idWires("f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(6) -> Map(
      "srl"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "srli" -> (idWires("imm_f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(7) -> Map(
      "sra"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "srai" -> (idWires("imm_f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(8) -> Map(
      "or"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "ori" -> (idWires("f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(9) -> Map(
      "and"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
      "andi" -> (idWires("f3_opcode"), opID("I")("math"))
    ),
    ctrlSeq(10) -> Map(
      "math"  -> (io.opcode, opcodes("I")),
      "load"  -> (io.opcode, opcodes("I")),
      "store" -> (io.opcode, opcodes("S")),
      "auipc" -> (io.opcode, opcodes("U")),
      "lui"   -> (io.opcode, opcodes("U"))
    ),
    ctrlSeq(11) -> Map("auipc" -> (io.opcode, opcodes("U"))),
    ctrlSeq(12) -> Map("lui"   -> (io.opcode, opcodes("U"))),
    ctrlSeq(13) -> Map("load"  -> (io.opcode, opcodes("I"))),
    ctrlSeq(14) -> Map("lb"    -> (idWires("f3_opcode"), opID("I")("load"))),
    ctrlSeq(15) -> Map("lh"    -> (idWires("f3_opcode"), opID("I")("load"))),
    ctrlSeq(16) -> Map("lw"    -> (idWires("f3_opcode"), opID("I")("load"))),
    ctrlSeq(17) -> Map("lbu"   -> (idWires("f3_opcode"), opID("I")("load"))),
    ctrlSeq(18) -> Map("lhu"   -> (idWires("f3_opcode"), opID("I")("load"))),
    ctrlSeq(19) -> Map("lwu"   -> (idWires("f3_opcode"), opID("I")("load"))),
    ctrlSeq(20) -> Map("store" -> (io.opcode, opcodes("S"))),
    ctrlSeq(21) -> Map("sb"    -> (idWires("f3_opcode"), opID("S")("store"))),
    ctrlSeq(22) -> Map("sh"    -> (idWires("f3_opcode"), opID("S")("store"))),
    ctrlSeq(23) -> Map("sw"    -> (idWires("f3_opcode"), opID("S")("store")))
  ).map(
    x => x._1 -> x._2.map(
      y => y._1 -> (y._2._1 === y._2._2(y._1).U)
    )
  )

  val enWires: Map[String, Bool] = (
    for (i <- 0 until numCtrl)
      yield ctrlSeq(i) -> cmpWires(ctrlSeq(i)).values.reduce(
        (x, y) => x || y
      )
  ).toMap

  val wrEn: Bool = Seq(
    opcodes("R")("math"),
    opcodes("I")("math"),
    opcodes("U")("auipc"),
    opcodes("U")("lui"),
    opcodes("J")("jal")
  ).map(
    x => io.opcode === x.U
  ).reduce(
    (x, y) => x || y
  )

  // Connections
  (
    (
      for (i <- 0 until numCtrl)
        yield (io.en(i), enWires(ctrlSeq(i)))
    ) ++ Seq((io.en(numCtrl), wrEn))
  ).map(
    x => x._1 := Mux(io.stallEn, 0.B, x._2)
  )
}
