package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.{DecoderIO, Enables}


class RegDEIO extends Bundle with Configs {
  val pc         : UInt      = new RegFDIO().pc
  val rAddr      : Vec[UInt] = new DecoderIO().rAddr
  val data       : Vec[SInt] = Output(Vec(3, SInt(XLEN.W)))
  val aluEN      : Vec[Bool] = new Enables().alu
  val regFileEN  : Bool      = new Enables().regFile
}


class RegDE extends Module with Configs {
  val io = IO(new Bundle {
    val in : RegDEIO = Flipped(new RegDEIO)
    val out: RegDEIO = new RegDEIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc        -> io.out.pc,
    io.in.rAddr     -> io.out.rAddr,
    io.in.data      -> io.out.data,
    io.in.aluEN     -> io.out.aluEN,
    io.in.regFileEN -> io.out.regFileEN
  )/* ++ (
    for (i <- 0 until io.in.rAddr.length)
      yield io.in.rAddr(i) -> io.out.rAddr(i)
  ) ++ (
    for (i <- 0 until io.in.data.length)
      yield io.in.data(i) -> io.out.data(i)
  )*/)
}
