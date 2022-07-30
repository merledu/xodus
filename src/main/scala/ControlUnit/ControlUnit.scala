package ControlUnit

import chisel3._

class ControlUnit_IO extends Bundle
{
    // Input pins
    val opcode: UInt = Input(UInt(7.W))

    // Output pins
    val wr_en   : Bool = Output(Bool())
    val imm_en  : Bool = Output(Bool())
    val str_en  : Bool = Output(Bool())
    val ld_en   : Bool = Output(Bool())
    val br_en   : Bool = Output(Bool())
    val jal_en  : Bool = Output(Bool())
    val jalr_en : Bool = Output(Bool())
    val auipc_en: Bool = Output(Bool())
    val lui_en  : Bool = Output(Bool())
}
class ControlUnit extends Module
{
    // Initializing IO pins
    val io: ControlUnit_IO = IO(new ControlUnit_IO())

    // Input wires
    val opcode: UInt = dontTouch(WireInit(io.opcode))
    
    // Output wires
    val wr_en   : Bool = dontTouch(WireInit(
        opcode === 51.U || opcode === 3.U || opcode === 15.U || opcode === 19.U || opcode === 115.U || opcode === 103.U || opcode === 23.U || opcode === 55.U || opcode === 111.U
    ))
    val str_en  : Bool = dontTouch(WireInit(opcode === 35.U))
    val ld_en   : Bool = dontTouch(WireInit(opcode === 3.U))
    val br_en   : Bool = dontTouch(WireInit(opcode === 99.U))
    val jal_en  : Bool = dontTouch(WireInit(opcode === 111.U))
    val jalr_en : Bool = dontTouch(WireInit(opcode === 103.U))
    val auipc_en: Bool = dontTouch(WireInit(opcode === 23.U))
    val lui_en  : Bool = dontTouch(WireInit(opcode === 55.U))
    val imm_en  : Bool = dontTouch(WireInit(
        opcode === 15.U || opcode === 19.U || opcode === 115.U || opcode === 35.U || opcode === 3.U || opcode === 103.U
    ))

    // Wiring to outpin pins
    Array(
        io.wr_en,  io.imm_en,  io.str_en,   io.ld_en, io.br_en,
        io.jal_en, io.jalr_en, io.auipc_en, io.lui_en
    ) zip Array(
        wr_en,     imm_en,     str_en,      ld_en,    br_en,
        jal_en,    jalr_en,    auipc_en,    lui_en
    ) foreach
    {
        x => x._1 := x._2
    }
}
