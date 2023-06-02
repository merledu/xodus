package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DecoderIO


class Enables extends Bundle {
  val alu: Vec[Bool] = Output(Vec(new ControlUnit().aluEn.length, Bool()))
}


class ControlUnitIO extends Bundle with Configs {
  val opcode     : UInt = Flipped(new DecoderIO().opcode)
  val funct3     : UInt = Flipped(new DecoderIO().funct3)
  val funct7_imm7: UInt = Flipped(new DecoderIO().funct7_imm7)

  //val en: Enables = new Enables
}


class ControlUnit extends RawModule with Configs {
  val io: ControlUnitIO = IO(new ControlUnitIO)

  val aluEn: Seq[String] = Seq(
    "s+", "s<", "u<",  "&",   "|",
    "^",  "<<", ">>",  ">>>", "lui",
    "u+", "-",  "imm", "auipc"
  )

  //// Wires
  //val idWires: Map[String, UInt] = Map(
  //  "opcode"                          -> io.opcode,
  //  "funct3_opcode"                   -> Cat(io.funct3, io.opcode),
  //  "funct7/imm(11, 5)_funct3_opcode" -> Cat(io.funct7_imm7, io.funct3, io.opcode),
  //)

  //val instEn: Map[String, Bool] = Seq(
  //  "funct3_opcode"                   -> (0, 21),
  //  "funct7/imm(11, 5)_funct3_opcode" -> (21, 34),
  //  "opcode"                          -> (34, isa("insts").size)
  //).map(
  //  x => isa("insts").slice(x._2._1, x._2._2).map(
  //    y => y._1 -> (y._2.values.map(
  //      z => z.U
  //    ).reduce(
  //      (a, b) => Cat(a, b)
  //    ) === idWires(x._1))
  //  )
  //).reduce(
  //  (x, y) => x ++ y
  //)

  //val enWires: Map[String, Bool] = (Seq(
  //  Seq(  // s+
  //    "addi", "lb", "lh", "lw", "lbu",
  //    "lhu",  "sb", "sh", "sw", "add"
  //  ),
  //  Seq("slti", "slt"),           // s<
  //  Seq("sltiu", "sltu"),         // u<
  //  Seq("andi", "and"),           // &
  //  Seq("ori", "or"),             // |
  //  Seq("xori", "xor"),           // ^
  //  Seq("slli", "sll"),           // <<
  //  Seq("srli", "srl"),           // >>
  //  Seq("srai", "sra"),           // >>>
  //  Seq("lui"),                   // lui
  //  Seq("auipc", "jalr", "jal"),  // u+
  //  Seq("sub")                    // -
  //).map(
  //  x => x.map(
  //    y => instEn(y)
  //  ).reduce(
  //    (y, z) => y || z
  //  )
  //) ++ Seq(
  //  Seq(  // imm
  //    "I" -> Seq("load", "iArith"),
  //    "S" -> Seq("store"),
  //    "U" -> Seq("lui", "auipc")
  //  ),
  //  Seq("U" -> Seq("auipc"))  // auipc
  //).map(
  //  x => x.map(
  //    y => isa("opcodes")(y._1).map(
  //      z => io.opcode === isa("opcodes")(y._1)(z._1).U
  //    )
  //  ).reduce(
  //    (y, z) => y ++ z
  //  ).reduce(
  //    (y, z) => y || z
  //  )
  //)).zipWithIndex.map(
  //  x => arch("cuEn")(x._2) -> x._1
  //).toMap


  /********************
   * Interconnections *
   ********************/

  //for (i <- 0 until io.en.length) {
  //  io.en(i) := enWires.values.toSeq(i)
  //}
}
