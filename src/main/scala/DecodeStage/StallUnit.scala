package DecodeStage

import chisel3._

class StallUnit_IO extends Bundle {
    // Input pins
    val RegFD_inst   : UInt = Input(UInt(32.W))
    val load_en      : Bool = Input(Bool())
    val RegDA_rd_addr: UInt = Input(UInt(5.W))
    val PC_in        : UInt = Input(UInt(32.W))
    val stallPC_in   : UInt = Input(UInt(32.W))
    val rs1_addr     : UInt = Input(UInt(5.W))
    val rs2_addr     : UInt = Input(UInt(5.W))

    // Output pins
    val forward_inst: Bool = Output(Bool())
    val forward_PC  : Bool = Output(Bool())
    val stallControl: Bool = Output(Bool())
    val inst        : UInt = Output(UInt(32.W))
    val PC_out      : UInt = Output(UInt(32.W))
    val stallPC_out : UInt = Output(UInt(32.W))
}

class StallUnit extends Module {
    // Initializing IO pins
    val io           : StallUnit_IO = IO(new StallUnit_IO)
    val RegFD_inst   : UInt         = dontTouch(WireInit(io.RegFD_inst))
    val load_en      : Bool         = dontTouch(WireInit(io.load_en))
    val RegDA_rd_addr: UInt         = dontTouch(WireInit(io.RegDA_rd_addr))
    val PC_in        : UInt         = dontTouch(WireInit(io.PC_in))
    val stallPC_in   : UInt         = dontTouch(WireInit(io.stallPC_in))
    val rs1_addr     : UInt         = dontTouch(WireInit(io.rs1_addr))
    val rs2_addr     : UInt         = dontTouch(WireInit(io.rs2_addr))

    // Intermediate wires
    val rs1_Hazard: Bool = dontTouch(WireInit(RegDA_rd_addr === rs1_addr))
    val rs2_Hazard: Bool = dontTouch(WireInit(RegDA_rd_addr === rs2_addr))
    val loadHazard: Bool = dontTouch(WireInit(load_en && (rs1_Hazard || rs2_Hazard)))

    // Wiring to output pins
    Seq(
        io.forward_inst, io.forward_PC, io.stallControl
    ) map (_ := Mux(loadHazard, 1.B, 0.B))
    Seq(
        io.inst,    io.PC_out, io.stallPC_out
    ) zip Seq(
        RegFD_inst, PC_in,     stallPC_in
    ) foreach {
        x => x._1 := x._2
    }
}
