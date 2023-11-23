package xodus.sram

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class IMemIO extends Bundle with Configs {
  val clk0  : Bool = Input(Bool())
  val csb0  : Bool = Input(Bool())
  val web0  : Bool = Input(Bool())
  val wmask0: UInt = Input(UInt(WMASK_WIDTH.W))
  val addr0 : UInt = Input(UInt(ADDR_WIDTH.W))
  val din0  : UInt = Input(UInt(XLEN.W))

  val dout0: UInt = Output(UInt(XLEN.W))
}


class IMem(imem_file: Option[String]) extends BlackBox(
  Map("IFILE" -> (if (imem_file.isDefined) imem_file.get else ""))
) with HasBlackBoxResource {
  val io: IMemIO = IO(new IMemIO)

  addResource("/IMem.v")
}
