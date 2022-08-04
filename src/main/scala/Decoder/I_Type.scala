package Decoder

import chisel3._

class I_Type_IO extends Bundle
{
    // Input pins
    val in    : UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val rd_addr : UInt = Output(UInt(5.W))
    val func3   : UInt = Output(UInt(3.W))
    val rs1_addr: UInt = Output(UInt(5.W))
    val imm     : SInt = Output(SInt(32.W))
}
class I_Type extends Module
{
    // Initializing IO pins
    val io    : I_Type_IO = IO(new I_Type_IO)
    val in    : UInt = dontTouch(WireInit(io.in))
    val opcode: UInt = dontTouch(WireInit(io.opcode))

    val ld_id        : UInt = dontTouch(WireInit(3.U(7.W)))
    val fence_id     : UInt = dontTouch(WireInit(15.U(7.W)))
    val arithmetic_id: UInt = dontTouch(WireInit(19.U(7.W)))
    val jalr_id      : UInt = dontTouch(WireInit(103.U(7.W)))
    val call_id      : UInt = dontTouch(WireInit(115.U(7.W)))

    // Intermediate wires
    val rd_addr : UInt = dontTouch(WireInit(in(4, 0)))
    val func3   : UInt = dontTouch(WireInit(in(7, 5)))
    val rs1_addr: UInt = dontTouch(WireInit(in(12, 8)))
    val imm     : SInt = dontTouch(WireInit(in(24, 13).asSInt))

    // Output is thrown when opcode mtches
    when (opcode === ld_id || opcode === fence_id || opcode === arithmetic_id || opcode === jalr_id || opcode === call_id)
    {
        Array(
            io.rd_addr, io.func3, io.rs1_addr, io.imm
        ) zip Array(
            rd_addr,    func3,    rs1_addr,    imm
        ) foreach
        {
            x => x._1 := x._2
        }
    }.otherwise
    {
        Array(
            io.rd_addr, io.func3, io.rs1_addr
        ) map ( _ := 0.U )
        io.imm    := 0.S
    }
}
