package xodus.memory

import chisel3._
import xodus.configs.Configs


class MemReqIO extends Bundle with Configs {
  val addr: UInt      = Input(UInt(MemDepth.W))
  val en  : Vec[Bool] = Input(Vec(2, Bool()))
  val data: UInt      = Input(UInt(XLEN.W))
}


class MemRespIO extends Bundle with Configs {
  val data: UInt = Input(UInt(XLEN.W))
}
