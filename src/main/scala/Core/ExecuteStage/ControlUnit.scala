package ExecuteStage

import chisel3._
import chisel3.util._


class ControlUnitIO(
  params  :Map[String, Int],
  ctrlNum :Int
) extends Bundle {
  // Input pins
  val opcode: UInt      = Input(UInt(params("opcodeLen").W))
  val func3 : UInt      = Input(UInt(params("f3Len").W))
  val func7 : UInt      = Input(UInt(params("f7Len").W))
  val imm   : SInt      = Input(SInt(params("XLEN").W))
  val enVec : Vec[Bool] = Input(Vec(2, Bool()))

  // Output pins
  val en: Vec[Bool] = Output(Vec(ctrlNum, Bool()))
}


class ControlUnit(
  params  :Map[String, Int],
  opcodes :Map[String, Map[String, Int]],
  opID    :Map[String, Map[String, Map[String, Int]]],
  conf    :Map[String, Seq[String]],
  debug   :Boolean
) extends Module {
  val aluNum    : Int = conf("alu").length
  val dataMemNum: Int = conf("dataMem").length
  val ctrlNum   : Int = aluNum + dataMemNum
  val io: ControlUnitIO = IO(new ControlUnitIO(
    params  = params,
    ctrlNum = ctrlNum+1  // +1 for wrEn
  ))

  // Wires
  val boolWires: Map[String, Bool] = Map(
    "stallEn" -> io.enVec(0),
    "jump"    -> io.enVec(1)
  )

  val idWires: Map[String, UInt] = Map(
    "f7_f3_opcode"  -> Cat(io.func7, io.func3, io.opcode),
    "f3_opcode"     -> Cat(io.func3, io.opcode),
    "imm_f3_opcode" -> Cat(io.imm(11, 5), io.func3, io.opcode)
  )

  val enWires: Map[String, Bool] = (
    for {
      (k1, v1) <- Map(
        "alu" -> Map(
          0 -> Map(
            "add"   -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "addi"  -> (idWires("f3_opcode"), opID("I")("math")),
            "load"  -> (io.opcode, opcodes("I")),
            "store" -> (io.opcode, opcodes("S"))
          ),
          1 -> Map(
            "sub" -> (idWires("f7_f3_opcode"), opID("R")("math"))
          ),
          2 -> Map(
            "sll" -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "slli" -> (idWires("imm_f3_opcode"), opID("I")("math"))
          ),
          3 -> Map(
            "slt"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "slti" -> (idWires("f3_opcode"), opID("I")("math"))
          ),
          4 -> Map(
            "sltu" -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "sltiu" -> (idWires("f3_opcode"), opID("I")("math"))
          ),
          5 -> Map(
            "xor"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "xori" -> (idWires("f3_opcode"), opID("I")("math"))
          ),
          6 -> Map(
            "srl"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "srli" -> (idWires("imm_f3_opcode"), opID("I")("math"))
          ),
          7 -> Map(
            "sra"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "srai" -> (idWires("imm_f3_opcode"), opID("I")("math"))
          ),
          8 -> Map(
            "or"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "ori" -> (idWires("f3_opcode"), opID("I")("math"))
          ),
          9 -> Map(
            "and"  -> (idWires("f7_f3_opcode"), opID("R")("math")),
            "andi" -> (idWires("f3_opcode"), opID("I")("math"))
          ),
          10 -> Map("auipc" -> (io.opcode, opcodes("U"))),
          11 -> Map("lui"   -> (io.opcode, opcodes("U"))),
          12 -> Map(
            "math"  -> (io.opcode, opcodes("I")),
            "load"  -> (io.opcode, opcodes("I")),
            "store" -> (io.opcode, opcodes("S")),
            "auipc" -> (io.opcode, opcodes("U")),
            "lui"   -> (io.opcode, opcodes("U"))
          )
        ),
        "dataMem" -> Map(
          0 -> Map("load"  -> (io.opcode, opcodes("I"))),
          1 -> Map("lb"    -> (idWires("f3_opcode"), opID("I")("load"))),
          2 -> Map("lh"    -> (idWires("f3_opcode"), opID("I")("load"))),
          3 -> Map("lw"    -> (idWires("f3_opcode"), opID("I")("load"))),
          4 -> Map("lbu"   -> (idWires("f3_opcode"), opID("I")("load"))),
          5 -> Map("lhu"   -> (idWires("f3_opcode"), opID("I")("load"))),
          6 -> Map("store" -> (io.opcode, opcodes("S"))),
          7 -> Map("sb"    -> (idWires("f3_opcode"), opID("S")("store"))),
          8 -> Map("sh"    -> (idWires("f3_opcode"), opID("S")("store"))),
          9 -> Map("sw"    -> (idWires("f3_opcode"), opID("S")("store")))
        )
      )
      (k2, v2) <- v1
    } yield conf(k1)(k2) -> (
      v2.map(
        x => x._1 -> (x._2._1 === x._2._2(x._1).U)
      ).values.reduce(
        (y, z) => y || z
      )
    )
  ).toMap

  // TODO: Remove commented block if core works after debug
  //val enWires: Map[String, Bool] = (
  //  for (i <- 0 until ctrlNum)
  //    yield conf(i) -> cmpWires(conf(i)).values.reduce(
  //      (x, y) => x || y
  //    )
  //).toMap

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
      for (i <- 0 until aluNum - 1)
        yield (io.en(i), enWires(conf("alu")(i)))
    ) ++ (
      for (i <- 0 until dataMemNum)
        yield (io.en(i + aluNum), enWires(conf("dataMem")(i)))
    ) ++ Seq(
      (io.en(aluNum - 1), boolWires("jump"))
    ) ++ Seq(
      (io.en(ctrlNum), wrEn)
    )
  ).map(
    x => x._1 := Mux(boolWires("stallEn"), 0.B, x._2)
  )
}
