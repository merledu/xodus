package PipelineRegs

import chisel3._

class RegFD_IO extends Bundle
{
    // Input pins
    val PC_in  : UInt = Input(UInt(32.W))
    val inst_in: UInt = Input(UInt(32.W))
    val PC4_in : UInt = Input(UInt(32.W))

    // Output pins
    val PC_out  : UInt = Output(UInt(32.W))
    val inst_out: UInt = Output(UInt(32.W))
    val PC4_out : UInt = Output(UInt(32.W))
}
class RegFD extends Module
{
    // Initializing IO pins
    val io     : RegFD_IO = IO(new RegFD_IO)
    val PC_in  : UInt = dontTouch(WireInit(io.PC_in))
    val inst_in: UInt = dontTouch(WireInit(io.inst_in))
    val PC4_in : UInt = dontTouch(WireInit(io.PC4_in))

    // Initializing registers
    val PC  : UInt = dontTouch(RegInit(0.U(32.W)))
    val inst: UInt = dontTouch(RegInit(0.U(32.W)))
    val PC4 : UInt = dontTouch(RegInit(0.U(32.W)))

    // Intermediate wires
    val PC_out  : UInt = dontTouch(WireInit(PC))
    val inst_out: UInt = dontTouch(WireInit(inst))
    val PC4_out : UInt = dontTouch(WireInit(PC4))

    // Wiring to output pins
    Array(
        // Output pins
        io.PC_out, io.inst_out, io.PC4_out,

        // Registers
        PC,        inst,        PC4
    ) zip Array(
        // Output pins
        PC_out,    inst_out,    PC4_out,

        // Registers
        PC_in,     inst_in,     PC4_in
    ) foreach
    {
        x => x._1 := x._2
    }
}
