package Decoder

import chisel3._
import chisel3.util._

class J_Type_IO extends Bundle
{
    // Input pins
    val in    : UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val rd_addr: UInt = Output(UInt(5.W))
    val imm    : SInt = Output(SInt(32.W))
}
class J_Type extends Module
{
    // Initializing IO pins
    val io: J_Type_IO = IO(new J_Type_IO)

    // Output is thrown when opcode matches
    when (io.opcode === 111.U)
    {
        Array(
            io.rd_addr,  io.imm
        ) zip Array(
            io.in(4, 0), Cat(io.in(24), io.in(12, 5), io.in(13), io.in(23, 14), "b0".U).asSInt
        ) foreach
        {
            x => x._1 := x._2
        }
    }.otherwise
    {
        io.rd_addr := 0.U
        io.imm     := 0.S
    }
}
