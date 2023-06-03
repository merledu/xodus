package core.pipeline_regs

import chisel3._


object genPipeline {
  def apply(ports: Seq[(Data, Data)]): Unit = {
    ports.foreach(
      x => {
        val pipelineReg = Reg(chiselTypeOf(x._1))

        pipelineReg := x._1
        x._2        := pipelineReg
      }
    )
  }
}
