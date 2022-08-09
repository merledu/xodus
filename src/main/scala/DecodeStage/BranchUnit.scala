package DecodeStage

import chisel3._
import chisel3.util._

class BranchUnit_IO extends Bundle
{
    // Input pins
    val rs1_data           : SInt = Input(SInt(32.W))
    val rs2_data           : SInt = Input(SInt(32.W))
    val alu                : SInt = Input(SInt(32.W))
    val RegAM_alu_out      : SInt = Input(SInt(32.W))
    val WriteBack_rd_data  : SInt = Input(SInt(32.W))
    val Memory_out         : SInt = Input(SInt(32.W))
    val func3              : UInt = Input(UInt(3.W))
    val b_id               : UInt = Input(UInt(7.W))
    val opcode             : UInt = Input(UInt(7.W))
    val forward_br_operand1: UInt = Input(UInt(3.W))
    val forward_br_operand2: UInt = Input(UInt(3.W))

    // Output pins
    val br_en: Bool = Output(Bool())
    val b_en : Bool = Output(Bool())
}
class BranchUnit extends Module
{
    // Initializing IO pins
    val io                 : BranchUnit_IO = IO(new BranchUnit_IO)
    val rs1_data           : SInt          = dontTouch(WireInit(io.rs1_data))
    val rs2_data           : SInt          = dontTouch(WireInit(io.rs2_data))
    val alu                : SInt          = dontTouch(WireInit(io.alu))
    val RegAM_alu_out      : SInt          = dontTouch(WireInit(io.RegAM_alu_out))
    val WriteBack_rd_data  : SInt          = dontTouch(WireInit(io.WriteBack_rd_data))
    val Memory_out         : SInt          = dontTouch(WireInit(io.Memory_out))
    val func3              : UInt          = dontTouch(WireInit(io.func3))
    val b_id               : UInt          = dontTouch(WireInit(io.b_id))
    val opcode             : UInt          = dontTouch(WireInit(io.opcode))
    val forward_br_operand1: UInt          = dontTouch(WireInit(io.forward_br_operand1))
    val forward_br_operand2: UInt          = dontTouch(WireInit(io.forward_br_operand2))

    // B-Type IDs
    val beq_id : UInt = dontTouch(WireInit(99.U(10.W)))
    val bne_id : UInt = dontTouch(WireInit(227.U(10.W)))
    val blt_id : UInt = dontTouch(WireInit(611.U(10.W)))
    val bge_id : UInt = dontTouch(WireInit(739.U(10.W)))
    val bltu_id: UInt = dontTouch(WireInit(867.U(10.W)))
    val bgeu_id: UInt = dontTouch(WireInit(995.U(10.W)))

    // Intermediate wires
    val operand1: SInt = dontTouch(WireInit(MuxLookup(forward_br_operand1, rs1_data, Seq(
        1.U -> alu,
        2.U -> RegAM_alu_out,
        3.U -> WriteBack_rd_data,
        4.U -> Memory_out
    ))))
    val operand2: SInt = dontTouch(WireInit(MuxLookup(forward_br_operand2, rs2_data, Seq(
        1.U -> alu,
        2.U -> RegAM_alu_out,
        3.U -> WriteBack_rd_data,
        4.U -> Memory_out
    ))))
    val func3_opcode_id: UInt = dontTouch(WireInit(Cat(func3, opcode)))
    val b_en           : Bool = dontTouch(WireInit(opcode === b_id))
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
        io.br_en, io.b_en
    ) zip Seq(
        br_en,    b_en
    ) foreach
    {
        x => x._1 := x._2
    }
}

