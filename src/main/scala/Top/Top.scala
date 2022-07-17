package Top

import chisel3._
import Fetch.Fetch
import PipelineRegs._
import Decoder.Decoder

class Top extends Module
{
    // Initialzing modules
    val Fetch: Fetch = Module(new Fetch())
    val RegFD: RegFD = Module(new RegFD())
    val Decoder: Decoder = Module(new Decoder())

    /*******************************************************
                        WIRING THE MODULES
    *******************************************************/

    Array(
        // Fetch
        Fetch.io.nPC_in, Fetch.io.nPC_en,

        // RegFD
        RegFD.io.PC_in,  RegFD.io.inst_in,

        // Decoder
        Decoder.io.inst_in
    ) zip Array(
        // Fetch
        0.U,             0.B,  // Temporary values

        // RegFD
        Fetch.io.PC_out, Fetch.io.inst_out,
        
        // Decoder
        RegFD.io.inst_out
    ) foreach
    {
        x => x._1 := x._2
    }
}