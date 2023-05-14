package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class PC_IO extends Bundle with Configs {
  // Input ports
  
  // Output ports
  val addr: UInt = Output(UInt(MemDepth.W))
  val pc  : UInt = Output(UInt(XLEN.W))
}


class PC extends Module with Configs {
  val io: PC_IO = IO(new PC_IO)

  // Program Counter
  val pc: UInt = RegInit(0.U(XLEN.W))

  // Wires
  val uintWires: Map[String, UInt] = Map(
    "addr" -> pc(MemDepth - 1, 2),
    "npc"  -> (pc + 4.U)
  )


  /********************
   * Interconnections *
   ********************/

  Seq(
    uintWires("addr") -> io.addr,
    pc                -> io.pc,
    uintWires("npc")  -> pc
  ).map(
    x => x._2 := x._1
  )



  // Debug
  if (Debug) {
    val debug_pc  : UInt = dontTouch(WireInit(pc))
    val debug_addr: UInt = dontTouch(WireInit(uintWires("addr")))
    val debug_npc : UInt = dontTouch(WireInit(uintWires("npc")))
  }
}
