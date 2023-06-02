package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.pipeline_regs.RegDEIO


class ALU_IO extends Bundle with Configs {
  val in: Vec[SInt] = Flipped(new RegDEIO().data)
  val pc: UInt      = Input(UInt(XLEN.W))
  val en: Vec[Bool] = Input(Vec(arch("aluEn").length, Bool()))

  val out: SInt = Output(SInt(32.W))
}


class ALU extends RawModule with Configs {
  val io: ALU_IO = IO(new ALU_IO)

  // Operands
  val sOperand: Vec[SInt] = VecInit(
    io.in(0),
    Mux(io.en(12), io.in(2), io.in(1))  // imm or rs2
  )

  val uOperand: Vec[UInt] = VecInit(
    io.pc,
    Mux(io.en(13), io.in(2).asUInt, 4.U)  // auipc or 4.U
  )


  /********************
   * Interconnections *
   ********************/

  // Output Selection
  // Default: s+
  io.out := MuxCase(sOperand(0) + sOperand(1), Seq(
    (sOperand(0) < sOperand(1)).asSInt,
    (sOperand(0).asUInt < sOperand(1).asUInt).asSInt,
    (sOperand(0) & sOperand(1)).asSInt,
    (sOperand(0) | sOperand(1)).asSInt,
    (sOperand(0) ^ sOperand(1)).asSInt,
    (sOperand(0) << sOperand(1)(4, 0)).asSInt,
    (sOperand(0).asUInt >> sOperand(1)(4, 0)).asSInt,
    (sOperand(0) >> sOperand(1)(4, 0)),
    sOperand(1),
    (uOperand(0) + uOperand(1)).asSInt,
    sOperand(0) - sOperand(1)
  ).zipWithIndex.map(
    x => io.en(x._2 + 1) -> x._1
  ))
}
