package ExecuteStage

import chisel3._
import chisel3.util._

class ALU_IO extends Bundle
{
    // Input pins
    val rs1_data               : SInt = Input(SInt(32.W))
    val rs2_data               : SInt = Input(SInt(32.W))
    val imm                    : SInt = Input(SInt(32.W))
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
    val jalrAddition_en        : Bool = Input(Bool())

    // Output pins
    val out: SInt = Output(SInt(32.W))
}
class ALU extends Module
{
    // Initializing IO pins
    val io                     : ALU_IO = IO(new ALU_IO)
    val rs1_data               : SInt   = dontTouch(WireInit(io.rs1_data))
    val rs2_data               : SInt   = dontTouch(WireInit(io.rs2_data))
    val imm                    : SInt   = dontTouch(WireInit(io.imm))
    val imm_en                 : Bool   = dontTouch(WireInit(io.imm_en))
    val addition_en            : Bool   = dontTouch(WireInit(io.addition_en))
    val shiftLeftLogical_en    : Bool   = dontTouch(WireInit(io.shiftLeftLogical_en))
    val lessThan_en            : Bool   = dontTouch(WireInit(io.lessThan_en))
    val lessThanU_en           : Bool   = dontTouch(WireInit(io.lessThanU_en))
    val XOR_en                 : Bool   = dontTouch(WireInit(io.XOR_en))
    val shiftRightLogical_en   : Bool   = dontTouch(WireInit(io.shiftLeftLogical_en))
    val shiftRightArithmetic_en: Bool   = dontTouch(WireInit(io.shiftRightArithmetic_en))
    val OR_en                  : Bool   = dontTouch(WireInit(io.OR_en))
    val AND_en                 : Bool   = dontTouch(WireInit(io.AND_en))
    val subtraction_en         : Bool   = dontTouch(WireInit(io.subtraction_en))
    val jalrAddition_en        : Bool   = dontTouch(WireInit(io.jalrAddition_en))

    // Intermediate wires
    val operand1: SInt = dontTouch(WireInit(rs1_data))
    val operand2: SInt = dontTouch(WireInit(Mux(imm_en, imm, rs2_data)))

    // Intermediate wires
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
    val jalrAddition        : SInt = dontTouch(WireInit(Cat((operand1 + imm)(31, 1), "b0".U).asSInt))

    // Wiring to output pins
    io.out := MuxCase(0.S, Seq(
        addition_en             -> addition,
        shiftLeftLogical_en     -> shiftLeftLogical,
        lessThan_en             -> lessThan,
        lessThanU_en            -> lessThanU,
        XOR_en                  -> XOR,
        shiftRightLogical_en    -> shiftRightLogical,
        shiftRightArithmetic_en -> shiftRightArithmetic,
        OR_en                   -> OR,
        AND_en                  -> AND,
        subtraction_en          -> subtraction,
        jalrAddition_en         -> jalrAddition,
    ))
}

