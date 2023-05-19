package xodus.top

import chisel3._
import xodus.core.{Core},
       xodus.memory.Memory,
       xodus.configs.Configs,
       xodus.io._


class Top extends Module with Configs {
  val io: TopIO = IO(new TopIO)

  // Modules
  val core: CoreIO   = Module(new Core).io
  val iMem: MemoryIO = Module(new Memory(Data=false)).io


  /********************
   * Interconnections *
   ********************/

  iMem <> core.iMem



  // Debug
  if (Debug) {
    io.debug.get.core.pc       <> core.debug.get.pc
    io.debug.get.core.iMemJunc <> core.debug.get.iMemJunc
    io.debug.get.iMem.resp     <> iMem.resp
  }
}
