//package core.write_back_stage
//
//import chisel3._,
//       chisel3.util._
//import configs.Configs,
//       core.pipeline_regs.RegMWIO
//
//
//class WriteBackIO extends Bundle with Configs {
//  val alu : SInt        = Flipped(new RegMWIO().alu)
//  val load: Valid[SInt] = Flipped(new RegMWIO().load)
//
//  val out: SInt = Output(SInt(XLEN.W))
//}
//
//
//class WriteBack extends RawModule {
//  val io: WriteBackIO = IO(new WriteBackIO)
//
//
//  /********************
//   * Interconnections *
//   ********************/
//
//  // Write Back Selection
//  // Default: ALU output
//  io.out := Mux(io.load.valid, io.load.bits, io.alu)
//}
