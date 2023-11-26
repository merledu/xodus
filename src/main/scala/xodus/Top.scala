package xodus

import chisel3._
import core.{Core, CoreIO},
       sram.{IMemTop, IMemTopIO, DMemTop, DMemTopIO}


class Top(mem_files: Seq[Option[String]]) extends Module {
  // Modules
  val core: CoreIO    = Module(new Core).io
  val imem: IMemTopIO = Module(new IMemTop(mem_files.head)).io
  val dmem: DMemTopIO = Module(new DMemTop(mem_files(1))).io


   /*** Interconnections ***/

  imem <> core.imem
  dmem <> core.dmem
}