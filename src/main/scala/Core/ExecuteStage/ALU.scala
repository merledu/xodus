package ExecuteStage

import chisel3._
import chisel3.util._

class ALU_IO extends Bundle {
    // Input pins
    val PC                     : UInt = Input(UInt(32.W))
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
    val jalr_en                : Bool = Input(Bool())
    val jal_en                 : Bool = Input(Bool())
    val auipc_en               : Bool = Input(Bool())
    val lui_en                 : Bool = Input(Bool())

    // Output pins
    val out: SInt = Output(SInt(32.W))
}
class ALU extends Module
{
    // Initializing IO pins
    val io                     : ALU_IO = IO(new ALU_IO)
    val PC                     : UInt   = dontTouch(WireInit(io.PC))
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
    val jalr_en                : Bool   = dontTouch(WireInit(io.jalr_en))
    val jal_en                 : Bool   = dontTouch(WireInit(io.jal_en))
    val auipc_en               : Bool   = dontTouch(WireInit(io.auipc_en))
    val lui_en                 : Bool   = dontTouch(WireInit(io.lui_en))

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
    val PC4                 : SInt = dontTouch(WireInit((PC + 4.U).asSInt))
    val PC4_en              : Bool = dontTouch(WireInit(jalr_en || jal_en))
    val u_imm               : SInt = dontTouch(WireInit(imm << 12.U))
    val auipc               : SInt = dontTouch(WireInit((PC + u_imm.asUInt).asSInt))
    val lui                 : SInt = dontTouch(WireInit(u_imm))

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
        PC4_en                  -> PC4,
        auipc_en                -> auipc,
        lui_en                  -> lui
    ))
}

