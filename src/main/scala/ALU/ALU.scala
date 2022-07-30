package ALU

import chisel3._
import chisel3.util._

class ALU_IO extends Bundle
{
    // Input pins
    val opcode  : UInt = Input(UInt(7.W))
    val func3   : UInt = Input(UInt(3.W))
    val rs1_data: SInt = Input(SInt(32.W))
    val rs2_data: SInt = Input(SInt(32.W))
    val func7   : UInt = Input(UInt(7.W))
    val imm     : SInt = Input(SInt(12.W))
    val imm_en  : Bool = Input(Bool())

    // Output pins
    val out: SInt = Output(SInt(32.W))
}
class ALU extends Module
{
    // Initializing IO pins
    val io: ALU_IO = IO(new ALU_IO())

    // Input wires
    val opcode  : UInt = dontTouch(WireInit(io.opcode))
    val func3   : UInt = dontTouch(WireInit(io.func3))
    val rs1_data: SInt = dontTouch(WireInit(io.rs1_data))
    val rs2_data: SInt = dontTouch(WireInit(io.rs2_data))
    val func7   : UInt = dontTouch(WireInit(io.func7))
    val imm     : SInt = dontTouch(WireInit(io.imm))
    val imm_en  : Bool = dontTouch(WireInit(io.imm_en))

    // Intermediate wires
    val operand1             : SInt = dontTouch(WireInit(rs1_data))
    val operand2             : SInt = dontTouch(WireInit(Mux(imm_en, imm, rs2_data)))
    val func7_func3_opcode_id: UInt = dontTouch(WireInit(Cat(func7, func3, opcode)))
    val func3_opcode_id      : UInt = dontTouch(WireInit(Cat(func3, opcode)))
    val imm_func3_opcode_id  : UInt = dontTouch(WireInit(Cat(imm(11, 5), func3, opcode)))

    // Operation wires
    val addition            : SInt = dontTouch(WireInit(operand1 + operand2))
    val additionU           : SInt = dontTouch(WireInit(((operand1.asUInt + operand2.asUInt).asSInt)))
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
    val jalrAddition        : SInt = dontTouch(WireInit(Cat((operand1 + imm)(31, 1), 0.U).asSInt))

    // Wiring to output pins
    io.out := MuxCase(0.S, Array(
        (func3_opcode_id === "b0000011".U                  || func3_opcode_id === "b0000010011".U               || func7_func3_opcode_id === "b00000000000110011".U || opcode === "b0100011".U) -> addition,              // lb, lh, lw, lbu, lhu || addi  || add || sb, sh, sw
        (func3_opcode_id === "b0100010011".U               || func7_func3_opcode_id === "b00000000100110011".U  || func3_opcode_id === "b1001100011".U)                                         -> lessThan,              // slti  || slt || blt
        (func3_opcode_id === "b0110010011".U               || func7_func3_opcode_id === "b00000000110110011".U  || func3_opcode_id === "b1101100011".U)                                         -> lessThanU,             // sltiu || sltu || bltu
        (func3_opcode_id === "b1000010011".U               || func7_func3_opcode_id === "b00000001000110011".U)                                                                                 -> XOR,                   // xori  || xor
        (func3_opcode_id === "b1100010011".U               || func7_func3_opcode_id === "b00000001100110011".U)                                                                                 -> OR,                    // ori   || or
        (func3_opcode_id === "b1110010011".U               || func7_func3_opcode_id === "b00000001110110011".U)                                                                                 -> AND,                   // andi  || and
        (imm_func3_opcode_id === "b00000000010010011".U    || func7_func3_opcode_id === "b00000000010110011".U)                                                                                 -> shiftLeftLogical,      // slli  || sll
        (imm_func3_opcode_id === "b00000001010010011".U    || func7_func3_opcode_id === "b00000001010110011".U)                                                                                 -> shiftRightLogical,     // srli || srl
        (imm_func3_opcode_id === "b01000001010010011".U    || func7_func3_opcode_id === "b01000001010110011".U)                                                                                 -> shiftRightArithmetic,  // srai || sra
        (func7_func3_opcode_id === "b01000000000110011".U)                                                                                                                                      -> subtraction,           // sub
        (func3_opcode_id === "b0001100011".U)                                                                                                                                                   -> equal,                 // beq
        (func3_opcode_id === "b0011100011".U)                                                                                                                                                   -> notEqual,              // bne
        (func3_opcode_id === "b1011100011".U)                                                                                                                                                   -> greaterThanEqual,      // bge
        (func3_opcode_id === "b1111100011".U)                                                                                                                                                   -> greaterThanEqualU,     // bgeu
        (func3_opcode_id === "b0001100111".U)                                                                                                                                                   -> jalrAddition           // jalr
    ))
}

