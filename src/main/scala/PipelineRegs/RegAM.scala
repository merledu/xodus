package PipelineRegs

import chisel3._

class RegAM_IO extends Bundle
{
    // Input pins
    val PC_in      : UInt = Input(UInt(32.W))
    val alu_in     : SInt = Input(SInt(32.W))
    val rd_addr_in : UInt = Input(UInt(5.W))
    val rs2_data_in: SInt = Input(SInt(32.W))
    val imm_in     : SInt = Input(SInt(32.W))
    val wr_en_in   : Bool = Input(Bool())
    val str_en_in  : Bool = Input(Bool())
    val sb_en_in   : Bool = Input(Bool())
    val sh_en_in   : Bool = Input(Bool())
    val sw_en_in   : Bool = Input(Bool())
    val load_en_in : Bool = Input(Bool())
    val lb_en_in   : Bool = Input(Bool())
    val lh_en_in   : Bool = Input(Bool())
    val lw_en_in   : Bool = Input(Bool())
    val lbu_en_in  : Bool = Input(Bool())
    val lhu_en_in  : Bool = Input(Bool())
    val jalr_en_in : Bool = Input(Bool())
    val jal_en_in  : Bool = Input(Bool())
    val auipc_en_in: Bool = Input(Bool())
    val lui_en_in  : Bool = Input(Bool())

    // Output pins
    val PC_out      : UInt = Output(UInt(32.W))
    val alu_out     : SInt = Output(SInt(32.W))
    val rd_addr_out : UInt = Output(UInt(5.W))
    val rs2_data_out: SInt = Output(SInt(32.W))
    val imm_out     : SInt = Output(SInt(32.W))
    val wr_en_out   : Bool = Output(Bool())
    val str_en_out  : Bool = Output(Bool())
    val load_en_out : Bool = Output(Bool())
    val jalr_en_out : Bool = Output(Bool())
    val jal_en_out  : Bool = Output(Bool())
    val auipc_en_out: Bool = Output(Bool())
    val lui_en_out  : Bool = Output(Bool())
    val sb_en_out   : Bool = Output(Bool())
    val sh_en_out   : Bool = Output(Bool())
    val sw_en_out   : Bool = Output(Bool())
    val lb_en_out   : Bool = Output(Bool())
    val lh_en_out   : Bool = Output(Bool())
    val lw_en_out   : Bool = Output(Bool())
    val lbu_en_out  : Bool = Output(Bool())
    val lhu_en_out  : Bool = Output(Bool())
}
class RegAM extends Module
{
    // Initializing IO pins
    val io         : RegAM_IO = IO(new RegAM_IO)
    val PC_in      : UInt     = dontTouch(WireInit(io.PC_in))
    val alu_in     : SInt     = dontTouch(WireInit(io.alu_in))
    val rd_addr_in : UInt     = dontTouch(WireInit(io.rd_addr_in))
    val rs2_data_in: SInt     = dontTouch(WireInit(io.rs2_data_in))
    val imm_in     : SInt     = dontTouch(WireInit(io.imm_in))
    val wr_en_in   : Bool     = dontTouch(WireInit(io.wr_en_in))
    val str_en_in  : Bool     = dontTouch(WireInit(io.str_en_in))
    val sb_en_in   : Bool     = dontTouch(WireInit(io.sb_en_in))
    val sh_en_in   : Bool     = dontTouch(WireInit(io.sh_en_in))
    val sw_en_in   : Bool     = dontTouch(WireInit(io.sw_en_in))
    val load_en_in : Bool     = dontTouch(WireInit(io.load_en_in))
    val lb_en_in   : Bool     = dontTouch(WireInit(io.lb_en_in))
    val lh_en_in   : Bool     = dontTouch(WireInit(io.lh_en_in))
    val lw_en_in   : Bool     = dontTouch(WireInit(io.lw_en_in))
    val lbu_en_in  : Bool     = dontTouch(WireInit(io.lbu_en_in))
    val lhu_en_in  : Bool     = dontTouch(WireInit(io.lhu_en_in))
    val jalr_en_in : Bool     = dontTouch(WireInit(io.jalr_en_in))
    val jal_en_in  : Bool     = dontTouch(WireInit(io.jal_en_in))
    val auipc_en_in: Bool     = dontTouch(WireInit(io.auipc_en_in))
    val lui_en_in  : Bool     = dontTouch(WireInit(io.lui_en_in))

