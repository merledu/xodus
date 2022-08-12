package DecodeStage

import chisel3._
import chisel3.util._

class JumpUnit_IO extends Bundle
{
    // Input pins
    val rs1_data             : SInt = Input(SInt(32.W))
    val rs2_data             : SInt = Input(SInt(32.W))
    val alu                  : SInt = Input(SInt(32.W))
    val RegAM_alu_out        : SInt = Input(SInt(32.W))
    val WriteBack_rd_data    : SInt = Input(SInt(32.W))
    val Memory_out           : SInt = Input(SInt(32.W))
    val func3                : UInt = Input(UInt(3.W))
    val b_id                 : UInt = Input(UInt(7.W))
    val j_id                 : UInt = Input(UInt(7.W))
    val i_jalr_id            : UInt = Input(UInt(10.W))
    val opcode               : UInt = Input(UInt(7.W))
    val forward_jump_operand1: UInt = Input(UInt(3.W))
    val forward_jump_operand2: UInt = Input(UInt(3.W))
    val imm                  : SInt = Input(SInt(32.W))

    // Output pins
    val br_en  : Bool = Output(Bool())
    val b_en   : Bool = Output(Bool())
    val jal_en : Bool = Output(Bool())
    val jalr_en: Bool = Output(Bool())
    val jalr_PC: UInt = Output(UInt(32.W))
}
class JumpUnit extends Module
{
    // Initializing IO pins
    val io                   : JumpUnit_IO = IO(new JumpUnit_IO)
    val rs1_data             : SInt          = dontTouch(WireInit(io.rs1_data))
    val rs2_data             : SInt          = dontTouch(WireInit(io.rs2_data))
    val alu                  : SInt          = dontTouch(WireInit(io.alu))
    val RegAM_alu_out        : SInt          = dontTouch(WireInit(io.RegAM_alu_out))
    val WriteBack_rd_data    : SInt          = dontTouch(WireInit(io.WriteBack_rd_data))
    val Memory_out           : SInt          = dontTouch(WireInit(io.Memory_out))
    val func3                : UInt          = dontTouch(WireInit(io.func3))
    val b_id                 : UInt          = dontTouch(WireInit(io.b_id))
    val j_id                 : UInt          = dontTouch(WireInit(io.j_id))
    val i_jalr_id            : UInt          = dontTouch(WireInit(io.i_jalr_id))
    val opcode               : UInt          = dontTouch(WireInit(io.opcode))
    val forward_jump_operand1: UInt          = dontTouch(WireInit(io.forward_jump_operand1))
    val forward_jump_operand2: UInt          = dontTouch(WireInit(io.forward_jump_operand2))
    val imm                  : SInt          = dontTouch(WireInit(io.imm))

    // B-Type IDs
    val beq_id : UInt = dontTouch(WireInit(99.U(10.W)))
    val bne_id : UInt = dontTouch(WireInit(227.U(10.W)))
    val blt_id : UInt = dontTouch(WireInit(611.U(10.W)))
    val bge_id : UInt = dontTouch(WireInit(739.U(10.W)))
    val bltu_id: UInt = dontTouch(WireInit(867.U(10.W)))
    val bgeu_id: UInt = dontTouch(WireInit(995.U(10.W)))

    // Intermediate wires
    val operand1       : SInt = dontTouch(WireInit(MuxLookup(forward_jump_operand1, rs1_data, Seq(
        1.U -> alu,
        2.U -> RegAM_alu_out,
        3.U -> WriteBack_rd_data,
        4.U -> Memory_out
    ))))
    val operand_rs2    : SInt = dontTouch(WireInit(MuxLookup(forward_jump_operand2, rs2_data, Seq(
        1.U -> alu,
        2.U -> RegAM_alu_out,
        3.U -> WriteBack_rd_data,
        4.U -> Memory_out
    ))))
    val func3_opcode_id: UInt = dontTouch(WireInit(Cat(func3, opcode)))
    val jalr_en        : Bool = dontTouch(WireInit(func3_opcode_id === i_jalr_id))
    val operand2       : SInt = dontTouch(WireInit(Mux(jalr_en, imm, operand_rs2))) 
    val b_en           : Bool = dontTouch(WireInit(opcode === b_id))
    val jal_en         : Bool = dontTouch(WireInit(opcode === j_id))
    val jalr_PC        : UInt = dontTouch(WireInit(Cat((operand1 + operand2)(31, 1), "b0".U)))
    val br_en          : Bool = b_en && MuxCase(0.B, Seq(
        (func3_opcode_id === beq_id)  -> (operand1 === operand2),
        (func3_opcode_id === bne_id)  -> (operand1 =/= operand2),
        (func3_opcode_id === blt_id)  -> (operand1 < operand2),
        (func3_opcode_id === bge_id)  -> (operand1 >= operand2),
        (func3_opcode_id === bltu_id) -> (operand1.asUInt < operand2.asUInt),
        (func3_opcode_id === bgeu_id) -> (operand1.asUInt >= operand2.asUInt)
    ))

    // Wiring to output pins
    Seq(
        io.br_en, io.b_en, io.jal_en, io.jalr_en, io.jalr_PC
    ) zip Seq(
        br_en,    b_en,    jal_en,    jalr_en,    jalr_PC
    ) foreach
    {
        x => x._1 := x._2
    }
}

