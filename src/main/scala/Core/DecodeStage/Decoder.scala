package DecodeStage

import chisel3._
import chisel3.util._

class DecoderIO(PARAMS:Map[String, Int]) extends Bundle {
    // Input pins
    val inst: UInt = Input(UInt(PARAMS("XLEN").W))
    
    // Output pins
    val opcode: UInt      = Output(UInt(PARAMS("OPCODELEN").W))
    val rAddr : Vec[UInt] = Output(Vec(3, UInt(PARAMS("REGADDRLEN").W)))
    val func3 : UInt      = Output(UInt(PARAMS("F3LEN").W))
    val func7 : UInt      = Output(UInt(PARAMS("F7LEN").W))
    val imm   : SInt      = Output(SInt(PARAMS("XLEN").W))
}

class Decoder(PARAMS:Map[String, Int], OPCODES:Map[String, Map[String, UInt]], DEBUG:Boolean) extends Module {
    // Initializing IO ports
    val io: DecoderIO = IO(new DecoderIO(PARAMS))

    // Wires
    val uintWires: Map[String, UInt] = Map(
        "opcode"  -> io.inst(6, 0),
        "rdAddr"  -> io.inst(11, 7),
        "func3"   -> io.inst(14, 12),
        "rs1Addr" -> io.inst(19, 15),
        "rs2Addr" -> io.inst(24, 20),
        "func7"   -> io.inst(31, 25),
    )
    val enWires  : Map[String, Bool] = Map(
        "rdAddr"  -> (uintWires("opcode") === OPCODES("R")("math") || uintWires("opcode") === OPCODES("I")("math") || uintWires("opcode") === OPCODES("I")("load") || uintWires("opcode") === OPCODES("I")("fence") || uintWires("opcode") === OPCODES("I")("jalr") || uintWires("opcode") === OPCODES("I")("csr") || uintWires("opcode") === OPCODES("U")("auipc") || uintWires("opcode") === OPCODES("U")("lui") || uintWires("opcode") === OPCODES("J")("J")),
        "func3"   -> (uintWires("opcode") === OPCODES("R")("math") || uintWires("opcode") === OPCODES("I")("math") || uintWires("opcode") === OPCODES("I")("load") || uintWires("opcode") === OPCODES("I")("fence") || uintWires("opcode") === OPCODES("I")("jalr") || uintWires("opcode") === OPCODES("I")("csr") || uintWires("opcode") === OPCODES("S")("S")     || uintWires("opcode") === OPCODES("B")("B")),
        "rs1Addr" -> (uintWires("opcode") === OPCODES("R")("math") || uintWires("opcode") === OPCODES("I")("math") || uintWires("opcode") === OPCODES("I")("load") || uintWires("opcode") === OPCODES("I")("fence") || uintWires("opcode") === OPCODES("I")("jalr") || uintWires("opcode") === OPCODES("I")("csr") || uintWires("opcode") === OPCODES("S")("S")     || uintWires("opcode") === OPCODES("B")("B")),
        "rs2Addr" -> (uintWires("opcode") === OPCODES("R")("math") || uintWires("opcode") === OPCODES("S")("S")    || uintWires("opcode") === OPCODES("B")("B")),
        "func7"   -> (uintWires("opcode") === OPCODES("R")("math")),
        "immI"    -> (uintWires("opcode") === OPCODES("I")("math")  || uintWires("opcode") === OPCODES("I")("load") || uintWires("opcode") === OPCODES("I")("fence") || uintWires("opcode") === OPCODES("I")("jalr") || uintWires("opcode") === OPCODES("I")("csr")),
        "immS"    -> (uintWires("opcode") === OPCODES("S")("S")),
        "immB"    -> (uintWires("opcode") === OPCODES("B")("B")),
        "immU"    -> (uintWires("opcode") === OPCODES("U")("auipc") || uintWires("opcode") === OPCODES("U")("lui")),
        "immJ"    -> (uintWires("opcode") === OPCODES("J")("J"))
    )
    val immGen   : Map[String, SInt] = Map(
        "immI" -> io.inst(31, 20).asSInt,
        "immS" -> Cat(io.inst(31, 25), io.inst(11, 7)).asSInt,
        "immB" -> Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), "b0".U).asSInt,
        "immU" -> io.inst(31, 12).asSInt,
        "immJ" -> Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), "b0".U).asSInt
    )
    val imm      : SInt = MuxCase(0.S, Seq(
        enWires("immI") -> immGen("immI"),
        enWires("immS") -> immGen("immS"),
        enWires("immB") -> immGen("immB"),
        enWires("immU") -> immGen("immU"),
        enWires("immJ") -> immGen("immJ")
    ))

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
    if (DEBUG) {
        val debug_uintWires_opcode : UInt = dontTouch(WireInit(uintWires("opcode")))
        val debug_uintWires_rdAddr : UInt = dontTouch(WireInit(uintWires("rdAddr")))
        val debug_uintWires_func3  : UInt = dontTouch(WireInit(uintWires("func3")))
        val debug_uintWires_rs1Addr: UInt = dontTouch(WireInit(uintWires("rs1Addr")))
        val debug_uintWires_rs2Addr: UInt = dontTouch(WireInit(uintWires("rs2Addr")))
        val debug_uintWires_func7  : UInt = dontTouch(WireInit(uintWires("func7")))

        val debug_enWires_rdAddr : Bool = dontTouch(WireInit(enWires("rdAddr")))
        val debug_enWires_func3  : Bool = dontTouch(WireInit(enWires("func3")))
        val debug_enWires_rs1Addr: Bool = dontTouch(WireInit(enWires("rs1Addr")))
        val debug_enWires_rs2Addr: Bool = dontTouch(WireInit(enWires("rs2Addr")))
        val debug_enWires_func7  : Bool = dontTouch(WireInit(enWires("func7")))
        val debug_enWires_immI   : Bool = dontTouch(WireInit(enWires("immI")))
        val debug_enWires_immS   : Bool = dontTouch(WireInit(enWires("immS")))
        val debug_enWires_immB   : Bool = dontTouch(WireInit(enWires("immB")))
        val debug_enWires_immU   : Bool = dontTouch(WireInit(enWires("immU")))
        val debug_enWires_immJ   : Bool = dontTouch(WireInit(enWires("immJ")))

        val debug_immGen_immI: SInt = dontTouch(WireInit(immGen("immI")))
        val debug_immGen_immS: SInt = dontTouch(WireInit(immGen("immS")))
        val debug_immGen_immB: SInt = dontTouch(WireInit(immGen("immB")))
        val debug_immGen_immU: SInt = dontTouch(WireInit(immGen("immU")))
        val debug_immGen_immJ: SInt = dontTouch(WireInit(immGen("immJ")))
        val debug_imm        : SInt = dontTouch(WireInit(imm))
    } else None
}
