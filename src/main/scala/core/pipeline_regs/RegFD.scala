package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.fetch_stage.{PCIO, IMemJuncIO}


class RegFDIO extends Bundle with Configs {
  val pc  : UInt = new PCIO().pc
  val inst: UInt = new IMemJuncIO().inst
}


class RegFD extends Module with Configs {
  val io = IO(new Bundle {
    val in : RegFDIO = Flipped(new RegFDIO)
    val out: RegFDIO = new RegFDIO
  })

  // Pipeline
  genPipeline(Seq(
    io.in.pc   -> io.out.pc,
    io.in.inst -> io.out.inst
  ))
}
