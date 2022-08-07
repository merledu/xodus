package Top

import chisel3._
import Fetch._
import PipelineRegs._
import Decoder._
import RegFile._
import ControlUnit._
import ALU._
import Memory._
import WriteBack._

class Top extends Module
{
    // Initializing modules
    val Fetch      : Fetch       = Module(new Fetch)
    val RegFD      : RegFD       = Module(new RegFD)
    val Decoder    : Decoder     = Module(new Decoder)
    val RegFile    : RegFile     = Module(new RegFile)
    val StallUnit  : StallUnit   = Module(new StallUnit)
    val RegDA      : RegDA       = Module(new RegDA)
    val ControlUnit: ControlUnit = Module(new ControlUnit)
    val ALU        : ALU         = Module(new ALU)
    val ForwardUnit: ForwardUnit = Module(new ForwardUnit)
    val RegAM      : RegAM       = Module(new RegAM)
    val Memory     : Memory      = Module(new Memory)
    val RegMW      : RegMW       = Module(new RegMW)
    val WriteBack  : WriteBack   = Module(new WriteBack)

    /*******************************************************
                        WIRING THE MODULES
    *******************************************************/

    Array(
        // Fetch
        Fetch.io.nPC_in,     Fetch.io.nPC_en,       Fetch.io.forward_inst, Fetch.io.StallUnit_inst, Fetch.io.stallPC,
        Fetch.io.forward_PC, Fetch.io.StallUnit_PC,

        // RegFD
        RegFD.io.PC_in, RegFD.io.inst_in, RegFD.io.PC4_in,

        // Decoder
        Decoder.io.inst,

        // RegFile
        RegFile.io.rd_addr, RegFile.io.rd_data, RegFile.io.rs1_addr, RegFile.io.rs2_addr, RegFile.io.wr_en,

        // StallUnit
        StallUnit.io.RegFD_inst, StallUnit.io.load_en,  StallUnit.io.RegDA_rd_addr, StallUnit.io.PC_in, StallUnit.io.stallPC_in,
        StallUnit.io.rs1_addr,   StallUnit.io.rs2_addr,

        // RegDA
        RegDA.io.PC_in,            RegDA.io.opcode_in,        RegDA.io.rd_addr_in,  RegDA.io.func3_in,      RegDA.io.rs1_addr_in,
        RegDA.io.rs1_data_in,      RegDA.io.rs2_addr_in,      RegDA.io.rs2_data_in, RegDA.io.func7_in,      RegDA.io.imm_in,
        RegDA.io.forward_operand1, RegDA.io.forward_operand2, RegDA.io.PC4_in,      RegDA.io.RegAM_rd_data, RegDA.io.WriteBack_rd_data,
        RegDA.io.stallControl_in,

        // Control Unit
        ControlUnit.io.opcode,     ControlUnit.io.func3,     ControlUnit.io.func7,     ControlUnit.io.imm,          ControlUnit.io.r_id,
        ControlUnit.io.i_math_id,  ControlUnit.io.i_load_id, ControlUnit.io.i_jalr_id, ControlUnit.io.s_id,         ControlUnit.io.b_id,
        ControlUnit.io.u_auipc_id, ControlUnit.io.u_lui_id,  ControlUnit.io.j_id,      ControlUnit.io.stallControl,

        // ALU
        ALU.io.rs1_data,                ALU.io.rs2_data,            ALU.io.imm,                  ALU.io.imm_en,          ALU.io.addition_en,
        ALU.io.shiftLeftLogical_en,     ALU.io.lessThan_en,         ALU.io.lessThanU_en,         ALU.io.XOR_en,          ALU.io.shiftRightLogical_en,
        ALU.io.shiftRightArithmetic_en, ALU.io.OR_en,               ALU.io.AND_en,               ALU.io.subtraction_en,  ALU.io.equal_en,
        ALU.io.notEqual_en,             ALU.io.greaterThanEqual_en, ALU.io.greaterThanEqualU_en, ALU.io.jalrAddition_en,

        // ForwardUnit
        ForwardUnit.io.RegDA_rs1_addr, ForwardUnit.io.RegDA_rs2_addr, ForwardUnit.io.RegAM_rd_addr, ForwardUnit.io.RegAM_wr_en, ForwardUnit.io.RegMW_rd_addr,
        ForwardUnit.io.RegMW_wr_en,

        // RegAM
        RegAM.io.PC_in,       RegAM.io.alu_in,     RegAM.io.rd_addr_in, RegAM.io.imm_in,      RegAM.io.wr_en_in,
        RegAM.io.str_en_in,   RegAM.io.sb_en_in,   RegAM.io.sh_en_in,   RegAM.io.sw_en_in,    RegAM.io.load_en_in,
        RegAM.io.lb_en_in,    RegAM.io.lh_en_in,   RegAM.io.lw_en_in,   RegAM.io.lbu_en_in,   RegAM.io.lhu_en_in,
        RegAM.io.br_en_in,    RegAM.io.jalr_en_in, RegAM.io.jal_en_in,  RegAM.io.auipc_en_in, RegAM.io.lui_en_in,
        RegAM.io.rs2_data_in, RegAM.io.PC4_in,

        // Memory
        Memory.io.alu_in, Memory.io.rs2_data, Memory.io.str_en, Memory.io.load_en, Memory.io.sb_en,
        Memory.io.sh_en,  Memory.io.sw_en,    Memory.io.lb_en,  Memory.io.lh_en,   Memory.io.lw_en,
        Memory.io.lbu_en, Memory.io.lhu_en,

        // RegMW
        RegMW.io.PC_in,       RegMW.io.alu_in,     RegMW.io.mem_data_in, RegMW.io.rd_addr_in, RegMW.io.imm_in,
        RegMW.io.wr_en_in,    RegMW.io.br_en_in,   RegMW.io.jalr_en_in,  RegMW.io.jal_en_in,  RegMW.io.auipc_en_in,
        RegMW.io.lui_en_in,   RegMW.io.load_en_in, RegMW.io.PC4_in,

        // WriteBack
        WriteBack.io.PC,      WriteBack.io.alu,    WriteBack.io.mem_data, WriteBack.io.imm,    WriteBack.io.br_en,
        WriteBack.io.jalr_en, WriteBack.io.jal_en, WriteBack.io.auipc_en, WriteBack.io.lui_en, WriteBack.io.load_en,
        WriteBack.io.PC4
    ) zip Array(
        // Fetch
        WriteBack.io.nPC,        WriteBack.io.nPC_en, StallUnit.io.forward_inst, StallUnit.io.inst, StallUnit.io.stallPC_out,
        StallUnit.io.forward_PC, StallUnit.io.PC_out,

        // RegFD
        Fetch.io.PC_out, Fetch.io.inst_out, Fetch.io.PC4,
        
        // Decoder
        RegFD.io.inst_out,

        // RegFile
        RegMW.io.rd_addr_out, WriteBack.io.rd_data, Decoder.io.rs1_addr, Decoder.io.rs2_addr,  RegMW.io.wr_en_out,

        // StallUnit
        RegFD.io.inst_out,   ControlUnit.io.load_en, RegDA.io.rd_addr_out, RegFD.io.PC4_out, RegFD.io.PC_out,
        Decoder.io.rs1_addr, Decoder.io.rs2_addr,

        // RegDA
        RegFD.io.PC_out,                 Decoder.io.opcode,               Decoder.io.rd_addr,  Decoder.io.func3, Decoder.io.rs1_addr,
        RegFile.io.rs1_data,             Decoder.io.rs2_addr,             RegFile.io.rs2_data, Decoder.io.func7, Decoder.io.imm,
        ForwardUnit.io.forward_operand1, ForwardUnit.io.forward_operand2, RegFD.io.PC4_out,    RegAM.io.alu_out, WriteBack.io.rd_data,
        StallUnit.io.stallControl,

        // Control Unit
        RegDA.io.opcode_out,   RegDA.io.func3_out,   RegDA.io.func7_out,   RegDA.io.imm_out,          Decoder.io.r_id,
        Decoder.io.i_math_id,  Decoder.io.i_load_id, Decoder.io.i_jalr_id, Decoder.io.s_id,           Decoder.io.b_id,
        Decoder.io.u_auipc_id, Decoder.io.u_lui_id,  Decoder.io.j_id,      RegDA.io.stallControl_out,

        // ALU
        RegDA.io.rs1_data_out,                  RegDA.io.rs2_data_out,              RegDA.io.imm_out,                    ControlUnit.io.imm_en,          ControlUnit.io.addition_en,
        ControlUnit.io.shiftLeftLogical_en,     ControlUnit.io.lessThan_en,         ControlUnit.io.lessThanU_en,         ControlUnit.io.XOR_en,          ControlUnit.io.shiftRightLogical_en,
        ControlUnit.io.shiftRightArithmetic_en, ControlUnit.io.OR_en,               ControlUnit.io.AND_en,               ControlUnit.io.subtraction_en,  ControlUnit.io.equal_en,
        ControlUnit.io.notEqual_en,             ControlUnit.io.greaterThanEqual_en, ControlUnit.io.greaterThanEqualU_en, ControlUnit.io.jalrAddition_en,

        // ForwardUnit
        RegDA.io.rs1_addr_out, RegDA.io.rs2_addr_out, RegAM.io.rd_addr_out, RegAM.io.wr_en_out, RegMW.io.rd_addr_out,
        RegMW.io.wr_en_out,

        // RegAM
        RegDA.io.PC_out,       ALU.io.out,             RegDA.io.rd_addr_out, RegDA.io.imm_out,        ControlUnit.io.wr_en,
        ControlUnit.io.str_en, ControlUnit.io.sb_en,   ControlUnit.io.sh_en, ControlUnit.io.sw_en,    ControlUnit.io.load_en,
        ControlUnit.io.lb_en,  ControlUnit.io.lh_en,   ControlUnit.io.lw_en, ControlUnit.io.lbu_en,   ControlUnit.io.lhu_en,
        ControlUnit.io.br_en,  ControlUnit.io.jalr_en, ControlUnit.io.j_en,  ControlUnit.io.auipc_en, ControlUnit.io.lui_en,
        RegDA.io.rs2_data_out, RegDA.io.PC4_out,

        // Memory
        RegAM.io.alu_out,    RegAM.io.rs2_data_out, RegAM.io.str_en_out, RegAM.io.load_en_out, RegAM.io.sb_en_out,
        RegAM.io.sh_en_out,  RegAM.io.sw_en_out,    RegAM.io.lb_en_out,  RegAM.io.lh_en_out,   RegAM.io.lw_en_out,
        RegAM.io.lbu_en_out, RegAM.io.lhu_en_out,

        // RegMW
        RegAM.io.PC_out,     RegAM.io.alu_out,     Memory.io.out,        RegAM.io.rd_addr_out,   RegAM.io.imm_out,
        RegAM.io.wr_en_out,  RegAM.io.br_en_out,   RegAM.io.jalr_en_out, RegAM.io.jal_en_out, RegAM.io.auipc_en_out,
        RegAM.io.lui_en_out, RegAM.io.load_en_out, RegAM.io.PC4_out,

        // WriteBack
        RegMW.io.PC_out,      RegMW.io.alu_out,    RegMW.io.mem_data_out, RegMW.io.imm_out,    RegMW.io.br_en_out,
        RegMW.io.jalr_en_out, RegMW.io.jal_en_out, RegMW.io.auipc_en_out, RegMW.io.lui_en_out, RegMW.io.load_en_out,
        RegMW.io.PC4_out
    ) foreach
    {
        x => x._1 := x._2
    }
}

