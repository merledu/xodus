package xodus.debug_io

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.fetch_stage.{PCIO, IMemJuncIO},
       xodus.core.decode_stage.{DecoderIO, RegFileIO, Enables},
       xodus.core.execute_stage.ALUIO,
       xodus.core.pipeline_regs.{RegFDIO, RegDEIO, RegEMIO},
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


class DebugDecoder extends Bundle {
  val opcode     : UInt      = new DecoderIO().opcode
  val rAddr      : Vec[UInt] = new DecoderIO().rAddr
  val funct3     : UInt      = new DecoderIO().funct3
  val funct7_imm7: UInt      = new DecoderIO().funct7_imm7
  val imm        : SInt      = new DecoderIO().imm
}


class DebugRegFile extends Bundle {
  val read: Vec[SInt] = new RegFileIO().read
}


class DebugControlUnit extends Bundle {
  val en: Enables = new Enables
}


class DebugRegDE extends Bundle {
  val out: RegDEIO = new RegDEIO
}


class DebugALU extends Bundle {
  val out: SInt = new ALUIO().out
}


class DebugRegEM extends Bundle {
  val out: RegEMIO = new RegEMIO
}


class DebugCore extends Bundle {
  val pc      : DebugPC          = new DebugPC
  val iMemJunc: DebugIMemJunc    = new DebugIMemJunc
  val regFD   : DebugRegFD       = new DebugRegFD
  val decoder : DebugDecoder     = new DebugDecoder
  val regFile : DebugRegFile     = new DebugRegFile
  val cu      : DebugControlUnit = new DebugControlUnit
  val regDE   : DebugRegDE       = new DebugRegDE
  val alu     : DebugALU         = new DebugALU
  val regEM   : DebugRegEM       = new DebugRegEM
}


class DebugIMem extends Bundle {
  val resp: MemRespIO = new MemoryIO().resp
}


class DebugTop extends Bundle {
  val core: DebugCore = new DebugCore
  val iMem: DebugIMem = new DebugIMem
}
