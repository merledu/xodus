package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.fetch_stage.PC_IO


class RegFD_IO extends Bundle with Configs {
  // Input ports
  val pcIn   = Flipped(new PC_IO().pc)
  val addrIn = Flipped(new PC_IO().addr)
  
  // Output ports
  val pcOut   = Flipped(pcIn)
  val addrOut = Flipped(addrIn)
}


class RegFD extends Module with Configs {
  val io = IO(new RegFD_IO)

  // Pipeline
  genPipeline(Seq(
    io.pcIn   -> io.pcOut,
    io.addrIn -> io.addrOut
  )).map {
    x =>
      x._2 := x._3
      x._1 := x._2
  }
}
