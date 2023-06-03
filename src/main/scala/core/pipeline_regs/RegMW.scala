//package core.pipeline_regs
//
//import chisel3._
//import configs.Configs,
//       core.memory_stage.DMemAlignerIO
//
//
//class RegMWIO extends Bundle with Configs {
//  val regFileEN: Bool = new RegEMIO().regFileEN
//  val alu      : SInt = new RegEMIO().alu
//  //val load     : SInt = new DMemAlignerIO().load
//}
//
//
//class RegMW extends Module with Configs {
//  val io = IO(new Bundle {
//    val in : RegMWIO = Flipped(new RegMWIO)
//    val out: RegMWIO = new RegMWIO
//  })
//
//
//  // Pipeline
//  genPipeline(Seq(
//    io.in.regFileEN -> io.out.regFileEN,
//    io.in.alu       -> io.out.alu
//  ))
//}
