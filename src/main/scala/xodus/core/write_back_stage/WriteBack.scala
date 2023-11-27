package xodus.core.write_back_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class WriteBackIO extends Bundle with Configs {
  val alu : SInt        = Input(SInt(XLEN.W))
  val load: Valid[SInt] = Flipped(Valid(SInt(XLEN.W)))

  val out: SInt = Output(SInt(XLEN.W))
}


class WriteBack extends RawModule {
  val io: WriteBackIO = IO(new WriteBackIO)


   /*** Interconnections ***/

  // Write Back Selection
  // Default: ALU output
  io.out := Mux(io.load.valid, io.load.bits, io.alu)
}
