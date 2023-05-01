package xodus.core.pipeline_regs

import chisel3._
import xodus.configs.Configs


class RegMW_IO extends Bundle with Configs {
  // Input ports
  val aluIn: SInt = Flipped(new RegEM_IO().aluOut)
  // Output ports
  val aluOut: SInt = Flipped(aluIn)
}


class RegMW extends Module with Configs {
  val io: RegMW_IO = IO(new RegMW_IO)

  // Pipeline
  genPipeline(Seq(
    io.aluIn -> io.aluOut
  )).map {
    x =>
      x._2 := x._3
      x._1 := x._2
  }
}
