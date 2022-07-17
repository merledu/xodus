package Decoder

import chisel3._

class Decoder_IO extends Bundle
{
    // Input pins
    val inst_in: UInt = Input(UInt(32.W))

    // Output pins
    val rd_addr : UInt = Output(UInt(5.W))
    val func3   : UInt = Output(UInt(3.W))
    val rs1_addr: UInt = Output(UInt(5.W))
    val rs2_addr: UInt = Output(UInt(5.W))
    val func7   : UInt = Output(UInt(7.W))
    val imm     : SInt = Output(SInt(32.W))
    val opcode  : UInt = Output(UInt(7.W))
}
class Decoder extends Module
{
    // Initializing IO pins and modules
    val io: Decoder_IO = IO(new Decoder_IO())
    val r : R_Type = Module(new R_Type())
    val i : I_Type = Module(new I_Type())
    val s : S_Type = Module(new S_Type())
    val sb: SB_Type = Module(new SB_Type())
    val u : U_Type = Module(new U_Type())
    val uj: UJ_Type = Module(new UJ_Type())

    // Input wires
    val inst_in: UInt = dontTouch(WireInit(io.inst_in))

    // Intermdiate wires
    val opcode: UInt = dontTouch(WireInit(inst_in(6, 0)))
    val inst  : UInt = dontTouch(WireInit(inst_in(31, 7)))

    // Output wires
    val rd_addr : UInt = dontTouch(WireInit(
        r.io.rd_addr | i.io.rd_addr | u.io.rd_addr | uj.io.rd_addr
    ))
    val func3   : UInt = dontTouch(WireInit(
        r.io.func3 | i.io.func3 | s.io.func3 | sb.io.func3
    ))
    val rs1_addr: UInt = dontTouch(WireInit(
        r.io.rs1_addr | i.io.rs1_addr | s.io.rs1_addr | sb.io.rs1_addr
    ))
    val rs2_addr: UInt = dontTouch(WireInit(
        r.io.rs2_addr | s.io.rs2_addr | sb.io.rs2_addr
    ))
    val func7   : UInt = dontTouch(WireInit(r.io.func7))
    val imm     : SInt = dontTouch(WireInit(
        i.io.imm | s.io.imm | sb.io.imm | u.io.imm | uj.io.imm
    ))

    // Wiring the instruction to type modules
    Array(
        r.io.in, i.io.in, s.io.in, sb.io.in, u.io.in, uj.io.in
    ) map ( _ := inst )
    
    // Wiring the opcode to type modules
    Array(
        r.io.opcode, i.io.opcode, s.io.opcode, sb.io.opcode, u.io.opcode, uj.io.opcode
    ) map ( _ := opcode )

    // Wiring to output pins
    Array(
        io.rd_addr, io.func3, io.rs1_addr, io.rs2_addr,
        io.func7,   io.imm,   io.opcode
    ) zip Array(
        rd_addr,    func3,    rs1_addr,    rs2_addr,
        func7,      imm,      opcode
    ) foreach
    {
        x => x._1 := x._2
    }
}
