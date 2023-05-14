package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.io.RegFDIO


class RegFD extends Module with Configs {
  val io = IO(new Bundle {
    val in : RegFDIO = new RegFDIO
    val out: RegFDIO = Flipped(new RegFDIO)
  })

  // Pipeline
  genPipeline(Seq(
    io.in.pc   -> io.out.pc,
    io.in.inst -> io.out.inst
  ))
}
