package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class PCIO extends Bundle with Configs {
  val jump: Valid[UInt] = Flipped(Valid(UInt(XLEN.W)))

  val pc: UInt = Output(UInt(XLEN.W))
}


class PC extends Module with Configs {
  val io: PCIO = IO(new PCIO)

  // Program Counter
  val pc: UInt = RegInit(0.U(XLEN.W))


  /*** Interconnections ***/

  pc    := Mux(io.jump.valid, io.jump.bits, pc + 4.U)
  io.pc := pc
}