package ExecuteStage

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
    val RegDA_rd_addr : UInt = Input(UInt(5.W))
    val rs1_addr      : UInt = Input(UInt(5.W))
    val rs2_addr      : UInt = Input(UInt(5.W))
    val b_en          : Bool = Input(Bool())
    val load_en       : Bool = Input(Bool())
    val RegAM_load_en : Bool = Input(Bool())
    val RegMW_load_en : Bool = Input(Bool())

    // Output pins
    val forward_operand1   : UInt = Output(UInt(2.W))
    val forward_operand2   : UInt = Output(UInt(2.W))
    val forward_br_operand1: UInt = Output(UInt(3.W))
    val forward_br_operand2: UInt = Output(UInt(3.W))
}
class ForwardUnit extends Module
{
    // Initializing IO pins
    val io            : ForwardUnit_IO = IO(new ForwardUnit_IO())
    val RegDA_rs1_addr: UInt           = dontTouch(WireInit(io.RegDA_rs1_addr))
    val RegDA_rs2_addr: UInt           = dontTouch(WireInit(io.RegDA_rs2_addr))
    val RegAM_rd_addr : UInt           = dontTouch(WireInit(io.RegAM_rd_addr))
    val RegAM_wr_en   : Bool           = dontTouch(WireInit(io.RegAM_wr_en))
    val RegMW_rd_addr : UInt           = dontTouch(WireInit(io.RegMW_rd_addr))
    val RegMW_wr_en   : Bool           = dontTouch(WireInit(io.RegMW_wr_en))
    val RegDA_rd_addr : UInt           = dontTouch(WireInit(io.RegDA_rd_addr))
    val rs1_addr      : UInt           = dontTouch(WireInit(io.rs1_addr))
    val rs2_addr      : UInt           = dontTouch(WireInit(io.rs2_addr))
    val b_en         : Bool           = dontTouch(WireInit(io.b_en))
    val load_en       : Bool           = dontTouch(WireInit(io.load_en))
    val RegAM_load_en : Bool           = dontTouch(WireInit(io.RegAM_load_en))
    val RegMW_load_en : Bool           = dontTouch(WireInit(io.RegMW_load_en))

    // ALU Hazard wires
    val ALU_br_hazard    : Bool = dontTouch(WireInit(b_en && RegDA_rd_addr =/= 0.U))
    val ALU_rs1_br_hazard: Bool = dontTouch(WireInit(ALU_br_hazard && RegDA_rd_addr === rs1_addr))
    val ALU_rs2_br_hazard: Bool = dontTouch(WireInit(ALU_br_hazard && RegDA_rd_addr === rs2_addr))

    // ALU-Memory Hazard wires
    // - Data Hazard
    val AM_hazard     : Bool = dontTouch(WireInit(RegAM_wr_en === 1.B && RegAM_rd_addr =/= 0.U))
    val AM_rs1_hazard : Bool = dontTouch(WireInit(AM_hazard && RegDA_rs1_addr === RegAM_rd_addr))
    val AM_rs2_hazard : Bool = dontTouch(WireInit(AM_hazard && RegDA_rs2_addr === RegAM_rd_addr))
    // - Branch Hazard
    val AM_br_hazard    : Bool = dontTouch(WireInit(b_en && RegAM_rd_addr =/= 0.U))
    val AM_rs1_br_hazard: Bool = dontTouch(WireInit(AM_br_hazard && RegAM_rd_addr === rs1_addr && !ALU_rs1_br_hazard))
    val AM_rs2_br_hazard: Bool = dontTouch(WireInit(AM_br_hazard && RegAM_rd_addr === rs2_addr && !ALU_rs2_br_hazard))

    // Memory-WriteBack Hazard wires
    // - Data Hazard
    val MW_hazard    : Bool = dontTouch(WireInit(RegMW_wr_en === 1.B && RegMW_rd_addr =/= 0.U))
    val MW_rs1_hazard: Bool = dontTouch(WireInit(MW_hazard && RegDA_rs1_addr === RegMW_rd_addr))
    val MW_rs2_hazard: Bool = dontTouch(WireInit(MW_hazard && RegDA_rs2_addr === RegMW_rd_addr))
    // - Branch Hazard
    val MW_br_hazard    : Bool = dontTouch(WireInit(b_en && RegMW_rd_addr =/= 0.U))
    val MW_rs1_br_hazard: Bool = dontTouch(WireInit(MW_br_hazard && RegMW_rd_addr === rs1_addr && !ALU_rs1_br_hazard && !AM_rs1_br_hazard))
    val MW_rs2_br_hazard: Bool = dontTouch(WireInit(MW_br_hazard && RegMW_rd_addr === rs2_addr && !ALU_rs2_br_hazard && !AM_rs2_br_hazard))

    // Intermediate wires
    val forward_operand1: UInt = dontTouch(WireInit(MuxCase(0.U, Seq(
        AM_rs1_hazard                        -> 1.U,
        (MW_rs1_hazard && !AM_rs1_hazard)    -> 2.U,
    ))))
    val forward_operand2: UInt = dontTouch(WireInit(MuxCase(0.U, Seq(
        (AM_rs2_hazard)                      -> 1.U,
        (MW_rs2_hazard && !AM_rs2_hazard)    -> 2.U,
        
    ))))
    val forward_br_operand1: UInt = dontTouch(WireInit(MuxCase(0.U, Seq(
        (ALU_rs1_br_hazard && !load_en)      -> 1.U,
        (AM_rs1_br_hazard && !RegAM_load_en) -> 2.U,
        /*(*/MW_rs1_br_hazard /*&& !RegMW_load_en)*/ -> 3.U,
        (AM_rs1_br_hazard && RegAM_load_en)  -> 4.U
        //(MW_rs1_br_hazard && RegMW_load_en)  -> 7.U
    ))))
    val forward_br_operand2: UInt = dontTouch(WireInit(MuxCase(0.U, Seq(
        (ALU_rs2_br_hazard && !load_en)      -> 1.U,
        (AM_rs2_br_hazard && !RegAM_load_en) -> 2.U,
        /*(*/MW_rs2_br_hazard /*&& !RegMW_load_en)*/ -> 3.U,
        (AM_rs2_br_hazard && RegAM_load_en)  -> 4.U
        //(MW_rs2_br_hazard && RegMW_load_en)  -> 7.U
    ))))

    // Wiring to output pins
    Seq(
        io.forward_operand1, io.forward_operand2, io.forward_br_operand1, io.forward_br_operand2
    ) zip Seq(
        forward_operand1,    forward_operand2,    forward_br_operand1,    forward_br_operand2
    ) foreach
    {
        x => x._1 := x._2
    }
}

