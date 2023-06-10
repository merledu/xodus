//package core.pipeline_regs
//
//import chisel3._,
//       chisel3.util._
//import configs.Configs,
//       core.decode_stage.RegFileEN,
//       core.memory_stage.DMemAlignerIO
//
//
//class RegMWIO extends Bundle with Configs {
//  val rAddr    : Vec[UInt]   = new RegEMIO().rAddr
//  val regFileEN: RegFileEN   = new RegEMIO().regFileEN
//  val alu      : SInt        = new RegEMIO().alu
//  val load     : Valid[SInt] = new DMemAlignerIO().load
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
//    io.in.rAddr     -> io.out.rAddr,
//    io.in.regFileEN -> io.out.regFileEN,
//    io.in.alu       -> io.out.alu,
//    io.in.load      -> io.out.load
//  ))
//}
