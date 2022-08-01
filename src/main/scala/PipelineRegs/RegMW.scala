package PipelineRegs

import chisel3._

class RegMW_IO extends Bundle
{
    // Input pins
    val PC_in       : UInt = Input(UInt(32.W))
    val alu_in      : SInt = Input(SInt(32.W))
    val mem_data_in : SInt = Input(SInt(32.W))
    val rd_addr_in  : UInt = Input(UInt(5.W))
    val i_s_b_imm_in: SInt = Input(SInt(12.W))
    val u_j_imm_in  : SInt = Input(SInt(20.W))
    val wr_en_in    : Bool = Input(Bool())
    val br_en_in    : Bool = Input(Bool())
    val jalr_en_in  : Bool = Input(Bool())
    val jal_en_in   : Bool = Input(Bool())
    val auipc_en_in : Bool = Input(Bool())
    val lui_en_in   : Bool = Input(Bool())

    // Output pins
    val PC_out       : UInt = Output(UInt(32.W))
    val alu_out      : SInt = Output(SInt(32.W))
    val mem_data_out : SInt = Output(SInt(32.W))
    val rd_addr_out  : UInt = Output(UInt(5.W))
    val i_s_b_imm_out: SInt = Output(SInt(12.W))
    val u_j_imm_out  : SInt = Output(SInt(20.W))
    val wr_en_out    : Bool = Output(Bool())
    val br_en_out    : Bool = Output(Bool())
    val jalr_en_out  : Bool = Output(Bool())
    val jal_en_out   : Bool = Output(Bool())
    val auipc_en_out : Bool = Output(Bool())
    val lui_en_out   : Bool = Output(Bool())
}
class RegMW extends Module
{
    // Initializing IO pins
    val io: RegMW_IO = IO(new RegMW_IO())

    // Input wires
    val PC_in       : UInt = dontTouch(WireInit(io.PC_in))
    val alu_in      : SInt = dontTouch(WireInit(io.alu_in))
    val mem_data_in : SInt = dontTouch(WireInit(io.mem_data_in))
    val rd_addr_in  : UInt = dontTouch(WireInit(io.rd_addr_in))
    val i_s_b_imm_in: SInt = dontTouch(WireInit(io.i_s_b_imm_in))
    val u_j_imm_in  : SInt = dontTouch(WireInit(io.u_j_imm_in))
    val wr_en_in    : Bool = dontTouch(WireInit(io.wr_en_in))
    val br_en_in    : Bool = dontTouch(WireInit(io.br_en_in))
    val jalr_en_in  : Bool = dontTouch(WireInit(io.jalr_en_in))
    val jal_en_in   : Bool = dontTouch(WireInit(io.jal_en_in))
    val auipc_en_in : Bool = dontTouch(WireInit(io.auipc_en_in))
    val lui_en_in   : Bool = dontTouch(WireInit(io.lui_en_in))

    // Initializing registers
    val PC       : UInt = dontTouch(RegInit(0.U(32.W)))
    val alu      : SInt = dontTouch(RegInit(0.S(32.W)))
    val mem_data : SInt = dontTouch(RegInit(0.S(32.W)))
    val rd_addr  : UInt = dontTouch(RegInit(0.U(5.W)))
    val i_s_b_imm: SInt = dontTouch(RegInit(0.S(12.W)))
    val u_j_imm  : SInt = dontTouch(RegInit(0.S(20.W)))
    val wr_en    : Bool = dontTouch(RegInit(0.B))
    val br_en    : Bool = dontTouch(RegInit(0.B))
    val jalr_en  : Bool = dontTouch(RegInit(0.B))
    val jal_en   : Bool = dontTouch(RegInit(0.B))
    val auipc_en : Bool = dontTouch(RegInit(0.B))
    val lui_en   : Bool = dontTouch(RegInit(0.B))

    // Output wires
    val PC_out       : UInt = dontTouch(WireInit(PC))
    val alu_out      : SInt = dontTouch(WireInit(alu))
    val mem_data_out : SInt = dontTouch(WireInit(mem_data))
    val rd_addr_out  : UInt = dontTouch(WireInit(rd_addr))
    val i_s_b_imm_out: SInt = dontTouch(WireInit(i_s_b_imm))
    val u_j_imm_out  : SInt = dontTouch(WireInit(u_j_imm))
    val wr_en_out    : Bool = dontTouch(WireInit(wr_en))
    val br_en_out    : Bool = dontTouch(WireInit(br_en))
    val jalr_en_out  : Bool = dontTouch(WireInit(jalr_en))
    val jal_en_out   : Bool = dontTouch(WireInit(jal_en))
    val auipc_en_out : Bool = dontTouch(WireInit(auipc_en))
    val lui_en_out   : Bool = dontTouch(WireInit(lui_en))

    // Wiring to output pins
    Array(
        // Output pins
        io.PC_out,       io.alu_out,    io.mem_data_out, io.rd_addr_out, io.i_s_b_imm_out,
        io.u_j_imm_out,  io.wr_en_out,  io.br_en_out,    io.jalr_en_out, io.jal_en_out,
        io.auipc_en_out, io.lui_en_out,

        // Registers
        PC,              alu,           mem_data,        rd_addr,        i_s_b_imm,
        u_j_imm,         wr_en,         br_en,           jalr_en,        jal_en,
        auipc_en,        lui_en
    ) zip Array(
        // Output pins
        PC,              alu,           mem_data,        rd_addr,        i_s_b_imm,
        u_j_imm,         wr_en,         br_en,           jalr_en,        jal_en,
        auipc_en,        lui_en,

        // Registers
        PC_in,           alu_in,        mem_data_in,     rd_addr_in,     i_s_b_imm_in,
        u_j_imm_in,      wr_en_in,      br_en_in,        jalr_en_in,     jal_en_in,
        auipc_en_in,     lui_en_in
    ) foreach
    {
        x => x._1 := x._2
    }
}

