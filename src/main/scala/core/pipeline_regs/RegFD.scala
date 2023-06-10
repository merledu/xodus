package core.pipeline_regs

import chisel3._
import configs.Configs,
       core.fetch_stage.{PCIO, IMemInterfaceIO}


class RegFDIO extends Bundle with Configs {
  val pc  : UInt = new PCIO().pc
  val inst: UInt = new IMemInterfaceIO().inst
}


class RegFD extends Module {
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
