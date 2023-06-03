package core.pipeline_regs

import chisel3._,
       chisel3.util._
import configs.Configs,
       core.decode_stage.{DecoderIO, RegFileEN, ALUEN, DMemEN}


class RegDEIO extends Bundle with Configs {
  val pc       : UInt      = new RegFDIO().pc
  val rAddr    : Vec[UInt] = new DecoderIO().rAddr
  val data     : Vec[SInt] = Output(Vec(3, SInt(XLEN.W)))
  val regFileEN: RegFileEN = new RegFileEN
  val aluEN    : ALUEN     = new ALUEN
  val dMemEN   : DMemEN    = new DMemEN
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
    io.in.regFileEN -> io.out.regFileEN,
    io.in.aluEN     -> io.out.aluEN,
    io.in.dMemEN    -> io.out.dMemEN
  ))
}
