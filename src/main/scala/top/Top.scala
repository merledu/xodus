package top

import chisel3._
import core.{Core},
       memory.Memory,
       configs.Configs,
       debug_io.DebugTop,
       core.CoreIO,
       memory.MemoryIO


class TopIO extends Bundle with Configs {
  val debug: Option[DebugTop] = if (Debug) Some(new DebugTop) else None
}


class Top extends Module with Configs {
  val io: TopIO = IO(new TopIO)

  // Modules
  val core: CoreIO   = Module(new Core).io
  val iMem: MemoryIO = Module(new Memory).io


  /********************
   * Interconnections *
   ********************/

  iMem <> core.iMem



  // Debug
  if (Debug) {
    io.debug.get.core.pc       <> core.debug.get.pc
    io.debug.get.core.regFD    <> core.debug.get.regFD
    io.debug.get.iMem.resp     <> iMem.resp
    io.debug.get.core.decoder  <> core.debug.get.decoder
    io.debug.get.core.regFile  <> core.debug.get.regFile
    io.debug.get.core.cu       <> core.debug.get.cu
    io.debug.get.core.regDE    <> core.debug.get.regDE
    io.debug.get.core.alu      <> core.debug.get.alu
    io.debug.get.core.regEM    <> core.debug.get.regEM
  }
}
