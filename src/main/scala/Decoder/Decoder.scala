package Decoder

import chisel3._

class Decoder_IO extends Bundle
{
    // Input pins
    val inst: UInt = Input(UInt(32.W))

    // Output pins
    val rd_addr : UInt = Output(UInt(5.W))
    val func3   : UInt = Output(UInt(3.W))
    val rs1_addr: UInt = Output(UInt(5.W))
    val rs2_addr: UInt = Output(UInt(5.W))
    val func7   : UInt = Output(UInt(7.W))
    val imm     : SInt = Output(SInt(32.W))
    val opcode  : UInt = Output(UInt(7.W))
}
class Decoder extends Module
{
    // Initializing IO pins and modules
    val io  : Decoder_IO = IO(new Decoder_IO())
    val inst: UInt = dontTouch(WireInit(io.inst))

    // Type IDs
    val r_id: UInt = dontTouch(WireInit(51.U(7.W)))
    val i_math_id: UInt = dontTouch(WireInit(19.U(7.W)))
    val i_load_id: UInt = dontTouch(WireInit(3.U(7.W)))
    val i_fence_id: UInt = dontTouch(WireInit())

    // Intermediate wires
    val opcode: UInt = dontTouch(WireInit(inst(6, 0)))

    // Wiring to output pins
    Array(
        io.opcode, io.rd_addr, io.func3, io.rs1_addr, io.rs2_addr,
        io.func7,  io.imm
    ) zip Array(
        opcode,    rd_addr,    func3,    rs1_addr,    rs2_addr,
        func7,     imm
    ) foreach
    {
        x => x._1 := x._2
    }
}

