package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.ALUCtrl,
       xodus.core.pipeline_regs.RegDEIO


class ALUIO extends Bundle with Configs {
  val in  : Vec[SInt] = Flipped(new RegDEIO().int_data)
  val pc  : UInt      = Flipped(new RegDEIO().pc)
  val ctrl: ALUCtrl   = Flipped(new RegDEIO().alu_ctrl)

  val out: SInt = Output(SInt(XLEN.W))
}


class ALU extends RawModule with Configs {
  val io: ALUIO = IO(new ALUIO)

  // Operands
  val s_operand: Vec[SInt] = VecInit(
    io.in(0),
    Mux(io.ctrl.imm_sel, io.in(2), io.in(1))  // imm or rs2
  )

  val u_operand: Vec[UInt] = VecInit(
    io.pc,
    Mux(io.ctrl.imm_sel, io.in(2).asUInt, 4.U)  // imm or 4.U
  )


   /*** Interconnections ***/

  // Output Selection
  io.out := MuxLookup(io.ctrl.op_sel, 0.S)(Seq(
    s_operand(0) + s_operand(1),                                               // signed addition
    Cat(0.U((XLEN - 1).W), s_operand(0) < s_operand(1)).asSInt,                // signed less than
    Cat(0.U((XLEN - 1).W), s_operand(0).asUInt < s_operand(1).asUInt).asSInt,  // unsigned less than
    s_operand(0) ^ s_operand(1),                                               // xor
    s_operand(0) | s_operand(1),                                               // or
    s_operand(0) & s_operand(1),                                               // and
    (s_operand(0) << s_operand(1)(4, 0)).asSInt,                               // shift left logical
    (s_operand(0).asUInt >> s_operand(1)(4, 0)).asSInt,                        // shift right logical
    (s_operand(0) >> s_operand(1)(4, 0)).asSInt,                               // shift right arithmetic
    s_operand(0) - s_operand(1),                                               // subtraction
    (s_operand(0) + s_operand(1)) & Cat(-1.S((XLEN - 1).W), 0.U(1.W)).asSInt,  // jalr
    s_operand(1),                                                              // lui
    (u_operand(0) + u_operand(1)).asSInt                                       // unsigned addition
  ).zipWithIndex.map(
    x => (x._2 + 1).U -> x._1
  ))
}