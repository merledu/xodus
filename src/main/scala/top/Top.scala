package xodus.top

import chisel3._
import xodus.core.{Core},
       xodus.memory.Memory,
       xodus.configs.Configs,
       xodus.debug_io.DebugTop,
       xodus.core.CoreIO,
       xodus.memory.MemoryIO


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
    io.debug.get.core.iMemJunc <> core.debug.get.iMemJunc
    io.debug.get.core.regFD    <> core.debug.get.regFD
    io.debug.get.iMem.resp     <> iMem.resp
    io.debug.get.core.decoder  <> core.debug.get.decoder
    io.debug.get.core.regFile  <> core.debug.get.regFile
  }
}
