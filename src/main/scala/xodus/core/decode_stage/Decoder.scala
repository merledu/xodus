package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class DecoderIO extends Bundle with Configs {
  val inst: UInt        = Input(UInt(XLEN.W))
  val ctrl: DecoderCtrl = Flipped(new DecoderCtrl)

  val opcode: UInt      = Output(UInt(OPCODE_WIDTH.W))
  val r_addr: Vec[UInt] = Output(Vec(3, UInt(REG_ADDR_WIDTH.W)))
  val funct3: UInt      = Output(UInt(FUNCT3_WIDTH.W))
  val funct7: UInt      = Output(UInt(FUNCT7_WIDTH.W))
  val imm   : SInt      = Output(SInt(XLEN.W))
}


class Decoder extends RawModule {
  val io: DecoderIO = IO(new DecoderIO)


   /*** Interconnections ***/

  Seq(
    io.opcode -> (6, 0),
    io.funct3 -> (14, 12),
    io.funct7 -> (31, 25)
  ).foreach(
    x => x._1 := io.inst(x._2._1, x._2._2)
  )

  Seq(
    (11, 7),   // rd
    (19, 15),  // rs1
    (24, 20)   // rs2
  ).zipWithIndex.foreach(
    x => io.r_addr(x._2) := io.inst(x._1._1, x._1._2)
  )

  // Immediate Generation
  io.imm := MuxLookup(io.ctrl.imm_gen_sel, 0.S)(Seq(
    io.inst(31, 20),                                                           // I
    Cat(io.inst(31, 25), io.inst(11, 7)),                                      // S
    Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), 0.U(1.W)),   // B
    Cat(io.inst(31, 12), 0.U(12.W)),                                           // U
    Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), 0.U(1.W))  // J
  ).zipWithIndex.map(
    x => (x._2 + 1).U -> x._1.asSInt
  ))
}
