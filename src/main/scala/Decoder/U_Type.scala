package Decoder

import chisel3._

class U_Type_IO extends Bundle
{
    // Input pins
    val in    : UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val rd_addr: UInt = Output(UInt(5.W))
    val imm    : SInt = Output(SInt(20.W))
}
class U_Type extends Module
{
    // Initializing IO pins
    val io: U_Type_IO = IO(new U_Type_IO())

    // Input wires
    val in    : UInt = dontTouch(WireInit(io.in))
    val opcode: UInt = dontTouch(WireInit(io.opcode))

    // Output wires
    val rd_addr: UInt = dontTouch(WireInit(in(4, 0)))
    val imm    : SInt = dontTouch(WireInit(in(24, 5).asSInt))

    // Output is thrown when opcode matches
    when (opcode === 23.U | opcode === 55.U)
    {
        Array(
            io.rd_addr, io.imm
        ) zip Array(
            rd_addr,    imm
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
