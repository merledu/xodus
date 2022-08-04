package Decoder

import chisel3._

class U_Type_IO extends Bundle
{
    // Input pins
    val in    : UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val rd_addr: UInt = Output(UInt(5.W))
    val imm    : SInt = Output(SInt(32.W))
}
class U_Type extends Module
{
    // Initializing IO pins
    val io: U_Type_IO = IO(new U_Type_IO)

    // Output is thrown when opcode matches
    when (io.opcode === 23.U | io.opcode === 55.U)
    {
        Array(
            io.rd_addr,  io.imm
        ) zip Array(
            io.in(4, 0), io.in(24, 5).asSInt
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
