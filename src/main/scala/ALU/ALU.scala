package ALU

import chisel3._
import chisel3.util._

class ALU_IO extends Bundle
{
    // Input pins
    val rs1_data               : SInt = Input(SInt(32.W))
    val rs2_data               : SInt = Input(SInt(32.W))
    val i_s_b_imm              : SInt = Input(SInt(12.W))
    val imm_en                 : Bool = Input(Bool())
    val addition_en            : Bool = Input(Bool())
    val shiftLeftLogical_en    : Bool = Input(Bool())
    val lessThan_en            : Bool = Input(Bool())
    val lessThanU_en           : Bool = Input(Bool())
    val XOR_en                 : Bool = Input(Bool())
    val shiftRightLogical_en   : Bool = Input(Bool())
    val shiftRightArithmetic_en: Bool = Input(Bool())
    val OR_en                  : Bool = Input(Bool())
    val AND_en                 : Bool = Input(Bool())
    val subtraction_en         : Bool = Input(Bool())
    val equal_en               : Bool = Input(Bool())
    val notEqual_en            : Bool = Input(Bool())
    val greaterThanEqual_en    : Bool = Input(Bool())
    val greaterThanEqualU_en   : Bool = Input(Bool())
    val jalrAddition_en        : Bool = Input(Bool())

    // Output pins
    val out: SInt = Output(SInt(32.W))
}
class ALU extends Module
{
    // Initializing IO pins
    val io: ALU_IO = IO(new ALU_IO())

    // Input wires
    val rs1_data               : SInt = dontTouch(WireInit(io.rs1_data))
    val rs2_data               : SInt = dontTouch(WireInit(io.rs2_data))
    val i_s_b_imm              : SInt = dontTouch(WireInit(io.i_s_b_imm))
    val imm_en                 : Bool = dontTouch(WireInit(io.imm_en))
    val addition_en            : Bool = dontTouch(WireInit(io.addition_en))
    val shiftLeftLogical_en    : Bool = dontTouch(WireInit(io.shiftLeftLogical_en))
    val lessThan_en            : Bool = dontTouch(WireInit(io.lessThan_en))
    val lessThanU_en           : Bool = dontTouch(WireInit(io.lessThanU_en))
    val XOR_en                 : Bool = dontTouch(WireInit(io.XOR_en))
    val shiftRightLogical_en   : Bool = dontTouch(WireInit(io.shiftLeftLogical_en))
    val shiftRightArithmetic_en: Bool = dontTouch(WireInit(io.shiftRightArithmetic_en))
    val OR_en                  : Bool = dontTouch(WireInit(io.OR_en))
    val AND_en                 : Bool = dontTouch(WireInit(io.AND_en))
    val subtraction_en         : Bool = dontTouch(WireInit(io.subtraction_en))
    val equal_en               : Bool = dontTouch(WireInit(io.equal_en))
    val notEqual_en            : Bool = dontTouch(WireInit(io.notEqual_en))
    val greaterThanEqual_en    : Bool = dontTouch(WireInit(io.greaterThanEqual_en))
    val greaterThanEqualU_en   : Bool = dontTouch(WireInit(io.greaterThanEqualU_en))
    val jalrAddition_en        : Bool = dontTouch(WireInit(io.jalrAddition_en))

    // Intermediate wires
    val operand1: SInt = dontTouch(WireInit(rs1_data))
    val operand2: SInt = dontTouch(WireInit(Mux(imm_en, i_s_b_imm, rs2_data)))

    // Operation wires
    val addition            : SInt = dontTouch(WireInit(operand1 + operand2))
    val lessThan            : SInt = dontTouch(WireInit((operand1 < operand2).asSInt))
    val lessThanU           : SInt = dontTouch(WireInit((operand1.asUInt < operand2.asUInt).asSInt))
    val XOR                 : SInt = dontTouch(WireInit(operand1 ^ operand2))
    val OR                  : SInt = dontTouch(WireInit(operand1 | operand2))
    val AND                 : SInt = dontTouch(WireInit(operand1 & operand2))
    val shiftLeftLogical    : SInt = dontTouch(WireInit((operand1 << operand2(4, 0)).asSInt))
    val shiftRightLogical   : SInt = dontTouch(WireInit((operand1.asUInt >> operand2(4, 0)).asSInt))
    val shiftRightArithmetic: SInt = dontTouch(WireInit((operand1 >> operand2(4, 0)).asSInt))
    val subtraction         : SInt = dontTouch(WireInit(operand1 - operand2))
    val equal               : SInt = dontTouch(WireInit((operand1 === operand2).asSInt))
    val notEqual            : SInt = dontTouch(WireInit((operand1 =/= operand2).asSInt))
    val greaterThanEqual    : SInt = dontTouch(WireInit((operand1 >= operand2).asSInt))
    val greaterThanEqualU   : SInt = dontTouch(WireInit((operand1.asUInt >= operand2.asUInt).asSInt))
    val jalrAddition        : SInt = dontTouch(WireInit(Cat((operand1 + i_s_b_imm)(31, 1), 0.U).asSInt))

    // Wiring to output pins
    io.out := MuxCase(0.S, Array(
        addition_en             -> addition,              // lb, lh, lw, lbu, lhu, addi, sb, sh, sw, add
        shiftLeftLogical_en     -> shiftLeftLogical,      // slli, sll
        lessThan_en             -> lessThan,              // slti, slt, blt
        lessThanU_en            -> lessThanU,             // sltiu, sltu, bltu
        XOR_en                  -> XOR,                   // xori, xor
        shiftRightLogical_en    -> shiftRightLogical,     // srli, srl
        shiftRightArithmetic_en -> shiftRightArithmetic,  // srai, srai
        OR_en                   -> OR,                    // ori, or
        AND_en                  -> AND,                   // andi, and
        subtraction_en          -> subtraction,           // sub
        equal_en                -> equal,                 // beq
        notEqual_en             -> notEqual,              // bne
        greaterThanEqual_en     -> greaterThanEqual,      // bge
        greaterThanEqualU_en    -> greaterThanEqualU,     // bgeu
        jalrAddition_en         -> jalrAddition,          // jalr
    ))
}

