package Tracer

import chisel3._

class Tracer_IO extends Bundle
{
    // Constants
    val XLEN: Int = 32
    val NRET: Int = 1
    val ILEN: Int = 32

    // Input ports
    val RegFD_inst       : UInt = Input(UInt(32.W))
    val RegDA_rs1_addr   : UInt = Input(UInt(5.W))
    val RegDA_rs2_addr   : UInt = Input(UInt(5.W))
    val RegDA_rs1_data   : SInt = Input(SInt(32.W))
    val RegAM_rs2_data   : SInt = Input(SInt(32.W))
    val RegMW_wr_en      : Bool = Input(Bool())
    val RegMW_rd_addr    : UInt = Input(UInt(5.W))
    val WriteBack_rd_data: SInt = Input(SInt(32.W))
    val RegDA_PC         : UInt = Input(UInt(32.W))
    val Fetch_nPC        : UInt = Input(UInt(32.W))
    val RegAM_load_en    : Bool = Input(Bool())
    val RegAM_str_en     : Bool = Input(Bool())
    val RegAM_alu        : SInt = Input(SInt(32.W))

    // Output ports  (TODO: Commented ports are not used by this tracer, need to be added in the future)
    // - Instruction metadata
    // val rvfi_valid: UInt = Output(UInt(NRET.W))
    // val rvfi_order: UInt = Output(UInt((NRET * 64).W))
    val rvfi_insn : UInt = Output(UInt((NRET * ILEN).W))
    // val rvfi_trap : UInt = Output(UInt(NRET.W))
    // val rvfi_halt : UInt = Output(UInt(NRET.W))
    // val rvfi_intr : UInt = Output(UInt(NRET.W))
    val rvfi_mode : UInt = Output(UInt((NRET * 2).W))
    // val rvfi_ixl  : UInt = Output(UInt((NRET * 2).W))
    // - Register read/write
    val rvfi_rs1_addr : UInt = Output(UInt((NRET * 5).W))
    val rvfi_rs2_addr : UInt = Output(UInt((NRET * 5).W))
    val rvfi_rs1_rdata: SInt = Output(SInt((NRET * XLEN).W))
    val rvfi_rs2_rdata: SInt = Output(SInt((NRET * XLEN).W))
    val rvfi_rd_addr  : UInt = Output(UInt((NRET * 5).W))
    val rvfi_rd_wdata : SInt = Output(SInt((NRET * XLEN).W))
    // - Program Counter
    val rvfi_pc_rdata: UInt = Output(UInt((NRET * XLEN).W))
    val rvfi_pc_wdata: UInt = Output(UInt((NRET * XLEN).W))
    // - Memory Access
    val rvfi_mem_addr : UInt = Output(UInt((NRET * XLEN).W))
    // val rvfi_mem_rmask: UInt = Output(UInt((NRET * XLEN / 8).W))
    // val rvfi_mem_wmask: UInt = Output(UInt((NRET * XLEN / 8).W))
    val rvfi_mem_rdata: SInt = Output(SInt((NRET * XLEN).W))
    val rvfi_mem_wdata: SInt = Output(SInt((NRET * XLEN).W))
}
class Tracer extends Module
{
    // Initializing IO ports
    val io               : Tracer_IO = IO(new Tracer_IO)
    val RegFD_inst       : UInt      = dontTouch(WireInit(io.RegFD_inst))
    val RegDA_rs1_addr   : UInt      = dontTouch(WireInit(io.RegDA_rs1_addr))
    val RegDA_rs2_addr   : UInt      = dontTouch(WireInit(io.RegDA_rs2_addr))
    val RegDA_rs1_data   : SInt      = dontTouch(WireInit(io.RegDA_rs1_data))
    val RegAM_rs2_data   : SInt      = dontTouch(WireInit(io.RegAM_rs2_data))
    val RegMW_wr_en      : Bool      = dontTouch(WireInit(io.RegMW_wr_en))
    val RegMW_rd_addr    : UInt      = dontTouch(WireInit(io.RegMW_rd_addr))
    val WriteBack_rd_data: SInt      = dontTouch(WireInit(io.WriteBack_rd_data))
    val RegDA_PC         : UInt      = dontTouch(WireInit(io.RegDA_PC))
    val Fetch_nPC        : UInt      = dontTouch(WireInit(io.Fetch_nPC))
    val RegAM_load_en    : Bool      = dontTouch(WireInit(io.RegAM_load_en))
    val RegAM_str_en     : Bool      = dontTouch(WireInit(io.RegAM_str_en))
    val RegAM_mem_addr   : UInt      = dontTouch(WireInit(io.RegAM_alu.asUInt))

