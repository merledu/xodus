package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.Enables,
       xodus.core.execute_stage.ALUIO


class RegEMIO extends Bundle with Configs {
  val regFileEN: Bool = new Enables().regFile
  val alu      : SInt = new ALUIO().out
}


class RegEM extends Module with Configs {
  val io = IO(new Bundle {
    val in : RegEMIO = Flipped(new RegEMIO)
    val out: RegEMIO = new RegEMIO
  })

  // Pipeline
  genPipeline(Seq(
    io.in.alu       -> io.out.alu,
    io.in.regFileEN -> io.out.regFileEN
  ))
}
