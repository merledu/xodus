package xodus.core.fetch_stage

import chisel3._, chisel3.util._
import xodus.configs.Configs


class InstMemRouterIO extends Bundle with Configs {
  // Input ports
  val addrIn: UInt = Input(UInt(MemDepth.W))
  val instIn: UInt = Input(UInt(XLEN.W))

  // Output ports
  val addrOut: UInt = Flipped(addrIn)
  val instOut: UInt = Flipped(instIn)
}


class InstMemRouter extends Module {
  val io: InstMemRouterIO = IO(new InstMemRouterIO)

  // Interconnections
  Seq()
}
