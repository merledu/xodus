package core.pipeline_regs

import chisel3._,
       chisel3.util._
import configs.Configs,
       core.decode_stage.RegFileCtrl,
       core.memory_stage.DMemInterfaceIO


class RegMWIO extends Bundle with Configs {
  val rAddr      : Vec[UInt]   = new RegEMIO().rAddr
  val regFileCtrl: RegFileCtrl = new RegEMIO().regFileCtrl
  val alu        : SInt        = new RegEMIO().alu
  val load       : Valid[SInt] = new DMemInterfaceIO().load
}


class RegMW extends Module {
  val io = IO(new Bundle {
    val in : RegMWIO = Flipped(new RegMWIO)
    val out: RegMWIO = new RegMWIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.rAddr       -> io.out.rAddr,
    io.in.regFileCtrl -> io.out.regFileCtrl,
    io.in.alu         -> io.out.alu,
    io.in.load        -> io.out.load
  ))
}
