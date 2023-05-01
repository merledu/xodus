package xodus.core.fetch_stage

import chisel3._, chisel3.util._
import xodus.configs.Configs


class PC_IO extends Bundle with Configs {
  // Input ports
  
  // Output ports
  val addrOut: UInt = Output(UInt(MemDepth.W))
}


class PC extends Module with Configs {
  val io: PC_IO = IO(new PC_IO)

  // Program Counter
  val pc: UInt = RegInit(0.U(XLEN.W))

  // Interconnections
  Seq(
    (io.addrOut, pc(MemDepth - 1, 2)),
    (pc, pc + 4.U)
  ).map(
    x => x._1 := x._2
  )



  // Debug
  if (Debug) {
    val debug_pc: UInt = dontTouch(WireInit(pc))
  }
}
