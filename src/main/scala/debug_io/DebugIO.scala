package debug_io

import chisel3._,
       chisel3.util._
import configs.Configs,
       core.fetch_stage.{PCIO, IMemInterfaceIO},
       core.decode_stage.{DecoderIO, IntRegFileIO, Controls},
       //core.execute_stage.ALUIO,
       //core.memory_stage.DMemAlignerIO,
       //core.write_back_stage.WriteBackIO,
       core.pipeline_regs.{RegFDIO, RegDEIO/*, RegEMIO, RegMWIO*/},
       sram.{SRAMReqIO, SRAMRespIO, SRAMTopIO}


class DebugPC extends Bundle {
  val pc: UInt = new PCIO().pc
}


class DebugIMemInterface extends Bundle {
  val reqValid : Bool      = new SRAMTopIO().req.valid
  val reqBits  : SRAMReqIO = new SRAMTopIO().req.bits
  val respReady: Bool      = Flipped(new SRAMTopIO().resp.ready)
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
  val read: Vec[SInt] = new IntRegFileIO().read
}


class DebugControlUnit extends Bundle {
  val ctrl: Controls = new Controls
}


class DebugRegDE extends Bundle {
  val out: RegDEIO = new RegDEIO
}


//class DebugALU extends Bundle {
//  val out: SInt = new ALUIO().out
//}
//
//
//class DebugRegEM extends Bundle {
//  val out: RegEMIO = new RegEMIO
//}


//class DebugDMemAligner extends Bundle {
//  val load   : Valid[SInt] = new DMemAlignerIO().load
//  val dMemReq: MemReqIO    = Flipped(new MemReqIO)
//}


//class DebugRegMW extends Bundle {
//  val out: RegMWIO = new RegMWIO
//}


//class DebugWriteBack extends Bundle {
//  val out: SInt = new WriteBackIO().out
//}


class DebugCore extends Bundle {
  val pc         : DebugPC            = new DebugPC
  val iMem       : DebugIMemInterface = new DebugIMemInterface
  val regFD      : DebugRegFD         = new DebugRegFD
  val decoder    : DebugDecoder       = new DebugDecoder
  val regFile    : DebugRegFile       = new DebugRegFile
  val cu         : DebugControlUnit   = new DebugControlUnit
  val regDE      : DebugRegDE         = new DebugRegDE
  //val alu        : DebugALU           = new DebugALU
  //val regEM      : DebugRegEM         = new DebugRegEM
  //val dMemAligner: DebugDMemAligner = new DebugDMemAligner
  //val regMW      : DebugRegMW       = new DebugRegMW
  //val wb         : DebugWriteBack   = new DebugWriteBack
}


class DebugIMem extends Bundle {
  val reqReady : Bool      = Flipped(new SRAMTopIO().req.ready)
  val respValid: Bool      = new SRAMTopIO().resp.valid
  val respBits : SRAMRespIO = new SRAMTopIO().resp.bits
}


class DebugDMem extends Bundle {
  val resp: SRAMRespIO = new SRAMRespIO
}


class DebugTop extends Bundle {
  val core: DebugCore = new DebugCore
  val iMem: DebugIMem = new DebugIMem
  //val dMem: DebugDMem = new DebugDMem
}
