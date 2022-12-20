package ExecuteStage

import chisel3._
import chisel3.util._


class ControlUnitIO(
  params  :Map[String, Int],
  ctrlNum :Int
) extends Bundle {
  // Input pins
  val opcode : UInt      = Input(UInt(params("opcodeLen").W))
  val func3  : UInt      = Input(UInt(params("f3Len").W))
  val func7  : UInt      = Input(UInt(params("f7Len").W))
  val imm    : SInt      = Input(SInt(params("XLEN").W))
  val boolVec: Vec[Bool] = Input(Vec(3, Bool()))

  // Output pins
  val en: Vec[Bool] = Output(Vec(ctrlNum, Bool()))
}


class ControlUnit(
  params  :Map[String, Int],
  opcodes :Map[String, Map[String, Int]],
  opID    :Map[String, Map[String, Map[String, Int]]],
  ctrlSeq :Seq[String],
  debug   :Boolean
) extends Module {
  val ctrlNum: Int = ctrlSeq.length - 2
  val io: ControlUnitIO = IO(new ControlUnitIO(params, ctrlNum))

  // Wires
  val boolWires: Map[String, Bool] = Map(
    "stallEn" -> io.boolVec(0),
    "jalr"    -> io.boolVec(1),
    "jal"     -> io.boolVec(2)
    )

  val idWires: Map[String, UInt] = Map(
    "f7_f3_opcode"  -> Cat(io.func7, io.func3, io.opcode),
    "f3_opcode"     -> Cat(io.func3, io.opcode),
    "imm_f3_opcode" -> Cat(io.imm(11, 5), io.func3, io.opcode)
    )

  val enWires: Map[String, Bool] = Map(
    ctrlSeq(0) -> Map(
      "add"   -> (idWires("f7_f3_opcode") === opID("R")("math")("add").U),
      "addi"  -> (idWires("f3_opcode") === opID("I")("math")("addi").U),
      "load"  -> (io.opcode === opcodes("I")("load").U),
      "store" -> (io.opcode === opcodes("I")("store").U)
      ),
    ctrlSeq(1) -> Map("sub" -> (idWires("f7_f3_opcode") === opID("R")("math")("sub").U)),
    ctrlSeq(2) -> Map(
      "sll"   -> (idWires("f7_f3_opcode") === opID("R")("math")("sll").U),
      "slli"  -> (idWires("imm_f3_opcode") === opID("I")("math")("slli").U),
      ),
    ctrlSeq(3) -> Map(
      "slt"  -> (idWires("f7_f3_opcode") === opID("R")("math")("slt").U),
      "slti" -> (idWires("f3_opcode") === opID("I")("math")("slti").U)
      ),
    ctrlSeq(4) -> Map(
      "sltu"  -> (idWires("f7_f3_opcode") === opID("R")("math")("slt").U),
      "sltiu" -> (idWires("f3_opcode") === opID("I")("math")("sltiu").U)
      ),
    ctrlSeq(5) -> Map(
      "xor"  -> (idWires("f7_f3_opcode") === opID("R")("math")("xor").U),
      "xori" -> (idWires("f3_opcode") === opID("I")("math")("xori").U)
      ),
    ctrlSeq(6) -> Map(
      "srl"  -> (idWires("f7_f3_opcode") === opID("R")("math")("srl").U),
      "srli" -> (idWires("imm_f3_opcode") === opID("I")("math")("srli").U),
      ),
    ctrlSeq(7) -> Map(
      "sra" -> (idWires("f7_f3_opcode") === opID("R")("math")("sra").U),
      "srai" -> (idWires("imm_f3_opcode") === opID("I")("math")("srai").U)
      ),
    ctrlSeq(8) -> Map(
      "or"  -> (idWires("f7_f3_opcode") === opID("R")("math")("or").U),
      "ori" -> (idWires("f3_opcode") === opID("I")("math")("ori").U)
      ),
    ctrlSeq(9) -> Map(
      "and"  -> (idWires("f7_f3_opcode") === opID("R")("math")("and").U),
      "andi" -> (idWires("f3_opcode") === opID("I")("math")("andi").U)
      ),
    ctrlSeq(10) -> Map(
      "mathI" -> (io.opcode === opcodes("I")("math").U),
      "load" -> (io.opcode === opcodes("I")("load").U)
      "jalr" -> (io.opcode === opcodes("I")("jalr").U)
      "immS" -> (io.opcode === opcodes("S")("").U)
      ),
    ctrlSeq(11) -> Map("auipc" -> (io.opcode === opcodes("U")("auipc").U)),
    ctrlSeq(12) -> Map("lui" -> (io.opcode === opcodes("U")("lui").U)),
    ctrlSeq(13) -> Map("load" -> (io.opcode === opcodes("I")("load").U)),
    ctrlSeq(14) -> Map("lb" -> (idWires("f3_opcode") === opID("I")("load")("lb").U)),
    ctrlSeq(15) -> Map("lh" -> (idWires("f3_opcode") === opID("I")("load")("lh").U)),
    ctrlSeq(16) -> Map("lw" -> (idWires("f3_opcode") === opID("I")("load")("lw").U)),
    ctrlSeq(18) -> Map("lbu" -> (idWires("f3_opcode") === opID("I")("load")("lbu").U)),
    ctrlSeq(19) -> Map("lhu" -> (idWires("f3_opcode") === opID("I")("load")("lhu").U)),
    ctrlSeq(20) -> Map("lwu" -> (idWires("f3_opcode") === opID("I")("load")("lwu").U)),
    ctrlSeq(21) -> Map("store" -> (io.opcode === opcodes("S")("store").U)),
    ctrlSeq(22) -> Map("sb" -> (idWires("f3_opcode") === opID("S")("store")("sb").U)),
    ctrlSeq(23) -> Map("sh" -> (idWires("f3_opcode") === opID("S")("store")("sh").U)),
    ctrlSeq(24) -> Map("sw" -> (idWires("f3_opcode") === opID("S")("store")("sw").U)),
    ctrlSeq(26) -> Map(
      "wrRmath" -> (io.opcode === opcodes("R")("math").U),
      "wrImath" -> (io.opcode === opcodes("I")("math").U),
      "wrLoad"  -> (io.opcode === opcodes("I")("load").U),
      "wrJalr"  -> boolWires("jalr"),
      "wrAuipc" -> (io.opcode === opcodes("U")("auipc").U),
      "wrLui"   -> (io.opcode === opcodes("U")("lui").U),
      "wrJal"   -> boolWires("jal")
      )
    ).map(x => x._1 -> x._2.values.reduce((x, y) => x || y))

  // Connections
  //for (i <- 0 until )
}
