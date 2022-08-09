package DecodeStage

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
    val io      : RegFile_IO = IO(new RegFile_IO)
    val rd_addr : UInt       = dontTouch(WireInit(io.rd_addr))
    val rd_data : SInt       = dontTouch(WireInit(io.rd_data))
    val rs1_addr: UInt       = dontTouch(WireInit(io.rs1_addr))
    val rs2_addr: UInt       = dontTouch(WireInit(io.rs2_addr))
    val wr_en   : Bool       = dontTouch(WireInit(io.wr_en))

    // Register file
    val regFile: Mem[SInt] = Mem(32, SInt(32.W))

    // Data is written when wr_en is high
    when (wr_en)
    {
        regFile.write(rd_addr, rd_data)
    }

    // Intermediate wires
    val rs1_data: SInt = dontTouch(WireInit(regFile.read(rs1_addr)))
    val rs2_data: SInt = dontTouch(WireInit(regFile.read(rs2_addr)))

    // Wiring to output pins
    Seq(
        io.rs1_data,         io.rs2_data
    ) zip Seq(
        (rs1_addr,rs1_data), (rs2_addr, rs2_data)
    ) foreach
    {
        x => x._1 := Mux(x._2._1 === 0.U, 0.S, x._2._2)
    }
}

