package core.pipeline_regs

import chisel3._,
       chisel3.util._
import configs.Configs,
       core.decode_stage.{RegFileEN, DMemEN},
       core.execute_stage.ALUIO


class RegEMIO extends Bundle with Configs {
  val rAddr    : Vec[UInt] = new RegDEIO().rAddr
  val regFileEN: RegFileEN = new RegDEIO().regFileEN
  val alu      : SInt      = new ALUIO().out
  val dMemEN   : DMemEN    = new RegDEIO().dMemEN
  val storeData: SInt      = Output(SInt(XLEN.W))
}


class RegEM extends Module with Configs {
  val io = IO(new Bundle {
    val in : RegEMIO = Flipped(new RegEMIO)
    val out: RegEMIO = new RegEMIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.rAddr     -> io.out.rAddr,
    io.in.regFileEN -> io.out.regFileEN,
    io.in.alu       -> io.out.alu,
    io.in.dMemEN    -> io.out.dMemEN,
    io.in.storeData -> io.out.storeData
  ))
}
