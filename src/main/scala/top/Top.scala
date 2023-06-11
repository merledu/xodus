package top

import chisel3._
import core.Core,
       sram.{SRAMTop, SRAMTopIO},
       configs.Configs,
       debug_io.DebugTop,
       core.CoreIO


class TopIO extends Bundle with Configs {
  //val debug: Option[DebugTop] = if (Debug) Some(new DebugTop) else None
}


class Top(HexFiles:Seq[Option[String]]) extends Module with Configs {
  val io: TopIO = IO(new TopIO)

  // Modules
  val core: CoreIO    = Module(new Core).io
  val iMem: SRAMTopIO = Module(new SRAMTop(HexFiles(0))).io
  val dMem: SRAMTopIO = Module(new SRAMTop(HexFiles(1))).io


  /********************
   * Interconnections *
   ********************/

  iMem <> core.iMem
  dMem <> core.dMem



  // Debug
  if (Debug) {
    //io.debug.get.core.pc      <> core.debug.get.pc
    //io.debug.get.core.iMem    <> core.debug.get.iMem
    //io.debug.get.iMem.resp    <> iMem.resp
    //io.debug.get.core.regFD   <> core.debug.get.regFD
    //io.debug.get.core.decoder <> core.debug.get.decoder
    //io.debug.get.core.regFile <> core.debug.get.regFile
    //io.debug.get.core.cu      <> core.debug.get.cu
    //io.debug.get.core.regDE   <> core.debug.get.regDE
    //io.debug.get.core.alu     <> core.debug.get.alu
    //io.debug.get.core.regEM   <> core.debug.get.regEM
    //io.debug.get.dMem.resp        <> dMem.resp
    //io.debug.get.core.regMW       <> core.debug.get.regMW
    //io.debug.get.core.wb          <> core.debug.get.wb
  }
}
