package xodus.io

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class PCIO extends Bundle with Configs {
  val pc  : UInt = Output(UInt(XLEN.W))
  val addr: UInt = Output(UInt(MemDepth.W))
}


class IMemJuncIO extends Bundle with Configs {
  val addr      : UInt     = Flipped(new PCIO().addr)

  val inst      : UInt     = Output(UInt(XLEN.W))

  val iMemReqResp: MemoryIO = Flipped(new MemoryIO)
}


class RegFDIO extends Bundle with Configs {
  val pc  : UInt = Flipped(new PCIO().pc)
  val inst: UInt = Flipped(new IMemJuncIO().inst)
}


class CoreIO extends Bundle with Configs {
  val iMem: MemoryIO = Flipped(new MemoryIO)
  //val dMem: MemoryIO = Flipped(new MemoryIO)


  val debug: Option[DebugIO] = if (Debug) Some(new DebugIO) else None
}


class MemReqIO extends Bundle with Configs {
  val addr: Valid[UInt] = Flipped(Valid(UInt(MemDepth.W)))
  val data: Valid[UInt] = Flipped(Valid(UInt(XLEN.W)))
}


class MemRespIO extends Bundle with Configs {
  val data: UInt = Output(UInt(XLEN.W))
}


class MemoryIO extends Bundle {
  val req : MemReqIO  = new MemReqIO

  val resp: MemRespIO = new MemRespIO
}


class TopIO extends Bundle with Configs {
  val debug: DebugIO = new CoreIO().debug.get
}




// Debug
class DebugPC extends Bundle with Configs {
  val addr: UInt = new PCIO().addr
  val pc  : UInt = new PCIO().pc
}


class DebugIMemJunc extends Bundle with Configs {
  val inst   : UInt     = new IMemJuncIO().inst
  val iMemReq: MemReqIO = Flipped(new IMemJuncIO().iMemReqResp.req)
}


class DebugIO extends Bundle with Configs {
  val pc      : DebugPC       = new DebugPC
  val iMemJunc: DebugIMemJunc = new DebugIMemJunc
}