    // Initializing registers
    val PC      : UInt = dontTouch(RegInit(0.U(32.W)))
    val alu     : SInt = dontTouch(RegInit(0.S(32.W)))
    val rd_addr : UInt = dontTouch(RegInit(0.U(5.W)))
    val rs2_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val imm     : SInt = dontTouch(RegInit(0.S(32.W)))
    val wr_en   : Bool = dontTouch(RegInit(0.B))
    val str_en  : Bool = dontTouch(RegInit(0.B))
    val sb_en   : Bool = dontTouch(RegInit(0.B))
    val sh_en   : Bool = dontTouch(RegInit(0.B))
    val sw_en   : Bool = dontTouch(RegInit(0.B))
    val load_en : Bool = dontTouch(RegInit(0.B))
    val lb_en   : Bool = dontTouch(RegInit(0.B))
    val lh_en   : Bool = dontTouch(RegInit(0.B))
    val lw_en   : Bool = dontTouch(RegInit(0.B))
    val lbu_en  : Bool = dontTouch(RegInit(0.B))
    val lhu_en  : Bool = dontTouch(RegInit(0.B))
    val jalr_en : Bool = dontTouch(RegInit(0.B))
    val jal_en  : Bool = dontTouch(RegInit(0.B))
    val auipc_en: Bool = dontTouch(RegInit(0.B))
    val lui_en  : Bool = dontTouch(RegInit(0.B))

    // Intermediate wires
    val PC_out      : UInt = dontTouch(WireInit(PC))
    val alu_out     : SInt = dontTouch(WireInit(alu))
    val rd_addr_out : UInt = dontTouch(WireInit(rd_addr))
    val rs2_data_out: SInt = dontTouch(WireInit(rs2_data))
    val imm_out     : SInt = dontTouch(WireInit(imm))
    val wr_en_out   : Bool = dontTouch(WireInit(wr_en))
    val str_en_out  : Bool = dontTouch(WireInit(str_en))
    val sb_en_out   : Bool = dontTouch(WireInit(sb_en))
    val sh_en_out   : Bool = dontTouch(WireInit(sh_en))
    val sw_en_out   : Bool = dontTouch(WireInit(sw_en))
    val load_en_out : Bool = dontTouch(WireInit(load_en))
    val lb_en_out   : Bool = dontTouch(WireInit(lb_en))
    val lh_en_out   : Bool = dontTouch(WireInit(lh_en))
    val lw_en_out   : Bool = dontTouch(WireInit(lw_en))
    val lbu_en_out  : Bool = dontTouch(WireInit(lbu_en))
    val lhu_en_out  : Bool = dontTouch(WireInit(lhu_en))
    val jalr_en_out : Bool = dontTouch(WireInit(jalr_en))
    val jal_en_out  : Bool = dontTouch(WireInit(jal_en))
    val auipc_en_out: Bool = dontTouch(WireInit(auipc_en))
    val lui_en_out  : Bool = dontTouch(WireInit(lui_en))

    // Wiring to output pins
    Seq(
        // Output pins
        io.PC_out,       io.alu_out,    io.rd_addr_out, io.rs2_data_out, io.imm_out,
        io.wr_en_out,    io.str_en_out, io.load_en_out, io.jalr_en_out,  io.jal_en_out,
        io.auipc_en_out, io.lui_en_out, io.sb_en_out,   io.sh_en_out,    io.sw_en_out,
        io.lb_en_out,    io.lh_en_out,  io.lw_en_out,   io.lbu_en_out,   io.lhu_en_out,
        
        // Registers
        PC,              alu,           rd_addr,        rs2_data,        imm,
        wr_en,           str_en,        load_en,        jalr_en,         jal_en,
        auipc_en,        lui_en,        sb_en,          sh_en,           sw_en,
        lb_en,           lh_en,         lw_en,          lbu_en,          lhu_en,
    ) zip Seq(
        // Output pins
        PC_out,          alu_out,       rd_addr_out,    rs2_data_out,    imm_out,
        wr_en_out,       str_en_out,    load_en_out,    jalr_en_out,     jal_en_out,
        auipc_en_out,    lui_en_out,    sb_en_out,      sh_en_out,       sw_en_out,
        lb_en_out,       lh_en_out,     lw_en_out,      lbu_en_out,      lhu_en_out,

        // Registers
        PC_in,           alu_in,        rd_addr_in,     rs2_data_in,     imm_in,
        wr_en_in,        str_en_in,     load_en_in,     jalr_en_in,      jal_en_in,
        auipc_en_in,     lui_en_in,     sb_en_in,       sh_en_in,        sw_en_in,
        lb_en_in,        lh_en_in,      lw_en_in,       lbu_en_in,       lhu_en_in,
    ) foreach
    {
        x => x._1 := x._2
    }
}
