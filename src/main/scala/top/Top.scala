package xodus.top

import chisel3._
import xodus.core.{Core},
       xodus.memory.Memory,
       xodus.configs.Configs,
       xodus.io._


class Top extends Module with Configs {
  // Modules
  val core: CoreIO   = Module(new Core).io
  val iMem: MemoryIO = Module(new Memory).io


  /********************
   * Interconnections *
   ********************/

  Seq(
    core.iMem -> iMem,
  ).map(
    x => x._2 <> x._1
  )
}
