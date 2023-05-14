package xodus.io

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class PCIO extends Bundle with Configs {
  val pc  : UInt = Output(UInt(XLEN.W))
  val addr: UInt = Output(UInt(MemDepth.W))
}


class InstMemJuncIO extends Bundle with Configs {
  val addr      : UInt     = Flipped(new PCIO().addr)
  val inst      : UInt     = Output(UInt(XLEN.W))
  val iMemReqRsp: MemoryIO = Flipped(new MemoryIO)
}


class RegFDIO extends Bundle with Configs {
  val pc  : UInt = Flipped(new PCIO().pc)
  val inst: UInt = Flipped(new InstMemJuncIO().inst)
}


class CoreIO extends Bundle {
  val iMem: MemoryIO = Flipped(new MemoryIO)
  //val dMem: MemoryIO = Flipped(new MemoryIO)
}


class MemReqIO extends Bundle with Configs {
  val addr: Valid[UInt] = Flipped(Valid(UInt(MemDepth.W)))
  val data: Valid[UInt] = Flipped(Valid(UInt(XLEN.W)))
}


class MemRespIO extends Bundle with Configs {
  val data: UInt = Output(UInt(XLEN.W))
}


class MemoryIO extends Bundle {
  val req: MemReqIO  = new MemReqIO
  val rsp: MemRespIO = new MemRespIO
}
