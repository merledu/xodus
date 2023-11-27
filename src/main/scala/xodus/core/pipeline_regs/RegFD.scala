package xodus.core.pipeline_regs

import chisel3._
import xodus.configs.Configs


class RegFDIO extends Bundle with Configs {
  val pc  : UInt = Output(UInt(XLEN.W))
  val inst: UInt = Output(UInt(XLEN.W))
}


class RegFD extends Module {
  val io = IO(new Bundle {
    val in: RegFDIO = Flipped(new RegFDIO)

    val out: RegFDIO = new RegFDIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc   -> io.out.pc,
    io.in.inst -> io.out.inst
  ))
}