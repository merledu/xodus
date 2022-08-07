package Fetch

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class Fetch_IO extends Bundle
{
    // Input pins
    val nPC_in        : UInt = Input(UInt(32.W))
    val nPC_en        : Bool = Input(Bool())
    val forward_inst  : Bool = Input(Bool())
    val StallUnit_inst: UInt = Input(UInt(32.W))
    val stallPC       : UInt = Input(UInt(32.W))
    val forward_PC    : Bool = Input(Bool())
    val StallUnit_PC  : UInt = Input(UInt(32.W))

    // Output pins
    val PC_out  : UInt = Output(UInt(32.W))
    val inst_out: UInt = Output(UInt(32.W))
    val PC4     : UInt = Output(UInt(32.W))
}
class Fetch extends Module
{
    // Initializing IO pins
    val io            : Fetch_IO = IO(new Fetch_IO)
    val nPC_in        : UInt     = dontTouch(WireInit(io.nPC_in))
    val nPC_en        : Bool     = dontTouch(WireInit(io.nPC_en))
    val forward_inst  : Bool     = dontTouch(WireInit(io.forward_inst))
    val StallUnit_inst: UInt     = dontTouch(WireInit(io.StallUnit_inst))
    val stallPC       : UInt     = dontTouch(WireInit(io.stallPC))
    val forward_PC    : Bool     = dontTouch(WireInit(io.forward_PC))
    val StallUnit_PC  : UInt     = dontTouch(WireInit(io.StallUnit_PC))

    // Program counter
    val PC: UInt = dontTouch(RegInit(0.U(32.W)))

    // Instruction memory
    val inst_mem: Mem[UInt] = Mem(16777216, UInt(32.W))

    // Loading instructions from memory
    loadMemoryFromFile(inst_mem, "assembly/hex.txt")

    // Intermediate wires
    val PC4     : UInt = dontTouch(WireInit(PC + 4.U))
    val PC_out  : UInt = dontTouch(WireInit(PC))
    val inst_out: UInt = dontTouch(WireInit(inst_mem.read(PC(25, 2))))
    val nPC     : UInt = dontTouch(WireInit(MuxCase(PC4, Array(
        nPC_en     -> nPC_in,
        forward_PC -> StallUnit_PC
    ))))

    // Wiring to output pins
    Array(
        io.PC4, PC
    ) zip Array(
        PC4,    nPC
    ) foreach
    {
        x => x._1 := x._2
    }
    Array(
        io.PC_out,              io.inst_out
    ) zip Array(
        Array(stallPC, PC_out), Array(StallUnit_inst, inst_out)
    ) foreach
    {
        x => x._1 := Mux(forward_inst, x._2(0), x._2(1))
    }
}

