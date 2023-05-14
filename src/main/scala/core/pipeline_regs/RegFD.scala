package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.fetch_stage.{PC_IO, InstMemJuncIO}


class RegFD_IO extends Bundle with Configs {
  // Input ports
  val pcIn  : UInt = Flipped(new PC_IO().pc)
  val instIn: UInt = Flipped(new InstMemJuncIO().instOut)
  
  // Output ports
  val pcOut  : UInt = Flipped(pcIn)
  val instOut: UInt = Flipped(instIn)
}


class RegFD extends Module with Configs {
  val io: RegFD_IO = IO(new RegFD_IO)

  // Pipeline
  genPipeline(Seq(
    io.pcIn   -> io.pcOut,
    io.instIn -> io.instOut
  )).map {
    x =>
      x._2 := x._3
      x._1 := x._2
  }
}
