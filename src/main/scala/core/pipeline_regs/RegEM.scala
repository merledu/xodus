package core.pipeline_regs

import chisel3._
import configs.Configs,
       core.decode_stage.{RegFileCtrl, DMemCtrl},
       core.execute_stage.{ALUIO, DMemAlignerIO}


class RegEMIO extends Bundle with Configs {
  val rAddr      : Vec[UInt]   = new RegDEIO().rAddr
  val regFileCtrl: RegFileCtrl = new RegDEIO().regFileCtrl
  val alu        : SInt        = new ALUIO().out
  val dMemCtrl   : DMemCtrl    = new RegDEIO().dMemCtrl
  val wmask      : UInt        = new DMemAlignerIO().wmask
  val dMemAddr   : UInt        = new DMemAlignerIO().addr
  val store      : UInt        = new DMemAlignerIO().alignedStore
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
    io.in.wmask       -> io.out.wmask,
    io.in.dMemAddr    -> io.out.dMemAddr,
    io.in.store       -> io.out.store
  ))
}
