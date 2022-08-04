package RegFile

import chisel3._

class RegFile_IO extends Bundle
{
    // Input pins
    val rd_addr : UInt = Input(UInt(5.W))
    val rd_data : SInt = Input(SInt(32.W))
    val rs1_addr: UInt = Input(UInt(5.W))
    val rs2_addr: UInt = Input(UInt(5.W))
    val wr_en   : Bool = Input(Bool())

    // Output pins
    val rs1_data: SInt = Output(SInt(32.W))
    val rs2_data: SInt = Output(SInt(32.W))
}
class RegFile extends Module
{
    // Initializing IO pins
    val io: RegFile_IO = IO(new RegFile_IO())

    // Register file
    val regFile: Mem[SInt] = Mem(32, SInt(32.W))

    // Data is written when wr_en is high
    when (io.wr_en)
    {
        regFile.write(io.rd_addr, io.rd_data)
    }

    // Wiring to output pins
    io.rs1_data := Mux(io.rs1_addr === 0.U, 0.S, regFile.read(io.rs1_addr))
    io.rs2_data := Mux(io.rs2_addr === 0.U, 0.S, regFile.read(io.rs2_addr))
}
