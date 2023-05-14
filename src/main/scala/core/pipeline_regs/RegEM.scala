//package xodus.core.pipeline_regs
//
//import chisel3._,
//       chisel3.util._
//import xodus.configs.Configs,
//       xodus.core.execute_stage.ALU_IO
//
//
//class RegEM_IO extends Bundle with Configs {
//  // Input ports
//  val aluIn: SInt = Flipped(new ALU_IO().out)
//
//  // Output ports
//  val aluOut: SInt = Flipped(aluIn)
//}
//
//
//class RegEM extends Module with Configs {
//  val io: RegEM_IO = IO(new RegEM_IO)
//
//  // Pipeline
//  genPipeline(Seq(
//    io.aluIn -> io.aluOut
//  )).map {
//    x =>
//      x._2 := x._3
//      x._1 := x._2
//  }
//}
