package core.pipeline_regs

import chisel3._
import configs.Configs,
       core.decode_stage.{DecoderIO, RegFileCtrl, ALUCtrl, DMemAlignerCtrl, DMemCtrl, RegDECtrl}


class RegDEIO extends Bundle with Configs {
  val pc             : UInt            = new RegFDIO().pc
  val rAddr          : Vec[UInt]       = new DecoderIO().rAddr
  val intData        : Vec[SInt]       = Output(Vec(3, SInt(XLEN.W)))
  val regFileCtrl    : RegFileCtrl     = new RegFileCtrl
  val aluCtrl        : ALUCtrl         = new ALUCtrl
  val dMemAlignerCtrl: DMemAlignerCtrl = new DMemAlignerCtrl
  val dMemCtrl       : DMemCtrl        = new DMemCtrl
}


class RegDE extends Module {
  val io = IO(new Bundle {
    val in   : RegDEIO = Flipped(new RegDEIO)
    val stall: Bool    = Flipped(new RegDECtrl().stall)

    val out: RegDEIO = new RegDEIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc                    -> io.out.pc,
    io.in.rAddr                 -> io.out.rAddr,
    io.in.intData               -> io.out.intData,
    io.in.regFileCtrl           -> io.out.regFileCtrl,
    io.in.aluCtrl               -> io.out.aluCtrl,
    io.in.dMemAlignerCtrl.load  -> io.out.dMemAlignerCtrl.load,
    io.in.dMemAlignerCtrl.store -> io.out.dMemAlignerCtrl.store,
    io.in.dMemCtrl              -> io.out.dMemCtrl
  ).map(
    x => Mux(io.stall, x._2, x._1) -> x._2
  ) ++ Seq(
    io.in.dMemAlignerCtrl.align -> io.out.dMemAlignerCtrl.align
  ))
}
