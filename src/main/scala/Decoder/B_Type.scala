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
    val imm     : SInt = Output(SInt(12.W))
}
class B_Type extends Module
{
    // Initializing IO pins
    val io: B_Type_IO = IO(new B_Type_IO())

    // Input wires
    val in    : UInt = dontTouch(WireInit(io.in))
    val opcode: UInt = dontTouch(WireInit(io.opcode))

    // Output wires
    val func3   : UInt = dontTouch(WireInit(in(7, 5)))
    val rs1_addr: UInt = dontTouch(WireInit(in(12, 8)))
    val rs2_addr: UInt = dontTouch(WireInit(in(17, 13)))
    val imm     : SInt = dontTouch(WireInit(Cat(in(24), in(0), in(23, 18), in(4, 1), "b0".U).asSInt))

    // Output is thrown when opcode matches
    when (opcode === 99.U)
    {
        Array(
            io.func3, io.rs1_addr, io.rs2_addr, io.imm
        ) zip Array(
            func3,    rs1_addr,    rs2_addr,    imm
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
