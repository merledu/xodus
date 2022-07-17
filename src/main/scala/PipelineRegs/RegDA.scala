package PipelineRegs

import chisel3._

class RegDA_IO extends Bundle
{
    // Input pins
    val PC_in      : UInt = Input(UInt(32.W))
    val rd_addr_in : UInt = Input(UInt(5.W))
    val func3_in   : UInt = Input(UInt(3.W))
    val rs1_data_in: SInt = Input(SInt(32.W))
    val rs2_data_in: SInt = Input(SInt(32.W))
    val func7_in   : UInt = Input(UInt(7.W))
    val imm_in     : SInt = Input(SInt(32.W))
    val wr_en_in   : Bool = Input(Bool())
    val imm_en_in  : Bool = Input(Bool())
    val str_en_in  : Bool = Input(Bool())
    val ld_en_in   : Bool = Input(Bool())
    val br_en_in   : Bool = Input(Bool())
    val jal_en_in  : Bool = Input(Bool())
    val jalr_en_in : Bool = Input(Bool())
    val auipc_en_in: Bool = Input(Bool())
    val lui_en_in  : Bool = Input(Bool())

    // Output pins
    val PC_out      : UInt = Output(UInt(32.W))
    val rd_addr_out : UInt = Output(UInt(5.W))
    val func3_out   : UInt = Output(UInt(3.W))
    val rs1_data_out: SInt = Output(SInt(32.W))
    val rs2_data_out: SInt = Output(SInt(32.W))
    val func7_out   : UInt = Output(UInt(7.W))
    val imm_out     : SInt = Output(SInt(32.W))
    val wr_en_out   : Bool = Output(Bool())
    val imm_en_out  : Bool = Output(Bool())
    val str_en_out  : Bool = Output(Bool())
    val ld_en_out   : Bool = Output(Bool())
    val br_en_out   : Bool = Output(Bool())
    val jal_en_out  : Bool = Output(Bool())
    val jalr_en_out : Bool = Output(Bool())
    val auipc_en_out: Bool = Output(Bool())
    val lui_en_out  : Bool = Output(Bool())
}
class RegDA extends Module
{
    // Initializing IO pins
    val io: RegDA_IO = IO(new RegDA_IO())

    // Input wires
    val PC_in      : UInt = dontTouch(WireInit(io.PC_in))
    val rd_addr_in : UInt = dontTouch(WireInit(io.rd_addr_in))
    val func3_in   : UInt = dontTouch(WireInit(io.func3_in))
    val rs1_data_in: SInt = dontTouch(WireInit(io.rs1_data_in))
    val rs2_data_in: SInt = dontTouch(WireInit(io.rs2_data_in))
    val func7_in   : UInt = dontTouch(WireInit(io.func7_in))
    val imm_in     : SInt = dontTouch(WireInit(io.imm_in))
    val wr_en_in   : Bool = dontTouch(WireInit(io.wr_en_in))
    val imm_en_in  : Bool = dontTouch(WireInit(io.imm_en_in))
    val str_en_in  : Bool = dontTouch(WireInit(io.str_en_in))
    val ld_en_in   : Bool = dontTouch(WireInit(io.ld_en_in))
    val br_en_in   : Bool = dontTouch(WireInit(io.br_en_in))
    val jal_en_in  : Bool = dontTouch(WireInit(io.jal_en_in))
    val jalr_en_in : Bool = dontTouch(WireInit(io.jalr_en_in))
    val auipc_en_in: Bool = dontTouch(WireInit(io.auipc_en_in))
    val lui_en_in  : Bool = dontTouch(WireInit(io.lui_en_in))

    // Initializing registers
    val PC      : UInt = dontTouch(RegInit(0.U(32.W)))
    val rd_addr : UInt = dontTouch(RegInit(0.U(5.W)))
    val func3   : UInt = dontTouch(RegInit(0.U(3.W)))
    val rs1_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val rs2_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val func7   : UInt = dontTouch(RegInit(0.U(7.W)))
    val imm     : SInt = dontTouch(RegInit(0.S(32.W)))
    val wr_en   : Bool = dontTouch(RegInit(0.B))
    val imm_en  : Bool = dontTouch(RegInit(0.B))
    val str_en  : Bool = dontTouch(RegInit(0.B))
    val ld_en   : Bool = dontTouch(RegInit(0.B))
    val br_en   : Bool = dontTouch(RegInit(0.B))
    val jal_en  : Bool = dontTouch(RegInit(0.B))
    val jalr_en : Bool = dontTouch(RegInit(0.B))
    val auipc_en: Bool = dontTouch(RegInit(0.B))
    val lui_en  : Bool = dontTouch(RegInit(0.B))

    // Output wires
    val PC_out      : UInt = dontTouch(WireInit(PC))
    val rd_addr_out : UInt = dontTouch(WireInit(rd_addr))
    val func3_out   : UInt = dontTouch(WireInit(func3))
    val rs1_data_out: SInt = dontTouch(WireInit(rs1_data))
    val rs2_data_out: SInt = dontTouch(WireInit(rs2_data))
    val func7_out   : UInt = dontTouch(WireInit(func7))
    val imm_out     : SInt = dontTouch(WireInit(imm))
    val wr_en_out   : Bool = dontTouch(WireInit(wr_en))
    val imm_en_out  : Bool = dontTouch(WireInit(imm_en))
    val str_en_out  : Bool = dontTouch(WireInit(str_en))
    val ld_en_out   : Bool = dontTouch(WireInit(ld_en))
    val br_en_out   : Bool = dontTouch(WireInit(br_en))
    val jal_en_out  : Bool = dontTouch(WireInit(jal_en))
    val jalr_en_out : Bool = dontTouch(WireInit(jalr_en))
    val auipc_en_out: Bool = dontTouch(WireInit(auipc_en))
    val lui_en_out  : Bool = dontTouch(WireInit(lui_en))

    // Wiring to output pins
    Array(
        // Output pins
        io.PC_out,     io.rd_addr_out, io.func3_out,  io.rs1_data_out, io.rs2_data_out,
        io.func7_out,  io.imm_out,     io.wr_en_out,  io.imm_en_out,   io.str_en_out,
        io.ld_en_out,  io.br_en_out,   io.jal_en_out, io.jalr_en_out,  io.auipc_en_out,
        io.lui_en_out,

        // Registers
        PC,            rd_addr,        func3,         rs1_data,        rs2_data,
        func7,         imm,            wr_en,         imm_en,          str_en,
        ld_en,         br_en,          jal_en,        jalr_en,         auipc_en,
        lui_en
    ) zip Array(
        // Output pins
        PC_out,        rd_addr_out,    func3_out,     rs1_data_out,    rs2_data_out,
        func7_out,     imm_out,        wr_en_out,     imm_en_out,      str_en_out,
        ld_en_out,     br_en_out,      jal_en_out,    jalr_en_out,     auipc_en_out,
        lui_en_out,

        // Registers
        PC_in,         rd_addr_in,     func3_in,      rs1_data_in,     rs2_data_in,
        func7_in,      imm_in,         wr_en_in,      imm_en_in,       str_en_in,
        ld_en_in,      br_en_in,       jal_en_in,     jalr_en_in,      auipc_en_in,
        lui_en_in
    ) foreach
    {
        x => x._1 := x._2
    }
}
