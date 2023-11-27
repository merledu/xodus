package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class JumpUnitIO extends Bundle with Configs {
  val int_data: Vec[SInt]    = Input(Vec(3, SInt(XLEN.W)))
  val pc      : UInt         = Input(UInt(XLEN.W))
  val ctrl    : JumpUnitCtrl = Flipped(new JumpUnitCtrl)

  val jump: Valid[UInt] = Valid(UInt(XLEN.W))
}


class JumpUnit extends RawModule {
  val io: JumpUnitIO = IO(new JumpUnitIO)


  /*** Interconnections ***/

  io.jump.valid := io.ctrl.en && MuxLookup(io.ctrl.op_sel, 0.B)(Seq(
    io.int_data(0) === io.int_data(1),              // beq
    io.int_data(0) =/= io.int_data(1),              // bne
    io.int_data(0) < io.int_data(1),                // blt
    io.int_data(0) >= io.int_data(1),               // bge
    io.int_data(0).asUInt < io.int_data(1).asUInt,  // bltu
    io.int_data(0).asUInt >= io.int_data(1).asUInt  // bgeu
  ).zipWithIndex.map(
    x => (x._2 + 2).U -> x._1
  )) && Seq(1, 8).map(io.ctrl.op_sel === _.U).reduce(_ || _)  // jalr, jal

  io.jump.bits  := Mux(io.ctrl.op_sel === 1.U, Cat(io.int_data(0) + io.int_data(2), 0.U(1.W)), io.pc + io.int_data(2).asUInt)
}