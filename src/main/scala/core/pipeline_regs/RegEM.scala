package core.pipeline_regs

import chisel3._
import configs.Configs,
       core.decode_stage.{RegFileCtrl, DMemCtrl},
       core.execute_stage.ALUIO


class RegEMIO extends Bundle with Configs {
  val rAddr      : Vec[UInt]   = new RegDEIO().rAddr
  val regFileCtrl: RegFileCtrl = new RegDEIO().regFileCtrl
  val alu        : SInt        = new ALUIO().out
  val dMemCtrl   : DMemCtrl    = new RegDEIO().dMemCtrl
  val storeData  : SInt        = Output(SInt(XLEN.W))
}


class RegEM extends Module {
  val io = IO(new Bundle {
    val in : RegEMIO = Flipped(new RegEMIO)
    val out: RegEMIO = new RegEMIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.rAddr       -> io.out.rAddr,
    io.in.regFileCtrl -> io.out.regFileCtrl,
    io.in.alu         -> io.out.alu,
    io.in.dMemCtrl    -> io.out.dMemCtrl,
    io.in.storeData   -> io.out.storeData
  ))
}
