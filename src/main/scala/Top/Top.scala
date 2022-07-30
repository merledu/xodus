package Top

import chisel3._
import Fetch.Fetch
import PipelineRegs._
import Decoder.Decoder
import RegFile.RegFile
import ControlUnit.ControlUnit
import ALU.ALU

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

    /*******************************************************
                        WIRING THE MODULES
    *******************************************************/

    Array(
        // Fetch
        Fetch.io.nPC_in,         Fetch.io.nPC_en,

        // RegFD
        RegFD.io.PC_in,          RegFD.io.inst_in,

        // Decoder
        Decoder.io.inst_in,

        // RegFile
        RegFile.io.rd_addr,      RegFile.io.rd_data,      RegFile.io.rs1_addr,   RegFile.io.rs2_addr,   RegFile.io.wr_en,

        // Control Unit
        ControlUnit.io.opcode, 

        // RegDA
        RegDA.io.PC_in,          RegDA.io.opcode_in,      RegDA.io.rd_addr_in,   RegDA.io.func3_in,     RegDA.io.rs1_data_in,
        RegDA.io.rs2_data_in,    RegDA.io.func7_in,       RegDA.io.i_s_b_imm_in, RegDA.io.u_j_imm_in,   RegDA.io.wr_en_in,
        RegDA.io.imm_en_in,      RegDA.io.str_en_in,      RegDA.io.ld_en_in,     RegDA.io.br_en_in,     RegDA.io.jal_en_in,
        RegDA.io.jalr_en_in,     RegDA.io.auipc_en_in,    RegDA.io.lui_en_in,

        // ALU
        ALU.io.opcode,           ALU.io.func3,            ALU.io.rs1_data,       ALU.io.rs2_data,       ALU.io.func7,
        ALU.io.imm,              ALU.io.imm_en
    ) zip Array(
        // Fetch
        0.U,                     0.B,  // Temporary values

        // RegFD
        Fetch.io.PC_out,         Fetch.io.inst_out,
        
        // Decoder
        RegFD.io.inst_out,

        // RegFile
        0.U,                     0.S, /*Temp values*/     Decoder.io.rs1_addr,   Decoder.io.rs2_addr,   1.B, // Temp value

        // Control Unit
        Decoder.io.opcode,

        // RegDA
        RegFD.io.PC_out,         Decoder.io.opcode,       Decoder.io.rd_addr,    Decoder.io.func3,      RegFile.io.rs1_data,
        RegFile.io.rs2_data,     Decoder.io.func7,        Decoder.io.i_s_b_imm,  Decoder.io.u_j_imm,    ControlUnit.io.wr_en,
        ControlUnit.io.imm_en,   ControlUnit.io.str_en,   ControlUnit.io.ld_en,  ControlUnit.io.br_en,  ControlUnit.io.jal_en,
        ControlUnit.io.jalr_en,  ControlUnit.io.auipc_en, ControlUnit.io.lui_en,

        // ALU
        RegDA.io.opcode_out,     RegDA.io.func3_out,      RegDA.io.rs1_data_out, RegDA.io.rs2_data_out, RegDA.io.func7_out,
        RegDA.io.i_s_b_imm_out,  RegDA.io.imm_en_out
    ) foreach
    {
        x => x._1 := x._2
    }
}

