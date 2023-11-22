//package core.execute_stage
//
//import chisel3._,
//       chisel3.util._
//import configs.Configs,
//       core.decode_stage.ALUCtrl,
//       core.pipeline_regs.RegDEIO
//
//
//class ALUIO extends Bundle with Configs {
//  val in  : Vec[SInt] = Flipped(new RegDEIO().intData)
//  val pc  : UInt      = Flipped(new RegDEIO().pc)
//  val ctrl: ALUCtrl   = Flipped(new RegDEIO().aluCtrl)
//
//  val out: SInt = Output(SInt(XLEN.W))
//}
//
//
//class ALU extends RawModule with Configs {
//  val io: ALUIO = IO(new ALUIO)
//
//  // Operands
//  val sOperand: Vec[SInt] = VecInit(
//    io.in(0),
//    Mux(io.ctrl.immSel, io.in(2), io.in(1))  // imm or rs2
//  )
//
//  val uOperand: Vec[UInt] = VecInit(
//    io.pc,
//    Mux(io.ctrl.opSel(11), io.in(2).asUInt, 4.U)  // auipc or 4.U
//  )
//
//
//  /********************
//   * Interconnections *
//   ********************/
//
//  // Output Selection
//  // Default: signed addition
//  io.out := MuxCase(sOperand(0) + sOperand(1), Seq(
//    sOperand(0) - sOperand(1),                                               // subtraction
//    (sOperand(0) << sOperand(1)(4, 0)).asSInt,                               // shift left logical
//    Cat(0.U((XLEN - 1).W), sOperand(0) < sOperand(1)).asSInt,                // signed less than
//    Cat(0.U((XLEN - 1).W), sOperand(0).asUInt < sOperand(1).asUInt).asSInt,  // unsigned less than
//    (sOperand(0) ^ sOperand(1)).asSInt,                                      // xor
//    (sOperand(0).asUInt >> sOperand(1)(4, 0)).asSInt,                        // shift right logical
//    (sOperand(0) >> sOperand(1)(4, 0)),                                      // shift right arithmetic
//    (sOperand(0) | sOperand(1)).asSInt,                                      // or
//    (sOperand(0) & sOperand(1)).asSInt,                                      // and
//    sOperand(1),                                                             // lui
//    (uOperand(0) + uOperand(1)).asSInt                                       // unsigned addition
//  ).zipWithIndex.map(
//    x => io.ctrl.opSel(x._2) -> x._1
//  ))
//}
