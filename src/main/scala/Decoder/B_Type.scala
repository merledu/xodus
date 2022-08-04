package Decoder

import chisel3._
import chisel3.util._

class B_Type_IO extends Bundle
{
    // Input pins
    val in    : UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val func3   : UInt = Output(UInt(3.W))
    val rs1_addr: UInt = Output(UInt(5.W))
    val rs2_addr: UInt = Output(UInt(5.W))
    val imm     : SInt = Output(SInt(32.W))
}
class B_Type extends Module
{
    // Initializing IO pins
    val io    : B_Type_IO = IO(new B_Type_IO)
    val in    : UInt = dontTouch(WireInit(io.in))
    val opcode: UInt = dontTouch(WireInit(io.opcode))

    val b_id
    val func3
    val rs1_addr

    // Output is thrown when opcode matches
    when (io.opcode === 99.U)
    {
        Array(
            io.func3,    io.rs1_addr,  io.rs2_addr,   io.imm
        ) zip Array(
            io.in(7, 5), io.in(12, 8), io.in(17, 13), Cat(io.in(24), io.in(0), io.in(23, 18), io.in(4, 1), "b0".U).asSInt
        ) foreach
        {
            x => x._1 := x._2
        }
    }.otherwise
    {
        Array(
            io.func3, io.rs1_addr, io.rs2_addr
        ) map ( _ := 0.U )
        io.imm    := 0.S
    }
}
