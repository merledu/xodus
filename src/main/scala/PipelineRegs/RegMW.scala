package PipelineRegs

import chisel3._

class RegMW_IO extends Bundle
{
    // Input pins
    val PC_in      : UInt = Input(UInt(32.W))
    val PC4_in     : UInt = Input(UInt(32.W))
    val alu_in     : SInt = Input(SInt(32.W))
    val mem_data_in: SInt = Input(SInt(32.W))
    val rd_addr_in : UInt = Input(UInt(5.W))
    val imm_in     : SInt = Input(SInt(32.W))
    val wr_en_in   : Bool = Input(Bool())
    val br_en_in   : Bool = Input(Bool())
    val jalr_en_in : Bool = Input(Bool())
    val jal_en_in  : Bool = Input(Bool())
    val auipc_en_in: Bool = Input(Bool())
    val lui_en_in  : Bool = Input(Bool())
    val load_en_in : Bool = Input(Bool())

    // Output pins
    val PC_out      : UInt = Output(UInt(32.W))
    val PC4_out     : UInt = Output(UInt(32.W))
    val alu_out     : SInt = Output(SInt(32.W))
    val mem_data_out: SInt = Output(SInt(32.W))
    val rd_addr_out : UInt = Output(UInt(5.W))
    val imm_out     : SInt = Output(SInt(32.W))
    val wr_en_out   : Bool = Output(Bool())
    val br_en_out   : Bool = Output(Bool())
    val jalr_en_out : Bool = Output(Bool())
    val jal_en_out  : Bool = Output(Bool())
    val auipc_en_out: Bool = Output(Bool())
    val lui_en_out  : Bool = Output(Bool())
    val load_en_out : Bool = Output(Bool())
}
class RegMW extends Module
{
    // Initializing IO pins
    val io         : RegMW_IO = IO(new RegMW_IO())
    val PC_in      : UInt = dontTouch(WireInit(io.PC_in))
    val PC4_in     : UInt = dontTouch(WireInit(io.PC4_in))
    val alu_in     : SInt = dontTouch(WireInit(io.alu_in))
    val mem_data_in: SInt = dontTouch(WireInit(io.mem_data_in))
    val rd_addr_in : UInt = dontTouch(WireInit(io.rd_addr_in))
    val imm_in     : SInt = dontTouch(WireInit(io.imm_in))
    val wr_en_in   : Bool = dontTouch(WireInit(io.wr_en_in))
    val br_en_in   : Bool = dontTouch(WireInit(io.br_en_in))
    val jalr_en_in : Bool = dontTouch(WireInit(io.jalr_en_in))
    val jal_en_in  : Bool = dontTouch(WireInit(io.jal_en_in))
    val auipc_en_in: Bool = dontTouch(WireInit(io.auipc_en_in))
    val lui_en_in  : Bool = dontTouch(WireInit(io.lui_en_in))
    val load_en_in : Bool = dontTouch(WireInit(io.load_en_in))

    // Initializing registers
    val PC      : UInt = dontTouch(RegInit(0.U(32.W)))
    val PC4     : UInt = dontTouch(RegInit(0.U(32.W)))
    val alu     : SInt = dontTouch(RegInit(0.S(32.W)))
    val mem_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val rd_addr : UInt = dontTouch(RegInit(0.U(5.W)))
    val imm     : SInt = dontTouch(RegInit(0.S(32.W)))
    val wr_en   : Bool = dontTouch(RegInit(0.B))
    val br_en   : Bool = dontTouch(RegInit(0.B))
    val jalr_en : Bool = dontTouch(RegInit(0.B))
    val jal_en  : Bool = dontTouch(RegInit(0.B))
    val auipc_en: Bool = dontTouch(RegInit(0.B))
    val lui_en  : Bool = dontTouch(RegInit(0.B))
    val load_en : Bool = dontTouch(RegInit(0.B))

    // Intermediate wires
    val PC_out      : UInt = dontTouch(WireInit(PC))
    val PC4_out     : UInt = dontTouch(WireInit(PC4))
    val alu_out     : SInt = dontTouch(WireInit(alu))
    val mem_data_out: SInt = dontTouch(WireInit(mem_data))
    val rd_addr_out : UInt = dontTouch(WireInit(rd_addr))
    val imm_out     : SInt = dontTouch(WireInit(imm))
    val wr_en_out   : Bool = dontTouch(WireInit(wr_en))
    val br_en_out   : Bool = dontTouch(WireInit(br_en))
    val jalr_en_out : Bool = dontTouch(WireInit(jalr_en))
    val jal_en_out  : Bool = dontTouch(WireInit(jal_en))
    val auipc_en_out: Bool = dontTouch(WireInit(auipc_en))
    val lui_en_out  : Bool = dontTouch(WireInit(lui_en))
    val load_en_out : Bool = dontTouch(WireInit(load_en))

    // Wiring to output pins
    Array(
        // Output pins
        io.PC_out,     io.alu_out,     io.mem_data_out, io.rd_addr_out, io.imm_out,
        io.wr_en_out,  io.br_en_out,   io.jalr_en_out,  io.jal_en_out,  io.auipc_en_out,
        io.lui_en_out, io.load_en_out, io.PC4_out,

        // Registers
        PC,            alu,            mem_data,        rd_addr,        imm,
        wr_en,         br_en,          jalr_en,         jal_en,         auipc_en,
        lui_en,        load_en,        PC4
    ) zip Array(
        // Output pins
        PC_out,        alu_out,        mem_data_out,    rd_addr_out,    imm_out,
        wr_en_out,     br_en_out,      jalr_en_out,     jal_en_out,     auipc_en_out,
        lui_en_out,    load_en_out,    PC4_out,

        // Registers
        PC_in,         alu_in,         mem_data_in,     rd_addr_in,     imm_in,
        wr_en_in,      br_en_in,       jalr_en_in,      jal_en_in,      auipc_en_in,
        lui_en_in,     load_en_in,     PC4_in
    ) foreach
    {
        x => x._1 := x._2
    }
}

