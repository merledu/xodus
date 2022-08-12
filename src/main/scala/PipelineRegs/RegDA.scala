package PipelineRegs

import chisel3._
import chisel3.util._

class RegDA_IO extends Bundle
{
    // Input pins
    val PC_in              : UInt = Input(UInt(32.W))
    val opcode_in          : UInt = Input(UInt(7.W))
    val rd_addr_in         : UInt = Input(UInt(5.W))
    val func3_in           : UInt = Input(UInt(3.W))
    val rs1_addr_in        : UInt = Input(UInt(5.W))
    val rs1_data_in        : SInt = Input(SInt(32.W))
    val rs2_addr_in        : UInt = Input(UInt(5.W))
    val rs2_data_in        : SInt = Input(SInt(32.W))
    val func7_in           : UInt = Input(UInt(7.W))
    val imm_in             : SInt = Input(SInt(32.W))
    val forward_operand1   : UInt = Input(UInt(2.W))
    val forward_operand2   : UInt = Input(UInt(2.W))
    val RegAM_rd_data      : SInt = Input(SInt(32.W))
    val WriteBack_rd_data  : SInt = Input(SInt(32.W))
    val stallControl_in    : Bool = Input(Bool())
    val jal_en_in          : Bool = Input(Bool())
    val forward_rs1_rd_data: Bool = Input(Bool())
    val forward_rs2_rd_data: Bool = Input(Bool())

    // Output pins
    val PC_out          : UInt = Output(UInt(32.W))
    val opcode_out      : UInt = Output(UInt(7.W))
    val rd_addr_out     : UInt = Output(UInt(5.W))
    val func3_out       : UInt = Output(UInt(3.W))
    val rs1_addr_out    : UInt = Output(UInt(5.W))
    val rs1_data_out    : SInt = Output(SInt(32.W))
    val rs2_addr_out    : UInt = Output(UInt(5.W))
    val rs2_data_out    : SInt = Output(SInt(32.W))
    val func7_out       : UInt = Output(UInt(7.W))
    val imm_out         : SInt = Output(SInt(32.W))
    val stallControl_out: Bool = Output(Bool())
    val jal_en_out      : Bool = Output(Bool())
}
class RegDA extends Module
{
    // Initializing IO pins
    val io                 : RegDA_IO = IO(new RegDA_IO)
    val PC_in              : UInt     = dontTouch(WireInit(io.PC_in))
    val opcode_in          : UInt     = dontTouch(WireInit(io.opcode_in))
    val rd_addr_in         : UInt     = dontTouch(WireInit(io.rd_addr_in))
    val func3_in           : UInt     = dontTouch(WireInit(io.func3_in))
    val rs1_addr_in        : UInt     = dontTouch(WireInit(io.rs1_addr_in))
    val rs1_data_in        : SInt     = dontTouch(WireInit(io.rs1_data_in))
    val rs2_addr_in        : UInt     = dontTouch(WireInit(io.rs2_addr_in))
    val rs2_data_in        : SInt     = dontTouch(WireInit(io.rs2_data_in))
    val func7_in           : UInt     = dontTouch(WireInit(io.func7_in))
    val imm_in             : SInt     = dontTouch(WireInit(io.imm_in))
    val forward_operand1   : UInt     = dontTouch(WireInit(io.forward_operand1))
    val forward_operand2   : UInt     = dontTouch(WireInit(io.forward_operand2))
    val RegAM_rd_data      : SInt     = dontTouch(WireInit(io.RegAM_rd_data))
    val WriteBack_rd_data  : SInt     = dontTouch(WireInit(io.WriteBack_rd_data))
    val stallControl_in    : Bool     = dontTouch(WireInit(io.stallControl_in))
    val jal_en_in          : Bool     = dontTouch(WireInit(io.jal_en_in))
    val forward_rs1_rd_data: Bool     = dontTouch(WireInit(io.forward_rs1_rd_data))
    val forward_rs2_rd_data: Bool     = dontTouch(WireInit(io.forward_rs2_rd_data))
    
