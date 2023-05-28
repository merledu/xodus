package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.pipeline_regs.RegFDIO


class DecoderIO extends Bundle with Configs {
  val inst: UInt = Flipped(new RegFDIO().inst)

  val opcode     : UInt      = Output(UInt(OpcodeWidth.W))
  val rAddr      : Vec[UInt] = Output(Vec(3, UInt(RegAddrWidth.W)))
  val funct3     : UInt      = Output(UInt(Funct3Width.W))
  val funct7_imm7: UInt      = Output(UInt(Funct7Width.W))
  val imm        : SInt      = Output(SInt(XLEN.W))
}


class Decoder extends RawModule with Configs {
  val io: DecoderIO = IO(new DecoderIO)


  /********************
   * Interconnections *
   ********************/

  Seq(
    io.opcode      -> (6, 0),
    io.funct3      -> (14, 12),
    io.funct7_imm7 -> (31, 25)
  ).map(
    x => x._1 := io.inst(x._2._1, x._2._2)
  )

  Seq(
    (11, 7),   // rd
    (19, 15),  // rs1
    (24, 20)   // rs2
  ).zipWithIndex.map(
    x => io.rAddr(x._2) := io.inst(x._1._1, x._1._2)
  )

  // Immediate Generation
  // Default: I immediate
  io.imm := MuxCase(io.inst(31, 20).asSInt, Seq(
    "S" -> Cat(io.inst(31, 25), io.inst(11, 7)),
    "B" -> Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), 0.U(1.W)),
    "U" -> Cat(io.inst(31, 12), 0.U(12.W)),
    "J" -> Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), 0.U(1.W))
  ).map(
    x => isa("opcodes")(x._1).values.map(
      y => io.opcode === y.U
    ).reduce(
      (a, b) => a || b
    ) -> x._2.asSInt
  ))
}
