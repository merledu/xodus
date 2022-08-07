package ControlUnit

import chisel3._
import chisel3.util._

class ForwardUnit_IO extends Bundle
{
    // Input pins
    val RegDA_rs1_addr: UInt = Input(UInt(5.W))
    val RegDA_rs2_addr: UInt = Input(UInt(5.W))
    val RegAM_rd_addr : UInt = Input(UInt(5.W))
    val RegAM_wr_en   : Bool = Input(Bool())
    val RegMW_rd_addr : UInt = Input(UInt(5.W))
    val RegMW_wr_en   : Bool = Input(Bool())

    // Output pins
    val forward_operand1: UInt = Output(UInt(2.W))
    val forward_operand2: UInt = Output(UInt(2.W))
}
class ForwardUnit extends Module
{
    // Initializing IO pins
    val io: ForwardUnit_IO = IO(new ForwardUnit_IO())
    val RegDA_rs1_addr: UInt = dontTouch(WireInit(io.RegDA_rs1_addr))
    val RegDA_rs2_addr: UInt = dontTouch(WireInit(io.RegDA_rs2_addr))
    val RegAM_rd_addr : UInt = dontTouch(WireInit(io.RegAM_rd_addr))
    val RegAM_wr_en   : Bool = dontTouch(WireInit(io.RegAM_wr_en))
    val RegMW_rd_addr : UInt = dontTouch(WireInit(io.RegMW_rd_addr))
    val RegMW_wr_en   : Bool = dontTouch(WireInit(io.RegMW_wr_en))

    // ALU-Memory Hazard wires
    val AM_hazard    : Bool = dontTouch(WireInit(RegAM_wr_en === 1.B && RegAM_rd_addr =/= 0.U))
    val AM_rs1_hazard: Bool = dontTouch(WireInit(RegDA_rs1_addr === RegAM_rd_addr))
    val AM_rs2_hazard: Bool = dontTouch(WireInit(RegDA_rs2_addr === RegAM_rd_addr))

    // Memory-WriteBack Hazard wires
    val MW_hazard    : Bool = dontTouch(WireInit(RegMW_wr_en === 1.B && RegMW_rd_addr =/= 0.U))
    val MW_rs1_hazard: Bool = dontTouch(WireInit(RegDA_rs1_addr === RegMW_rd_addr))
    val MW_rs2_hazard: Bool = dontTouch(WireInit(RegDA_rs2_addr === RegMW_rd_addr))
    
    // Intermediate wires
    val forward_operand1: UInt = dontTouch(WireInit(MuxCase(0.U, Array(
        (AM_hazard && AM_rs1_hazard)                                  -> 1.U,
        (MW_hazard && MW_rs1_hazard && !(AM_hazard && AM_rs1_hazard)) -> 2.U
    ))))
    val forward_operand2: UInt = dontTouch(WireInit(MuxCase(0.U, Array(
        (AM_hazard && AM_rs2_hazard)                                  -> 1.U,
        (MW_hazard && MW_rs2_hazard && !(AM_hazard && AM_rs2_hazard)) -> 2.U
    ))))

    // Wiring to output pins
    Array(
        io.forward_operand1, io.forward_operand2
    ) zip Array(
        forward_operand1,    forward_operand2
    ) foreach
    {
        x => x._1 := x._2
    }
}

