package xodus.sram

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class DMemIO extends Bundle with Configs {
  val clk0  : Bool = Input(Bool())
  val csb0  : Bool = Input(Bool())
  val web0  : Bool = Input(Bool())
  val wmask0: UInt = Input(UInt(WMASK_WIDTH.W))
  val addr0 : UInt = Input(UInt(ADDR_WIDTH.W))
  val din0  : UInt = Input(UInt(XLEN.W))
  val clk1  : Bool = Input(Bool())
  val csb1  : Bool = Input(Bool())
  val web1  : Bool = Input(Bool())
  val wmask1: UInt = Input(UInt(WMASK_WIDTH.W))
  val addr1 : UInt = Input(UInt(ADDR_WIDTH.W))
  val din1  : UInt = Input(UInt(XLEN.W))

  val dout0: UInt = Input(UInt(XLEN.W))
  val dout1: UInt = Input(UInt(XLEN.W))
}


class DMem(dmem_file: Option[String]) extends BlackBox(
  Map("IFILE" -> (if (dmem_file.isDefined) dmem_file.get else ""))
) with HasBlackBoxResource {
  val io: DMemIO = IO(new DMemIO)

  addResource("/DMem.v")
}
