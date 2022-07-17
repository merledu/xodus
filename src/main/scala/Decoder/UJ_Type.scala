package Decoder

import chisel3._
import chisel3.util._

class UJ_Type_IO extends Bundle
{
    // Input pins
    val in: UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val rd_addr: UInt = Output(UInt(5.W))
    val imm: SInt = Output(SInt(32.W))
}
class UJ_Type extends Module
{
    // Initializing IO pins
    val io: UJ_Type_IO = IO(new UJ_Type_IO())

    // Input wires
    val in: UInt = dontTouch(WireInit(io.in))
    val opcode: UInt = dontTouch(WireInit(io.opcode))

    // Output wires
    val rd_addr: UInt = dontTouch(WireInit(in(4, 0)))
    val imm: SInt = dontTouch(WireInit(Cat(in(24), in(12, 5), in(13), in(23, 14), 0.U).asSInt))

    // Output is thrown when opcode matches
    when (opcode === 111.U)
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
        io.imm := 0.S
    }
}
