package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DecoderIO


class RegDEIO extends Bundle with Configs {
  val pc         : UInt      = new RegFDIO().pc
  val opcode     : UInt      = new DecoderIO().opcode
  val rAddr      : Vec[UInt] = new DecoderIO().rAddr
  val funct3     : UInt      = new DecoderIO().funct3
  val funct7_imm7: UInt      = new DecoderIO().funct7_imm7
  val data       : Vec[SInt] = Output(Vec(3, SInt(XLEN.W)))
}


class RegDE extends Module with Configs {
  val io = IO(new Bundle {
    val in : RegDEIO = Flipped(new RegDEIO)
    val out: RegDEIO = new RegDEIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc          -> io.out.pc,
    io.in.opcode      -> io.out.opcode,
    io.in.funct3      -> io.out.funct3,
    io.in.funct7_imm7 -> io.out.funct7_imm7
  ) ++ (
    for (i <- 0 until io.in.rAddr.length)
      yield io.in.rAddr(i) -> io.out.rAddr(i)
  ) ++ (
    for (i <- 0 until io.in.data.length)
      yield io.in.data(i) -> io.out.data(i)
  ))
}
