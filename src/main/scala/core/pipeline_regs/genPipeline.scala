package xodus.core.pipeline_regs

import chisel3._


object genPipeline {
  def apply(ports: Seq[(Data, Data)]): Seq[(Data, Data, Data)] = {
    ports.map(
      x => (x._2, Reg(chiselTypeOf(x._1)), x._1)
    )
  }
}
