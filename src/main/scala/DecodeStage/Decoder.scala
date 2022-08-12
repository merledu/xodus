package DecodeStage

import chisel3._
import chisel3.util._

class Decoder_IO extends Bundle
{
    // Input pins
    val inst: UInt = Input(UInt(32.W))

    // Output pins
    val opcode    : UInt = Output(UInt(7.W))
    val rd_addr   : UInt = Output(UInt(5.W))
    val func3     : UInt = Output(UInt(3.W))
    val rs1_addr  : UInt = Output(UInt(5.W))
    val rs2_addr  : UInt = Output(UInt(5.W))
    val func7     : UInt = Output(UInt(7.W))
    val imm       : SInt = Output(SInt(32.W))
    val r_id      : UInt = Output(UInt(7.W))
    val i_math_id : UInt = Output(UInt(7.W))
    val i_load_id : UInt = Output(UInt(7.W))
    val i_jalr_id : UInt = Output(UInt(10.W))
    val s_id      : UInt = Output(UInt(7.W))
    val b_id      : UInt = Output(UInt(7.W))
    val u_auipc_id: UInt = Output(UInt(7.W))
    val u_lui_id  : UInt = Output(UInt(7.W))
    val j_id      : UInt = Output(UInt(7.W))
}
class Decoder extends Module
{
    // Initializing IO pins and modules
    val io  : Decoder_IO = IO(new Decoder_IO())
    val inst: UInt       = dontTouch(WireInit(io.inst))

    // Type IDs
    val r_id      : UInt = dontTouch(WireInit(51.U))
    val i_math_id : UInt = dontTouch(WireInit(19.U))
    val i_load_id : UInt = dontTouch(WireInit(3.U))
    val i_fence_id: UInt = dontTouch(WireInit(15.U))
    val i_jalr_id : UInt = dontTouch(WireInit(103.U))
    val i_call_id : UInt = dontTouch(WireInit(115.U))
    val s_id      : UInt = dontTouch(WireInit(35.U))
    val b_id      : UInt = dontTouch(WireInit(99.U))
    val u_auipc_id: UInt = dontTouch(WireInit(23.U))
    val u_lui_id  : UInt = dontTouch(WireInit(55.U))
    val j_id      : UInt = dontTouch(WireInit(111.U))

    // Intermediate wires
    val opcode  : UInt = dontTouch(WireInit(inst(6, 0)))
    val rd_addr : UInt = dontTouch(WireInit(Mux(
        opcode === r_id || opcode === i_math_id || opcode === i_load_id || opcode === i_fence_id || opcode === i_jalr_id || opcode === i_call_id || opcode === u_auipc_id || opcode === u_lui_id || opcode === j_id,
        inst(11, 7),
        0.U
    )))
    val func3   : UInt = dontTouch(WireInit(Mux(
        opcode === r_id || opcode === i_math_id || opcode === i_load_id || opcode === i_fence_id || opcode === i_jalr_id || opcode === i_call_id || opcode === s_id || opcode === b_id,
        inst(14, 12),
        0.U
    )))
    val rs1_addr: UInt = dontTouch(WireInit(Mux(
        opcode === r_id || opcode === i_math_id || opcode === i_load_id || opcode === i_fence_id || opcode === i_jalr_id || opcode === i_call_id || opcode === s_id || opcode === b_id,
        inst(19, 15),
        0.U
    )))
    val rs2_addr: UInt = dontTouch(WireInit(Mux(
        opcode === r_id || opcode === s_id || opcode === b_id,
        inst(24, 20),
        0.U
    )))
    val func7   : UInt = dontTouch(WireInit(Mux(opcode === r_id, inst(31, 25), 0.U)))

    // Immediate generation
    val i_imm: SInt = dontTouch(WireInit(Mux(opcode === i_math_id || opcode === i_load_id || opcode === i_fence_id || opcode === i_jalr_id || opcode === i_call_id, inst(31, 20).asSInt, 0.S)))
    val s_imm: SInt = dontTouch(WireInit(Mux(opcode === s_id, Cat(inst(31, 25), inst(11, 7)).asSInt, 0.S)))
    val b_imm: SInt = dontTouch(WireInit(Mux(opcode === b_id, Cat(inst(31), inst(7), inst(30, 25), inst(11, 8), "b0".U).asSInt, 0.S)))
    val u_imm: SInt = dontTouch(WireInit(Mux(opcode === u_auipc_id || opcode === u_lui_id, inst(31, 12).asSInt, 0.S)))
    val j_imm: SInt = dontTouch(WireInit(Mux(opcode === j_id, Cat(inst(31), inst(19, 12), inst(20), inst(30, 21), "b0".U).asSInt, 0.S)))
    val imm  : SInt = dontTouch(WireInit(i_imm | s_imm | b_imm | u_imm | j_imm))

    // Wiring to output pins
    Seq(
        io.opcode,    io.rd_addr, io.func3, io.rs1_addr,   io.rs2_addr,
        io.func7,     io.imm,     io.r_id,  io.i_math_id,  io.i_load_id,
        io.i_jalr_id, io.s_id,    io.b_id,  io.u_auipc_id, io.u_lui_id,
        io.j_id
    ) zip Seq(
        opcode,       rd_addr,    func3,    rs1_addr,      rs2_addr,
        func7,        imm,        r_id,     i_math_id,     i_load_id,
        i_jalr_id,    s_id,       b_id,     u_auipc_id,    u_lui_id,
        j_id
    ) foreach
    {
        x => x._1 := x._2
    }
}

