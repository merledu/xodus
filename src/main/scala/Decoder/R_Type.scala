package Decoder

import chisel3._

class R_Type_IO extends Bundle
{
    // Input pins
    val in    : UInt = Input(UInt(25.W))
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val rd_addr : UInt = Output(UInt(5.W))
    val func3   : UInt = Output(UInt(3.W))
    val rs1_addr: UInt = Output(UInt(5.W))
    val rs2_addr: UInt = Output(UInt(5.W))
    val func7   : UInt = Output(UInt(7.W))
}
class R_Type extends Module
{
    // Initializing IO pins
    val io    : R_Type_IO = IO(new R_Type_IO)
    val in    : UInt = dontTouch(WireInit(io.in))
    val opcode: UInt = dontTouch(WireInit(io.opcode))

    // Intermediate wires
    val rd_addr : UInt = dontTouch(WireInit(in(4, 0)))
    val func3   : UInt = dontTouch(WireInit(in(7, 5)))
    val rs1_addr: UInt = dontTouch(WireInit(in(12, 8)))
    val rs2_addr: UInt = dontTouch(WireInit(in(17, 13)))
    val func7   : UInt = dontTouch(WireInit(in(24, 18)))

    // Output is thrown when opcode matches
    when (io.opcode === 51.U)
    {
        Array(
            io.rd_addr, io.func3, io.rs1_addr, io.rs2_addr, io.func7
        ) zip Array(
            rd_addr,    func3,    rs1_addr,    rs2_addr,    func7
        ) foreach
        {
            x => x._1 := x._2
        }
    }.otherwise
    {
        Array(
            io.rd_addr, io.func3, io.rs1_addr, io.rs2_addr, io.func7
        ) map ( _ := 0.U )
    }
}
