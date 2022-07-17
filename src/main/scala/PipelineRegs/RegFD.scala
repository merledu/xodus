package PipelineRegs

import chisel3._

class RegFD_IO extends Bundle
{
    // Input pins
    val PC_in  : UInt = Input(UInt(32.W))
    val inst_in: UInt = Input(UInt(32.W))

    // Output pins
    val PC_out  : UInt = Output(UInt(32.W))
    val inst_out: UInt = Output(UInt(32.W))
}
class RegFD extends Module
{
    // Initializing IO pins
    val io: RegFD_IO = IO(new RegFD_IO())

    // Input wires
    val PC_in  : UInt = dontTouch(WireInit(io.PC_in))
    val inst_in: UInt = dontTouch(WireInit(io.inst_in))

    // Initialzing registers
    val PC  :UInt = dontTouch(RegInit(0.U(32.W)))
    val inst:UInt = dontTouch(RegInit(0.U(32.W)))

    // Output wires
    val PC_out  : UInt = dontTouch(WireInit(PC))
    val inst_out: UInt = dontTouch(WireInit(inst))

    // Wiring to output pins
    Array(
        // Output pins
        io.PC_out, io.inst_out,

        // Registers
        PC,        inst
    ) zip Array(
        // Output pins
        PC_out,    inst_out,

        // Registers
        PC_in,     inst_in
    ) foreach
    {
        x => x._1 := x._2
    }
}
