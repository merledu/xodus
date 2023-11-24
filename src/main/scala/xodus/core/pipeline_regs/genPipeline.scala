package xodus.core.pipeline_regs

import chisel3._


object genPipeline {
  def apply(ports: Seq[(Data, Data)]): Unit = {
    ports.foreach(
      x => {
        val pipeline_reg = Reg(chiselTypeOf(x._1))

        pipeline_reg := x._1
        x._2         := pipeline_reg
      }
    )
  }
}