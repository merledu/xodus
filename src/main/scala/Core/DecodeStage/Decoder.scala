package DecodeStage

import chisel3._
import chisel3.util._


class DecoderIO(params : Map[String, Int]) extends Bundle {
  // Input pins
  val inst: UInt = Input(UInt(params("XLEN").W))
  
  // Output pins
  val opcode: UInt      = Output(UInt(params("opcodeLen").W))
  val rAddr : Vec[UInt] = Output(Vec(params("numRegAddr"), UInt(params("regAddrLen").W)))
  val func3 : UInt      = Output(UInt(params("f3Len").W))
  val func7 : UInt      = Output(UInt(params("f7Len").W))
  val imm   : SInt      = Output(SInt(params("XLEN").W))
}

class Decoder(
  params  :Map[String, Int],
  opcodes :Map[String, Map[String, Int]],
  immSeq  :Seq[String],
  debug   :Boolean
) extends Module {
  val io: DecoderIO = IO(new DecoderIO(params))

  // Wires
  val uintWires: Map[String, UInt] = Map(
    "opcode"  -> io.inst(6, 0),
    "rdAddr"  -> io.inst(11, 7),
    "func3"   -> io.inst(14, 12),
    "rs1Addr" -> io.inst(19, 15),
    "rs2Addr" -> io.inst(24, 20),
    "func7"   -> io.inst(31, 25),
    )

  val enWires: Map[String, Bool] = Map(
    "rdAddr" -> Map(
      "mathR" -> (uintWires("opcode") === opcodes("R")("math").U),
      "mathI" -> (uintWires("opcode") === opcodes("I")("math").U),
      "load"  -> (uintWires("opcode") === opcodes("I")("load").U),
      "fence" -> (uintWires("opcode") === opcodes("I")("fence").U),
      "jalr"  -> (uintWires("opcode") === opcodes("I")("jalr").U),
      "csr"   -> (uintWires("opcode") === opcodes("I")("csr").U),
      "auipc" -> (uintWires("opcode") === opcodes("U")("auipc").U),
      "lui"   -> (uintWires("opcode") === opcodes("U")("lui").U),
      "jal"   -> (uintWires("opcode") === opcodes("J")("jal").U)
      ),
    "func3" -> Map(
      "mathR"  -> (uintWires("opcode") === opcodes("R")("math").U),
      "mathI"  -> (uintWires("opcode") === opcodes("I")("math").U),
      "load"   -> (uintWires("opcode") === opcodes("I")("load").U),
      "fence"  -> (uintWires("opcode") === opcodes("I")("fence").U),
      "jalr"   -> (uintWires("opcode") === opcodes("I")("jalr").U),
      "csr"    -> (uintWires("opcode") === opcodes("I")("csr").U),
      "store"  -> (uintWires("opcode") === opcodes("S")("store").U),
      "branch" -> (uintWires("opcode") === opcodes("B")("branch").U)
      ),
    "rs1Addr" -> Map(
      "mathR"  -> (uintWires("opcode") === opcodes("R")("math").U),
      "mathI"  -> (uintWires("opcode") === opcodes("I")("math").U),
      "load"   -> (uintWires("opcode") === opcodes("I")("load").U),
      "fence"  -> (uintWires("opcode") === opcodes("I")("fence").U),
      "jalr"   -> (uintWires("opcode") === opcodes("I")("jalr").U),
      "csr"    -> (uintWires("opcode") === opcodes("I")("csr").U),
      "store"  -> (uintWires("opcode") === opcodes("S")("store").U),
      "branch" -> (uintWires("opcode") === opcodes("B")("branch").U)
      ),
    "rs2Addr" -> Map(
      "mathR" -> (uintWires("opcode") === opcodes("R")("math").U),
      "store"  -> (uintWires("opcode") === opcodes("S")("store").U),
      "branch" -> (uintWires("opcode") === opcodes("B")("branch").U)
      ),
    "func7" -> Map("mathR" -> (uintWires("opcode") === opcodes("R")("math").U)),
    immSeq(0) -> Map(
      "mathI" -> (uintWires("opcode") === opcodes("I")("math").U),
      "load"  -> (uintWires("opcode") === opcodes("I")("load").U),
      "fence" -> (uintWires("opcode") === opcodes("I")("fence").U),
      "jalr"  -> (uintWires("opcode") === opcodes("I")("jalr").U),
      "csr"   -> (uintWires("opcode") === opcodes("I")("csr").U)
      ),
    immSeq(1) -> Map("store" -> (uintWires("opcode") === opcodes("S")("store").U)),
    immSeq(2) -> Map("branch" -> (uintWires("opcode") === opcodes("B")("branch").U)),
    immSeq(3) -> Map(
      "auipc" -> (uintWires("opcode") === opcodes("U")("auipc").U),
      "lui"   -> (uintWires("opcode") === opcodes("U")("lui").U)
      ),
    immSeq(4) -> Map("jal" -> (uintWires("opcode") === opcodes("J")("jal").U))
    ).map(x => x._1 -> x._2.values.reduce((x, y) => x || y))

  val immGen: Map[String, SInt] = Map(
    immSeq(0) -> io.inst(31, 20).asSInt,
    immSeq(1) -> Cat(io.inst(31, 25), io.inst(11, 7)).asSInt,
    immSeq(2) -> Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), "b0".U).asSInt,
    immSeq(3) -> io.inst(31, 12).asSInt,
    immSeq(4) -> Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), "b0".U).asSInt
    )

  // TODO: Remove commented imm block if code works without it
  //val imm: SInt = MuxCase(0.S, Seq(
  //  enWires("immI") -> immGen("immI"),
  //  enWires("immS") -> immGen("immS"),
  //  enWires("immB") -> immGen("immB"),
  //  enWires("immU") -> immGen("immU"),
  //  enWires("immJ") -> immGen("immJ")
  //  ))

  val imm: SInt = MuxCase(0.S, for (item <- immSeq) yield enWires(item) -> immGen(item))

  // Connections
  Seq(
    (io.opcode, uintWires("opcode")),
    (io.imm,    imm)
    ).map(x => x._1 := x._2)

  Seq(
    (io.rAddr(0), enWires("rdAddr"),  uintWires("rdAddr")),
    (io.func3,    enWires("func3"),   uintWires("func3")),
    (io.rAddr(1), enWires("rs1Addr"), uintWires("rs1Addr")),
    (io.rAddr(2), enWires("rs2Addr"), uintWires("rs2Addr")),
    (io.func7,    enWires("func7"),   uintWires("func7"))
    ).map(x => x._1 := Mux(x._2, x._3, 0.U))



  // Debug Section
  //if (debug) {
  //  val debug_uintWires_opcode : UInt = dontTouch(WireInit(uintWires("opcode")))
  //  val debug_uintWires_rdAddr : UInt = dontTouch(WireInit(uintWires("rdAddr")))
  //  val debug_uintWires_func3  : UInt = dontTouch(WireInit(uintWires("func3")))
  //  val debug_uintWires_rs1Addr: UInt = dontTouch(WireInit(uintWires("rs1Addr")))
  //  val debug_uintWires_rs2Addr: UInt = dontTouch(WireInit(uintWires("rs2Addr")))
  //  val debug_uintWires_func7  : UInt = dontTouch(WireInit(uintWires("func7")))

  //  val debug_enWires_rdAddr : Bool = dontTouch(WireInit(enWires("rdAddr")))
  //  val debug_enWires_func3  : Bool = dontTouch(WireInit(enWires("func3")))
  //  val debug_enWires_rs1Addr: Bool = dontTouch(WireInit(enWires("rs1Addr")))
  //  val debug_enWires_rs2Addr: Bool = dontTouch(WireInit(enWires("rs2Addr")))
  //  val debug_enWires_func7  : Bool = dontTouch(WireInit(enWires("func7")))
  //  val debug_enWires_immI   : Bool = dontTouch(WireInit(enWires("immI")))
  //  val debug_enWires_immS   : Bool = dontTouch(WireInit(enWires("immS")))
  //  val debug_enWires_immB   : Bool = dontTouch(WireInit(enWires("immB")))
  //  val debug_enWires_immU   : Bool = dontTouch(WireInit(enWires("immU")))
  //  val debug_enWires_immJ   : Bool = dontTouch(WireInit(enWires("immJ")))

  //  val debug_immGen_immI: SInt = dontTouch(WireInit(immGen("immI")))
  //  val debug_immGen_immS: SInt = dontTouch(WireInit(immGen("immS")))
  //  val debug_immGen_immB: SInt = dontTouch(WireInit(immGen("immB")))
  //  val debug_immGen_immU: SInt = dontTouch(WireInit(immGen("immU")))
  //  val debug_immGen_immJ: SInt = dontTouch(WireInit(immGen("immJ")))

  //  val debug_imm: SInt = dontTouch(WireInit(imm))
  //} else None
}
