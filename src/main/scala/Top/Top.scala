package Top

import chisel3._
import Fetch.Fetch
import PipelineRegs._
import Decoder.Decoder
import RegFile.RegFile
import ControlUnit._
import ALU.ALU
import Memory.Memory
import WriteBack.WriteBack

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
    val WriteBack  : WriteBack   = Module(new WriteBack())
    val ForwardUnit: ForwardUnit = Module(new ForwardUnit())

    /*******************************************************
                        WIRING THE MODULES
    *******************************************************/

    Array(
        // Fetch
        Fetch.io.nPC_in,                      Fetch.io.nPC_en,

        // RegFD
        RegFD.io.PC_in,                       RegFD.io.inst_in,

        // Decoder
        Decoder.io.inst,

        // RegFile
        RegFile.io.rd_addr,                   RegFile.io.rd_data,                  RegFile.io.rs1_addr,               RegFile.io.rs2_addr,                 RegFile.io.wr_en,

        // Control Unit
        ControlUnit.io.opcode,                ControlUnit.io.func3,                ControlUnit.io.func7,              ControlUnit.io.i_s_b_imm,

    ) zip Array(
        // Fetch
        WriteBack.io.nPC,                     WriteBack.io.nPC_en,

        // RegFD
        Fetch.io.PC_out,                      Fetch.io.inst_out,
        
        // Decoder
        RegFD.io.inst_out,

        // RegFile
        RegMW.io.rd_addr_out,                 WriteBack.io.rd_data,                Decoder.io.rs1_addr,               Decoder.io.rs2_addr,                 RegMW.io.wr_en_out,

        // Control Unit
        Decoder.io.opcode,                    Decoder.io.func3,                    Decoder.io.func7,                  Decoder.io.i_s_b_imm,

    ) foreach
    {
        x => x._1 := x._2
    }
}

