package top

import chisel3._
import core.Core,
       sram.{SRAMTop, SRAMTopIO},
       configs.Configs,
       core.CoreIO


class Top(HexFiles:Seq[Option[String]]) extends Module with Configs {
  // Modules
  val core: CoreIO    = Module(new Core).io
  val iMem: SRAMTopIO = Module(new SRAMTop(HexFiles(0))).io
  val dMem: SRAMTopIO = Module(new SRAMTop(HexFiles(1))).io


  /********************
   * Interconnections *
   ********************/

  iMem <> core.iMem
  dMem <> core.dMem
}
