package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class PC_IO extends Bundle with Configs {
  // Input ports
  
  // Output ports
  val addr = Output(UInt(MemDepth.W))
  val pc   = Output(UInt(XLEN.W))
}


class PC extends Module with Configs {
  val io = IO(new PC_IO)

  // Program Counter
  val pc = RegInit(0.U(XLEN.W))


  /********************
   * Interconnections *
   ********************/

  Seq(
    pc(MemDepth - 1, 2) -> io.addr,
    pc                  -> io.pc,
    (pc + 4.U)          -> pc
  ).map(
    x => x._2 := x._1
  )



  // Debug
  if (Debug) {
    val debug_pc: UInt = dontTouch(WireInit(pc))
  }
}