    // Initializing registers
    val PC          : UInt = dontTouch(RegInit(0.U(32.W)))
    val opcode      : UInt = dontTouch(RegInit(0.U(7.W)))
    val rd_addr     : UInt = dontTouch(RegInit(0.U(5.W)))
    val func3       : UInt = dontTouch(RegInit(0.U(3.W)))
    val rs1_addr    : UInt = dontTouch(RegInit(0.U(5.W)))
    val rs1_data    : SInt = dontTouch(RegInit(0.S(32.W)))
    val rs2_addr    : UInt = dontTouch(RegInit(0.U(5.W)))
    val rs2_data    : SInt = dontTouch(RegInit(0.S(32.W)))
    val func7       : UInt = dontTouch(RegInit(0.U(7.W)))
    val imm         : SInt = dontTouch(RegInit(0.S(32.W)))
    val stallControl: Bool = dontTouch(RegInit(0.B))
    val jal_en      : Bool = dontTouch(RegInit(0.B))
    
    // Intermediate wires
    val PC_out          : UInt = dontTouch(WireInit(PC))
    val opcode_out      : UInt = dontTouch(WireInit(opcode))
    val rd_addr_out     : UInt = dontTouch(WireInit(rd_addr))
    val func3_out       : UInt = dontTouch(WireInit(func3))
    val rs1_addr_out    : UInt = dontTouch(WireInit(rs1_addr))
    val rs1_data_out    : SInt = dontTouch(WireInit(MuxLookup(forward_operand1, rs1_data, Seq(
        1.U -> RegAM_rd_data,
        2.U -> WriteBack_rd_data
    ))))
    val rs2_addr_out    : UInt = dontTouch(WireInit(rs2_addr))
    val rs2_data_out    : SInt = dontTouch(WireInit(MuxLookup(forward_operand2, rs2_data, Seq(
        1.U -> RegAM_rd_data,
        2.U -> WriteBack_rd_data
    ))))
    val func7_out       : UInt = dontTouch(WireInit(func7))
    val imm_out         : SInt = dontTouch(WireInit(imm))
    val stallControl_out: Bool = dontTouch(WireInit(stallControl))
    val jal_en_out      : Bool = dontTouch(WireInit(jal_en))
    val rs1_data_PC     : SInt = dontTouch(WireInit(Mux(forward_rs1_rd_data, WriteBack_rd_data, rs1_data_in)))
    val rs2_data_PC     : SInt = dontTouch(WireInit(Mux(forward_rs2_rd_data, WriteBack_rd_data, rs2_data_in)))

    // Wiring to output pins
    Seq(
        // Output pins
        io.PC_out,           io.opcode_out,       io.rd_addr_out,   io.func3_out,     io.rs1_addr_out,
        io.rs1_data_out,     io.rs2_addr_out,     io.rs2_data_out,  io.func7_out,     io.imm_out,
        io.stallControl_out, io.jal_en_out,

        // Registers
        PC,                  opcode,              rd_addr,          func3,            rs1_addr,
        rs1_data,            rs2_addr,            rs2_data,         func7,            imm,
        stallControl,        jal_en
    ) zip Seq(               
        // Output pins       
        PC_out,              opcode_out,          rd_addr_out,      func3_out,        rs1_addr_out,
        rs1_data_out,        rs2_addr_out,        rs2_data_out,     func7_out,        imm_out,
        stallControl_out,    jal_en_out,
                             
        // Registers         
        PC_in,               opcode_in,           rd_addr_in,       func3_in,         rs1_addr_in,
        rs1_data_PC,         rs2_addr_in,         rs2_data_PC,      func7_in,         imm_in,
        stallControl_in,     jal_en_in
    ) foreach
    {
        x => x._1 := x._2
    }
}

