package DecodeStage

import chisel3._
import chisel3.util._

class JumpUnitIO(PARAMS:Map[String, Int], OPCODES:Map[String, Map[String, UInt]]) extends Bundle {
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
        val control: Vec[Bool] = Output(Vec(5, Bool()))
        val jalrPC : UInt      = Output(UInt(32.W))
}

class JumpUnit(PARAMS:Map[String, Int], OPCODES:Map[String, Map[String, UInt]]) extends Module {
        // Initializing IO pins
        val io                   : JumpUnitIO = IO(new JumpUnitIO(PARAMS, OPCODES))
        val rs1_data             : SInt       = dontTouch(WireInit(io.rs1_data))
        val rs2_data             : SInt       = dontTouch(WireInit(io.rs2_data))
        val alu                  : SInt       = dontTouch(WireInit(io.alu))
        val RegAM_alu_out        : SInt       = dontTouch(WireInit(io.RegAM_alu_out))
        val WriteBack_rd_data    : SInt       = dontTouch(WireInit(io.WriteBack_rd_data))
        val Memory_out           : SInt       = dontTouch(WireInit(io.Memory_out))
        val func3                : UInt       = dontTouch(WireInit(io.func3))
        val b_id                 : UInt       = dontTouch(WireInit(io.b_id))
        val j_id                 : UInt       = dontTouch(WireInit(io.j_id))
        val i_jalr_id            : UInt       = dontTouch(WireInit(io.i_jalr_id))
        val opcode               : UInt       = dontTouch(WireInit(io.opcode))
        val forward_jump_operand1: UInt       = dontTouch(WireInit(io.forward_jump_operand1))
        val forward_jump_operand2: UInt       = dontTouch(WireInit(io.forward_jump_operand2))
        val imm                  : SInt       = dontTouch(WireInit(io.imm))

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
        val jalrEn        : Bool = dontTouch(WireInit(func3_opcode_id === i_jalr_id))
        val operand2       : SInt = dontTouch(WireInit(Mux(jalrEn, imm, operand_rs2))) 
        val bEn           : Bool = dontTouch(WireInit(opcode === b_id))
        val jalEn          : Bool = dontTouch(WireInit(opcode === j_id))
        val brEn           : Bool = bEn && MuxCase(0.B, Seq(
                (func3_opcode_id === beq_id)  -> (operand1 === operand2),
                (func3_opcode_id === bne_id)  -> (operand1 =/= operand2),
                (func3_opcode_id === blt_id)  -> (operand1 < operand2),
                (func3_opcode_id === bge_id)  -> (operand1 >= operand2),
                (func3_opcode_id === bltu_id) -> (operand1.asUInt < operand2.asUInt),
                (func3_opcode_id === bgeu_id) -> (operand1.asUInt >= operand2.asUInt)
        ))
        val jumpStallEn = dontTouch(WireInit(brEn || jalEn || jalrEn))

        // Wiring to output pins
        io.jalrPC := dontTouch(WireInit(Cat((operand1 + operand2)(31, 1), "b0".U)))

        val control: Vec[Bool] = dontTouch(VecInit(brEn, bEn, jalEn, jalrEn, jumpStallEn))
        for (i <- 0 until control.length) {
                io.control(i) := control(i)
        }
}
