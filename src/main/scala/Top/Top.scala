package Top

import chisel3._
import Fetch.Fetch
import PipelineRegs._
import Decoder.Decoder
import RegFile.RegFile
import ControlUnit.ControlUnit

class Top extends Module
{
    // Initialzing modules
    val Fetch      : Fetch = Module(new Fetch())
    val RegFD      : RegFD = Module(new RegFD())
    val Decoder    : Decoder = Module(new Decoder())
    val RegFile    : RegFile = Module(new RegFile())
    val ControlUnit: ControlUnit = Module(new ControlUnit())

    /*******************************************************
                        WIRING THE MODULES
    *******************************************************/

    Array(
        // Fetch
        Fetch.io.nPC_in,      Fetch.io.nPC_en,

        // RegFD
        RegFD.io.PC_in,       RegFD.io.inst_in,

        // Decoder
        Decoder.io.inst_in,

        // RegFile
        RegFile.io.rd_addr,   RegFile.io.rd_data, RegFile.io.rs1_addr, RegFile.io.rs2_addr, RegFile.io.wr_en,

        // Control Unit
        ControlUnit.io.opcode
    ) zip Array(
        // Fetch
        0.U,                0.B,  // Temporary values

        // RegFD
        Fetch.io.PC_out,    Fetch.io.inst_out,
        
        // Decoder
        RegFD.io.inst_out,

        // RegFile
        Decoder.io.rd_addr, 0.S, /*Temp value*/ Decoder.io.rs1_addr, Decoder.io.rs2_addr, 1.B, // Temp value

        // Control Unit
        Decoder.io.opcode
    ) foreach
    {
        x => x._1 := x._2
    }
}