    // Delay Registers
    // - inst
    val RegDA_inst: UInt = dontTouch(RegInit(0.U(32.W)))
    val RegAM_inst: UInt = dontTouch(RegInit(0.U(32.W)))
    val RegMW_inst: UInt = dontTouch(RegInit(0.U(32.W)))
    // - rs1_addr/rs2_addr
    val RegAM_rs1_addr: UInt = dontTouch(RegInit(0.U(5.W)))
    val RegAM_rs2_addr: UInt = dontTouch(RegInit(0.U(5.W)))
    val RegMW_rs1_addr: UInt = dontTouch(RegInit(0.U(5.W)))
    val RegMW_rs2_addr: UInt = dontTouch(RegInit(0.U(5.W)))
    // - rs1_data/rs2_data
    val RegAM_rs1_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val RegMW_rs1_data: SInt = dontTouch(RegInit(0.S(32.W)))
    val RegMW_rs2_data: SInt = dontTouch(RegInit(0.S(32.W)))
    // - PC/nPC
    val RegAM_PC : UInt = dontTouch(RegInit(0.U(32.W)))
    val RegMW_PC : UInt = dontTouch(RegInit(0.U(32.W)))
    val RegFD_nPC: UInt = dontTouch(RegInit(0.U(32.W)))
    val RegDA_nPC: UInt = dontTouch(RegInit(0.U(32.W)))
    val RegAM_nPC: UInt = dontTouch(RegInit(0.U(32.W)))
    val RegMW_nPC: UInt = dontTouch(RegInit(0.U(32.W)))
    // - load_en/str_en/mem_addr
    val RegMW_load_en : Bool = dontTouch(RegInit(0.B))
    val RegMW_str_en  : Bool = dontTouch(RegInit(0.B))
    val RegMW_mem_addr: UInt = dontTouch(RegInit(0.U(32.W)))

    // Intermediate wires
    // - Instruction metadata
    val rvfi_insn: UInt = dontTouch(WireInit(RegMW_inst))
    val rvfi_mode: UInt = dontTouch(WireInit(3.U))
    // - Register read/write
    val rvfi_rs1_addr : UInt = dontTouch(WireInit(RegMW_rs1_addr))
    val rvfi_rs2_addr : UInt = dontTouch(WireInit(RegMW_rs2_addr))
    val rvfi_rs1_rdata: SInt = dontTouch(WireInit(RegMW_rs1_data))
    val rvfi_rs2_rdata: SInt = dontTouch(WireInit(RegMW_rs2_data))
    val rvfi_rd_addr  : UInt = dontTouch(WireInit(Mux(RegMW_wr_en, RegMW_rd_addr, 0.U)))
    val rvfi_rd_wdata : SInt = dontTouch(WireInit(Mux(RegMW_wr_en, WriteBack_rd_data, 0.S)))
    // - Program Counter
    val rvfi_pc_rdata: UInt = dontTouch(WireInit(RegMW_PC))
    val rvfi_pc_wdata: UInt = dontTouch(WireInit(RegMW_nPC))
    // - Memory Access
    val rvfi_mem_addr : UInt = dontTouch(WireInit(Mux(RegMW_load_en || RegMW_str_en, RegMW_mem_addr, 0.U)))
    val rvfi_mem_rdata: SInt = dontTouch(WireInit(Mux(RegMW_load_en, WriteBack_rd_data, 0.S)))
    val rvfi_mem_wdata: SInt = dontTouch(WireInit(Mux(RegMW_str_en, RegMW_rs2_data, 0.S)))

    // Wiring to output ports
    Seq(
        // Output ports
        io.rvfi_insn,      io.rvfi_mode,      io.rvfi_rs1_addr,  io.rvfi_rs2_addr, io.rvfi_rs1_rdata,
        io.rvfi_rs2_rdata, io.rvfi_rd_addr,   io.rvfi_rd_wdata,  io.rvfi_pc_rdata, io.rvfi_pc_wdata,
        io.rvfi_mem_addr,  io.rvfi_mem_rdata, io.rvfi_mem_wdata,

        // Delay Registers
        // - inst
        RegDA_inst, RegAM_inst, RegMW_inst,
        // - rs1_addr/rs2_addr
        RegAM_rs1_addr, RegAM_rs2_addr, RegMW_rs1_addr, RegMW_rs2_addr,
        // - rs1_data/rs2_data
        RegAM_rs1_data, RegMW_rs1_data, RegMW_rs2_data,
        // - PC/nPC
        RegAM_PC,  RegMW_PC, RegFD_nPC, RegDA_nPC, RegAM_nPC,
        RegMW_nPC,
        // - load_en/str_en/mem_addr
        RegMW_load_en, RegMW_str_en, RegMW_mem_addr
    ) zip Seq(
        // Output ports
        rvfi_insn,      rvfi_mode,      rvfi_rs1_addr,  rvfi_rs2_addr, rvfi_rs1_rdata,
        rvfi_rs2_rdata, rvfi_rd_addr,   rvfi_rd_wdata,  rvfi_pc_rdata, rvfi_pc_wdata,
        rvfi_mem_addr,  rvfi_mem_rdata, rvfi_mem_wdata,

        // Delay Registers
        // - inst
        RegFD_inst, RegDA_inst, RegAM_inst,
        // - rs1_addr/rs2_addr
        RegDA_rs1_addr, RegDA_rs2_addr, RegAM_rs1_addr, RegAM_rs2_addr,
        // - rs1_data/rs2_data
        RegDA_rs1_data, RegAM_rs1_data, RegAM_rs2_data,
        // - PC/nPC
        RegDA_PC, RegAM_PC, Fetch_nPC, RegFD_nPC, RegDA_nPC,
        RegAM_nPC,
        // - load_en/str_en/mem_addr
        RegAM_load_en, RegAM_str_en, RegAM_mem_addr
    ) foreach
    {
        x => x._1 := x._2
    }
}
