package xodus.debug_io

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.fetch_stage.{PCIO, IMemJuncIO},
       xodus.core.pipeline_regs.RegFDIO,
       xodus.memory.{MemReqIO, MemRespIO, MemoryIO}


class DebugPC extends Bundle {
  val addr: UInt = new PCIO().addr
  val pc  : UInt = new PCIO().pc
}


class DebugIMemJunc extends Bundle {
  val inst   : UInt     = new IMemJuncIO().inst
  val iMemReq: MemReqIO = Flipped(new IMemJuncIO().iMemReqResp.req)
}


class DebugRegFD extends Bundle {
  val out: RegFDIO = new RegFDIO
}


class DebugCore extends Bundle {
  val pc      : DebugPC       = new DebugPC
  val iMemJunc: DebugIMemJunc = new DebugIMemJunc
  val regFD   : DebugRegFD    = new DebugRegFD
}


class DebugIMem extends Bundle {
  val resp: MemRespIO = new MemoryIO().resp
}


class DebugTop extends Bundle {
  val core: DebugCore = new DebugCore
  val iMem: DebugIMem = new DebugIMem
}
