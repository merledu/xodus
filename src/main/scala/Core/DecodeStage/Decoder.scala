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

class Decoder(PARAMS:Map[String, Int], OPCODES:Map[String, Map[String, UInt]], DEBUG:Boolean=False) extends Module {
    // Initializing IO ports
    val io: DecoderIO = IO(new DecoderIO(PARAMS))

    // Connections
    Seq(
        (io.opcode, io.inst(6, 0)),
        (io.imm,    MuxCase(0.S, Seq(
            opcode === OPCODES("I")("math")  || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") -> io.inst(31, 20).asSInt,
            opcode === OPCODES("S")("S") -> Cat(io.inst(31, 25), io.inst(11, 7)).asSInt,
            opcode === OPCODES("B")("B") -> Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), "b0".U).asSInt,
            opcode === OPCODES("U")("auipc") || opcode === OPCODES("U")("lui") -> io.inst(31, 12).asSInt,
            opcode === OPCODES("J")("J") -> Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), "b0".U).asSInt
        )))
    ).map(x => x._1 := x._2)

    Seq(
        (io.rAddr(0), opcode === OPCODES("R")("math") || opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") || opcode === OPCODES("U")("auipc") || opcode === OPCODES("U")("lui") || opcode === OPCODES("J")("J"), io.inst(11, 7)),
        (io.func3,    opcode === OPCODES("R")("math") || opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") || opcode === OPCODES("S")("S")     || opcode === OPCODES("B")("B"), io.inst(14, 12)),
        (io.rAddr(1), opcode === OPCODES("R")("math") || opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") || opcode === OPCODES("S")("S")     || opcode === OPCODES("B")("B"), io.inst(19, 15)),
        (io.rAddr(2), opcode === OPCODES("R")("math") || opcode === OPCODES("S")("S")    || opcode === OPCODES("B")("B"), io.inst(24, 20)),
        (io.func7,    opcode === OPCODES("R")("math"), io.inst(31, 25), 0.U)
    ).map(x => x._1 := Mux(x._2, x._3, 0.U)


    // Debug Section
    if (DEBUG) {
        val opcode : UInt = dontTouch(WireInit(io.inst(6, 0)))
        val rdAddr : UInt = dontTouch(WireInit(Mux(
            opcode === OPCODES("R")("math") || opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") || opcode === OPCODES("U")("auipc") || opcode === OPCODES("U")("lui") || opcode === OPCODES("J")("J"),
            io.inst(11, 7),
            0.U
        )))
        val func3  : UInt = dontTouch(WireInit(Mux(
            opcode === OPCODES("R")("math") || opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") || opcode === OPCODES("S")("S") || opcode === OPCODES("B")("B"),
            io.inst(14, 12),
            0.U
        )))
        val rs1Addr: UInt = dontTouch(WireInit(Mux(
            opcode === OPCODES("R")("math") || opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr") || opcode === OPCODES("S")("S") || opcode === OPCODES("B")("B"),
            io.inst(19, 15),
            0.U
        )))
        val rs2Addr: UInt = dontTouch(WireInit(Mux(
            opcode === OPCODES("R")("math") || opcode === OPCODES("S")("S") || opcode === OPCODES("B")("B"),
            io.inst(24, 20),
            0.U
        )))
        val func7  : UInt = dontTouch(WireInit(Mux(opcode === OPCODES("R")("math"), io.inst(31, 25), 0.U)))

        val immI: SInt = dontTouch(WireInit(Mux(opcode === OPCODES("I")("math") || opcode === OPCODES("I")("load") || opcode === OPCODES("I")("fence") || opcode === OPCODES("I")("jalr") || opcode === OPCODES("I")("csr"), io.inst(31, 20).asSInt, 0.S)))
        val immS: SInt = dontTouch(WireInit(Mux(opcode === OPCODES("S")("S"), Cat(io.inst(31, 25), io.inst(11, 7)).asSInt, 0.S)))
        val immB: SInt = dontTouch(WireInit(Mux(opcode === OPCODES("B")("B"), Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), "b0".U).asSInt, 0.S)))
        val immU: SInt = dontTouch(WireInit(Mux(opcode === OPCODES("U")("auipc") || opcode === OPCODES("U")("lui"), io.inst(31, 12).asSInt, 0.S)))
        val immJ: SInt = dontTouch(WireInit(Mux(opcode === OPCODES("J")("J"), Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), "b0".U).asSInt, 0.S)))
        val imm : SInt = dontTouch(WireInit(immI | immS | immB | immU | immJ))
    } else None
}
