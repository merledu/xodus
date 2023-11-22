package xodus.core.fetch_stage

import chisel3._
import xodus.configs.Configs/*,
       xodus.core.decode_stage.PCCtrl*/


class PCIO extends Bundle with Configs {
  val pc: UInt = Output(UInt(XLEN.W))
}


class PC extends Module with Configs {
  val io: PCIO = IO(new PCIO)

  // Program Counter
  val pc: UInt = RegInit(0.U(XLEN.W))


  /*******************
  * Interconnections *
  *******************/

  pc    := pc + 4.U
  io.pc := pc
}

//class PCIO extends Bundle with Configs {
//  val stall: Bool = Flipped(new PCCtrl().stall)
//
//  val pc: UInt = Output(UInt(XLEN.W))
//}
//
//
//class PC extends Module with Configs {
//  val io: PCIO = IO(new PCIO)
//
//  // Program Counter
//  val pc: UInt = RegInit(0.U(XLEN.W))
//
//
//  /********************
//   * Interconnections *
//   ********************/
//
//  pc    := Mux(io.stall, pc, pc + 4.U)
//  io.pc := pc
//}
