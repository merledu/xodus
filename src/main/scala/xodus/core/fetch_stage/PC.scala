package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class PCIO extends Bundle with Configs {
  val jump = Flipped(Valid(UInt(XLEN.W)))

  val pc = Output(UInt(XLEN.W))
}


class PC extends Module with Configs {
  val io = IO(new PCIO)

  // Program Counter
  val pc = RegInit(0.U(XLEN.W))


  /*** Interconnections ***/

  pc    := Mux(io.jump.valid, io.jump.bits, pc + 4.U)
  io.pc := pc
}