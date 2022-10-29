package WriteBackStage

import chisel3._
import chisel3.util._

class WriteBack_IO extends Bundle
{
    // Input pins
    val alu     : SInt = Input(SInt(32.W))
    val mem_data: SInt = Input(SInt(32.W))
    val load_en : Bool = Input(Bool())

    // Output pins
    val rd_data: SInt = Output(SInt(32.W))
}
class WriteBack extends Module
{
    // Initializing IO pins
    val io      : WriteBack_IO = IO(new WriteBack_IO)
    val alu     : SInt         = dontTouch(WireInit(io.alu))
    val mem_data: SInt         = dontTouch(WireInit(io.mem_data))
    val load_en : Bool         = dontTouch(WireInit(io.load_en))

    // Intermediate wires
    val rd_data: SInt = dontTouch(WireInit(Mux(load_en, mem_data, alu)))

    // Wiring to output pins
    io.rd_data := rd_data
}

