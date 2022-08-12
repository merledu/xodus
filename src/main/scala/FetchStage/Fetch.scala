package FetchStage

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile

class Fetch_IO extends Bundle
{
    // Input pins
    val forward_inst  : Bool = Input(Bool())
    val StallUnit_inst: UInt = Input(UInt(32.W))
    val stallPC       : UInt = Input(UInt(32.W))
    val forward_PC    : Bool = Input(Bool())
    val StallUnit_PC  : UInt = Input(UInt(32.W))
    val br_en         : Bool = Input(Bool())
    val jal_en        : Bool = Input(Bool())
    val imm           : SInt = Input(SInt(32.W))
    val RegFD_PC      : UInt = Input(UInt(32.W))
    val jalr_en       : Bool = Input(Bool())
    val jalr_PC       : UInt = Input(UInt(32.W))

    // Output pins
    val PC_out  : UInt = Output(UInt(32.W))
    val inst_out: UInt = Output(UInt(32.W))
    val PC4     : UInt = Output(UInt(32.W))
}
class Fetch extends Module
{
    // Initializing IO pins
    val io            : Fetch_IO = IO(new Fetch_IO)
    val forward_inst  : Bool     = dontTouch(WireInit(io.forward_inst))
    val StallUnit_inst: UInt     = dontTouch(WireInit(io.StallUnit_inst))
    val stallPC       : UInt     = dontTouch(WireInit(io.stallPC))
    val forward_PC    : Bool     = dontTouch(WireInit(io.forward_PC))
    val StallUnit_PC  : UInt     = dontTouch(WireInit(io.StallUnit_PC))
    val br_en         : Bool     = dontTouch(WireInit(io.br_en))
    val jal_en        : Bool     = dontTouch(WireInit(io.jal_en))
    val imm           : SInt     = dontTouch(WireInit(io.imm))
    val RegFD_PC      : UInt     = dontTouch(WireInit(io.RegFD_PC))
    val jalr_en       : Bool     = dontTouch(WireInit(io.jalr_en))
    val jalr_PC       : UInt     = dontTouch(WireInit(io.jalr_PC))

    // Program counter
    val PC: UInt = dontTouch(RegInit(0.U(32.W)))

    // Instruction memory
    val inst_mem: Mem[UInt] = Mem(16777216, UInt(32.W))

    // Loading instructions from memory
    loadMemoryFromFile(inst_mem, "assembly/hex.txt")

    // Intermediate wires
    val PC_out   : UInt = dontTouch(WireInit(Mux(br_en || jal_en || jalr_en, 0.U, PC)))
    val inst_num : UInt = dontTouch(WireInit(PC_out(25, 2)))
    val PC4      : UInt = dontTouch(WireInit(PC_out + 4.U))
    val br_jal_PC: UInt = dontTouch(WireInit(RegFD_PC + imm.asUInt))
    val inst_out : UInt = dontTouch(WireInit(Mux(br_en || jal_en || jalr_en, 0.U, inst_mem.read(inst_num))))
    val nPC      : UInt = dontTouch(WireInit(MuxCase(PC4, Seq(
        forward_PC        -> StallUnit_PC,
        (br_en || jal_en) -> br_jal_PC,
        jalr_en           -> jalr_PC
    ))))

    // Wiring to output pins
    Seq(
        io.PC4, PC
    ) zip Seq(
        PC4,    nPC
    ) foreach
    {
        x => x._1 := x._2
    }
    Seq(
        io.PC_out,              io.inst_out
    ) zip Seq(
        (PC_out, stallPC), (inst_out, StallUnit_inst)
    ) foreach
    {
        x => x._1 := Mux(forward_inst, x._2._2, x._2._1)
    }
}

