package core.pipeline_regs

import chisel3._
import configs.Configs,
       core.decode_stage.{DecoderIO, RegFileCtrl, ALUCtrl, DMemCtrl}


class RegDEIO extends Bundle with Configs {
  val pc         : UInt        = new RegFDIO().pc
  val rAddr      : Vec[UInt]   = new DecoderIO().rAddr
  val intData    : Vec[SInt]   = Output(Vec(3, SInt(XLEN.W)))
  val regFileCtrl: RegFileCtrl = new RegFileCtrl
  val aluCtrl    : ALUCtrl     = new ALUCtrl
  val dMemCtrl   : DMemCtrl    = new DMemCtrl
}


class RegDE extends Module {
  val io = IO(new Bundle {
    val in : RegDEIO = Flipped(new RegDEIO)
    val out: RegDEIO = new RegDEIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc          -> io.out.pc,
    io.in.rAddr       -> io.out.rAddr,
    io.in.intData     -> io.out.intData,
    io.in.regFileCtrl -> io.out.regFileCtrl,
    io.in.aluCtrl     -> io.out.aluCtrl,
    io.in.dMemCtrl    -> io.out.dMemCtrl
  ))
}
