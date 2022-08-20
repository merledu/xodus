package PipelineRegs

import chisel3._

class RegMW_IO extends Bundle
{
    // Input pins
    val alu_in     : SInt = Input(SInt(32.W))
    val mem_data_in: SInt = Input(SInt(32.W))
    val rd_addr_in : UInt = Input(UInt(5.W))
    val wr_en_in   : Bool = Input(Bool())
    val load_en_in : Bool = Input(Bool())

    // Output pins
    val alu_out     : SInt = Output(SInt(32.W))
    val mem_data_out: SInt = Output(SInt(32.W))
    val rd_addr_out : UInt = Output(UInt(5.W))
    val wr_en_out   : Bool = Output(Bool())
    val load_en_out : Bool = Output(Bool())
}
class RegMW extends Module
{
    // Initializing IO pins
    val io         : RegMW_IO = IO(new RegMW_IO())
    val alu_in     : SInt     = dontTouch(WireInit(io.alu_in))
    val mem_data_in: SInt     = dontTouch(WireInit(io.mem_data_in))
    val rd_addr_in : UInt     = dontTouch(WireInit(io.rd_addr_in))
    val wr_en_in   : Bool     = dontTouch(WireInit(io.wr_en_in))
    val load_en_in : Bool     = dontTouch(WireInit(io.load_en_in))

    // Initializing registers
    val alu     : SInt = dontTouch(RegInit(0.S(32.W)))
    val mem_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val rd_addr : UInt = dontTouch(RegInit(0.U(5.W)))
    val wr_en   : Bool = dontTouch(RegInit(0.B))
    val load_en : Bool = dontTouch(RegInit(0.B))

    // Intermediate wires
    val alu_out     : SInt = dontTouch(WireInit(alu))
    val mem_data_out: SInt = dontTouch(WireInit(mem_data))
    val rd_addr_out : UInt = dontTouch(WireInit(rd_addr))
    val wr_en_out   : Bool = dontTouch(WireInit(wr_en))
    val load_en_out : Bool = dontTouch(WireInit(load_en))

    // Wiring to output pins
    Array(
        // Output pins
        io.alu_out, io.mem_data_out, io.rd_addr_out, io.wr_en_out, io.load_en_out,

        // Registers
        alu, mem_data, rd_addr, wr_en, load_en
    ) zip Array(
        // Output pins
        alu_out, mem_data_out, rd_addr_out, wr_en_out, load_en_out,

        // Registers
        alu_in, mem_data_in, rd_addr_in, wr_en_in, load_en_in
    ) foreach
    {
        x => x._1 := x._2
    }
}

