package Top

import chisel3._
import Fetch.Fetch
import PipelineRegs._
import Decoder.Decoder
import RegFile.RegFile
import ControlUnit.ControlUnit
import ALU.ALU
import Memory.Memory

class Top extends Module
{
    // Initializing modules
    val Fetch      : Fetch       = Module(new Fetch())
    val RegFD      : RegFD       = Module(new RegFD())
    val Decoder    : Decoder     = Module(new Decoder())
    val RegFile    : RegFile     = Module(new RegFile())
    val ControlUnit: ControlUnit = Module(new ControlUnit())
    val RegDA      : RegDA       = Module(new RegDA())
    val ALU        : ALU         = Module(new ALU())
    val RegAM      : RegAM       = Module(new RegAM())
    val Memory     : Memory      = Module(new Memory())
    val RegMW      : RegMW       = Module(new RegMW())

    /*******************************************************
                        WIRING THE MODULES
    *******************************************************/

    Array(
        // Fetch
        Fetch.io.nPC_in,                      Fetch.io.nPC_en,

        // RegFD
        RegFD.io.PC_in,                       RegFD.io.inst_in,

        // Decoder
        Decoder.io.inst_in,

        // RegFile
        RegFile.io.rd_addr,                   RegFile.io.rd_data,                  RegFile.io.rs1_addr,               RegFile.io.rs2_addr,                 RegFile.io.wr_en,

        // Control Unit
        ControlUnit.io.opcode,                ControlUnit.io.func3,                ControlUnit.io.func7,              ControlUnit.io.i_s_b_imm,

        // RegDA
        RegDA.io.PC_in,                       RegDA.io.opcode_in,                  RegDA.io.rd_addr_in,               RegDA.io.func3_in,                   RegDA.io.rs1_data_in,
        RegDA.io.rs2_data_in,                 RegDA.io.func7_in,                   RegDA.io.i_s_b_imm_in,             RegDA.io.u_j_imm_in,                 RegDA.io.wr_en_in,
        RegDA.io.imm_en_in,                   RegDA.io.str_en_in,                  RegDA.io.ld_en_in,                 RegDA.io.br_en_in,                   RegDA.io.jal_en_in,
        RegDA.io.jalr_en_in,                  RegDA.io.auipc_en_in,                RegDA.io.lui_en_in,                RegDA.io.addition_en_in,             RegDA.io.shiftLeftLogical_en_in,
        RegDA.io.lessThan_en_in,              RegDA.io.lessThanU_en_in,            RegDA.io.XOR_en_in,                RegDA.io.shiftRightLogical_en_in,    RegDA.io.shiftRightArithmetic_en_in,
        RegDA.io.OR_en_in,                    RegDA.io.AND_en_in,                  RegDA.io.subtraction_en_in,        RegDA.io.equal_en_in,                RegDA.io.notEqual_en_in,
        RegDA.io.greaterThanEqual_en_in,      RegDA.io.greaterThanEqualU_en_in,    RegDA.io.jalrAddition_en_in,       RegDA.io.auipcAddition_en_in,        RegDA.io.luiAddition_en_in,
        RegDA.io.jalAddition_en_in,           RegDA.io.sb_en_in,                   RegDA.io.sh_en_in,                 RegDA.io.sw_en_in,                   RegDA.io.lb_en_in,
        RegDA.io.lh_en_in,                    RegDA.io.lw_en_in,                   RegDA.io.lbu_en_in,                RegDA.io.lhu_en_in,

        // ALU
        ALU.io.rs1_data,                      ALU.io.rs2_data,                     ALU.io.i_s_b_imm,                  ALU.io.imm_en,                       ALU.io.addition_en,
        ALU.io.shiftLeftLogical_en,           ALU.io.lessThan_en,                  ALU.io.lessThanU_en,               ALU.io.XOR_en,                       ALU.io.shiftRightLogical_en,
        ALU.io.shiftRightArithmetic_en,       ALU.io.OR_en,                        ALU.io.AND_en,                     ALU.io.subtraction_en,               ALU.io.equal_en,
        ALU.io.notEqual_en,                   ALU.io.greaterThanEqual_en,          ALU.io.greaterThanEqualU_en,       ALU.io.jalrAddition_en,

        // RegAM
        RegAM.io.PC_in,                       RegAM.io.alu_in,                     RegAM.io.rd_addr_in,               RegAM.io.rs2_data_in,                RegAM.io.i_s_b_imm_in,
        RegAM.io.u_j_imm_in,                  RegAM.io.wr_en_in,                   RegAM.io.str_en_in,                RegAM.io.ld_en_in,                   RegAM.io.br_en_in,
        RegAM.io.jalr_en_in,                  RegAM.io.jal_en_in,                  RegAM.io.auipc_en_in,              RegAM.io.lui_en_in,                  RegAM.io.sb_en_in,
        RegAM.io.sh_en_in,                    RegAM.io.sw_en_in,                   RegAM.io.lb_en_in,                 RegAM.io.lh_en_in,                   RegAM.io.lw_en_in,
        RegAM.io.lbu_en_in,                   RegAM.io.lhu_en_in,

        // Memory
        Memory.io.alu_in,                     Memory.io.rs2_data,                  Memory.io.str_en,                  Memory.io.ld_en,                     Memory.io.sb_en,
        Memory.io.sh_en,                      Memory.io.sw_en,                     Memory.io.lb_en,                   Memory.io.lh_en,                     Memory.io.lw_en,
        Memory.io.lbu_en,                     Memory.io.lhu_en,

        // RegMW
        RegMW.io.PC_in,                       RegMW.io.alu_in,                     RegMW.io.mem_data_in,              RegMW.io.rd_addr_in,                 RegMW.io.i_s_b_imm_in,
        RegMW.io.u_j_imm_in,                  RegMW.io.wr_en_in,                   RegMW.io.br_en_in,                 RegMW.io.jalr_en_in,                 RegMW.io.jal_en_in,
        RegMW.io.auipc_en_in,                 RegMW.io.lui_en_in
    ) zip Array(
        // Fetch
        0.U,                                  0.B,  // Temporary values

        // RegFD
        Fetch.io.PC_out,                      Fetch.io.inst_out,
        
        // Decoder
        RegFD.io.inst_out,

        // RegFile
        0.U,                                  0.S, /*Temp values*/                 Decoder.io.rs1_addr,               Decoder.io.rs2_addr,                 1.B, // Temp value

        // Control Unit
        Decoder.io.opcode,                    Decoder.io.func3,                    Decoder.io.func7,                  Decoder.io.i_s_b_imm,

        // RegDA
        RegFD.io.PC_out,                      Decoder.io.opcode,                   Decoder.io.rd_addr,                Decoder.io.func3,                    RegFile.io.rs1_data,
        RegFile.io.rs2_data,                  Decoder.io.func7,                    Decoder.io.i_s_b_imm,              Decoder.io.u_j_imm,                  ControlUnit.io.wr_en,
        ControlUnit.io.imm_en,                ControlUnit.io.str_en,               ControlUnit.io.ld_en,              ControlUnit.io.br_en,                ControlUnit.io.jal_en,
        ControlUnit.io.jalr_en,               ControlUnit.io.auipc_en,             ControlUnit.io.lui_en,             ControlUnit.io.addition_en,          ControlUnit.io.shiftLeftLogical_en,
        ControlUnit.io.lessThan_en,           ControlUnit.io.lessThanU_en,         ControlUnit.io.XOR_en,             ControlUnit.io.shiftRightLogical_en, ControlUnit.io.shiftRightArithmetic_en,
        ControlUnit.io.OR_en,                 ControlUnit.io.AND_en,               ControlUnit.io.subtraction_en,     ControlUnit.io.equal_en,             ControlUnit.io.notEqual_en,
        ControlUnit.io.greaterThanEqual_en,   ControlUnit.io.greaterThanEqualU_en, ControlUnit.io.jalrAddition_en,    ControlUnit.io.auipcAddition_en,     ControlUnit.io.luiAddition_en,
        ControlUnit.io.jalAddition_en,        ControlUnit.io.sb_en,                ControlUnit.io.sh_en,              ControlUnit.io.sw_en,                ControlUnit.io.lb_en,
        ControlUnit.io.lh_en,                 ControlUnit.io.lw_en,                ControlUnit.io.lbu_en,             ControlUnit.io.lhu_en,

        // ALU
        RegDA.io.rs1_data_out,                RegDA.io.rs2_data_out,               RegDA.io.i_s_b_imm_out,            RegDA.io.imm_en_out,                 RegDA.io.addition_en_out,
        RegDA.io.shiftLeftLogical_en_out,     RegDA.io.lessThan_en_out,            RegDA.io.lessThanU_en_out,         RegDA.io.XOR_en_out,                 RegDA.io.shiftRightLogical_en_out,
        RegDA.io.shiftRightArithmetic_en_out, RegDA.io.OR_en_out,                  RegDA.io.AND_en_out,               RegDA.io.subtraction_en_out,         RegDA.io.equal_en_out,
        RegDA.io.notEqual_en_out,             RegDA.io.greaterThanEqual_en_out,    RegDA.io.greaterThanEqualU_en_out, RegDA.io.jalrAddition_en_out,

        // RegAM
        RegDA.io.PC_out,                      ALU.io.out,                          RegDA.io.rd_addr_out,              RegDA.io.rs2_data_out,               RegDA.io.i_s_b_imm_out,
        RegDA.io.u_j_imm_out,                 RegDA.io.wr_en_out,                  RegDA.io.str_en_out,               RegDA.io.ld_en_out,                  RegDA.io.br_en_out,
        RegDA.io.jalr_en_out,                 RegDA.io.jal_en_out,                 RegDA.io.auipc_en_out,             RegDA.io.lui_en_out,                 RegDA.io.sb_en_out,
        RegDA.io.sh_en_out,                   RegDA.io.sw_en_out,                  RegDA.io.lb_en_out,                RegDA.io.lh_en_out,                  RegDA.io.lw_en_out,
        RegDA.io.lbu_en_out,                  RegDA.io.lhu_en_out,

        // Memory
        RegAM.io.alu_out,                     RegAM.io.rs2_data_out,               RegAM.io.str_en_out,               RegAM.io.ld_en_out,                  RegAM.io.sb_en_out,
        RegAM.io.sh_en_out,                   RegAM.io.sw_en_out,                  RegAM.io.lb_en_out,                RegAM.io.lh_en_out,                  RegAM.io.lw_en_out,
        RegAM.io.lbu_en_out,                  RegAM.io.lhu_en_out,

        // RegMW
        RegAM.io.PC_out,                      RegAM.io.alu_out,                    Memory.io.out,                     RegAM.io.rd_addr_out,                RegAM.io.i_s_b_imm_out,
        RegAM.io.u_j_imm_out,                 RegAM.io.wr_en_out,                  RegAM.io.br_en_out,                RegAM.io.jalr_en_out,                RegAM.io.jal_en_out,
        RegAM.io.auipc_en_out,                RegAM.io.lui_en_out
    ) foreach
    {
        x => x._1 := x._2
    }
}

