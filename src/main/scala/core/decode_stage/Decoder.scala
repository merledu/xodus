package decode_stage

import chisel3._, chisel3.util._, configs._


class DecoderIO extends Bundle with Configs {
  // Input pins
  val inst: UInt = Input(UInt(xlen.W))
  
  // Output pins
  val opcode: UInt      = Output(UInt(opcodeLen.W))
  val rAddr : Vec[UInt] = Output(Vec(3, UInt(regAddrLen.W)))
  val func3 : UInt      = Output(UInt(f3len.W))
  val func7 : UInt      = Output(UInt(f7len.W))
  val imm   : SInt      = Output(SInt(xlen.W))
}


class Decoder extends Module with Configs{
  val io: DecoderIO = IO(new DecoderIO)

  // Wires
  val uintWires: Map[String, UInt] = Map(
    "opcode"  -> (6, 0),
    "rdAddr"  -> (11, 7),
    "func3"   -> (14, 12),
    "rs1Addr" -> (19, 15),
    "rs2Addr" -> (24, 20),
    "func7"   -> (31, 25)
  ).map(
    x => x._1 -> io.inst(x._2._1, x._2._2)
  )

  val enWires: Map[String, Bool] = Map(
    "rdAddr" -> Map(
      "mathR" -> ("R", "math"),
      "mathI" -> ("I", "math"),
      "load"  -> ("I", "load"),
      "fence" -> ("I", "fence"),
      "jalr"  -> ("I", "jalr"),
      "csr"   -> ("I", "csr"),
      "auipc" -> ("U", "auipc"),
      "lui"   -> ("U", "lui"),
      "jal"   -> ("J", "jal")
    ),
    "func3" -> Map(
      "mathR"  -> ("R", "math"),
      "mathI"  -> ("I", "math"),
      "load"   -> ("I", "load"),
      "fence"  -> ("I", "fence"),
      "jalr"   -> ("I", "jalr"),
      "csr"    -> ("I", "csr"),
      "store"  -> ("S", "store"),
      "branch" -> ("B", "branch")
    ),
    "rs1Addr" -> Map(
      "mathR"  -> ("R", "math"),
      "mathI"  -> ("I", "math"),
      "load"   -> ("I", "load"),
      "fence"  -> ("I", "fence"),
      "jalr"   -> ("I", "jalr"),
      "csr"    -> ("I", "csr"),
      "store"  -> ("S", "store"),
      "branch" -> ("B", "branch")
    ),
    "rs2Addr" -> Map(
      "mathR"  -> ("R", "math"),
      "store"  -> ("S", "store"),
      "branch" -> ("B", "branch")
    ),
    "func7" -> Map(
      "mathR" -> ("R", "math")
    ),
    immConf(0) -> Map(
      "mathI" -> ("I", "math"),
      "load"  -> ("I", "load"),
      "fence" -> ("I", "fence"),
      "jalr"  -> ("I", "jalr"),
      "csr"   -> ("I", "csr")
    ),
    immConf(1) -> Map(
      "store" -> ("S", "store")
    ),
    immConf(2) -> Map(
      "branch" -> ("B", "branch")
    ),
    immConf(3) -> Map(
      "auipc" -> ("U", "auipc"),
      "lui"   -> ("U", "lui")
    ),
    immConf(4) -> Map(
      "jal" -> ("J", "jal")
    )
  ).map(
    x => x._1 -> x._2.map(
      y => y._1 -> (opcodes(y._2._1)(y._2._2).U === uintWires("opcode"))
    ).values.reduce(
      (x, y) => x || y
    )
  )

  val immGen: Map[String, SInt] = Map(
    immConf(0) -> io.inst(31, 20).asSInt,
    immConf(1) -> Cat(io.inst(31, 25), io.inst(11, 7)).asSInt,
    immConf(2) -> Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), "b0".U).asSInt,
    immConf(3) -> io.inst(31, 12).asSInt,
    immConf(4) -> Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), "b0".U).asSInt
  )

  // TODO: Remove commented imm block if code works without it after debugging
  //val imm: SInt = MuxCase(0.S, Seq(
  //  enWires("immI") -> immGen("immI"),
  //  enWires("immS") -> immGen("immS"),
  //  enWires("immB") -> immGen("immB"),
  //  enWires("immU") -> immGen("immU"),
  //  enWires("immJ") -> immGen("immJ")
  //  ))

  // Connections
  Seq(
    (io.opcode, uintWires("opcode")),
    (io.imm,    MuxCase(
      0.S,
      for (immType <- immConf)
        yield enWires(immType) -> immGen(immType)
    ))
  ).map(x => x._1 := x._2)

  Seq(
    (io.rAddr(0), "rdAddr"),
    (io.func3,    "func3"),
    (io.rAddr(1), "rs1Addr"),
    (io.rAddr(2), "rs2Addr"),
    (io.func7,    "func7")
  ).map(x => x._1 := Mux(enWires(x._2), uintWires(x._2), 0.U))



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
