package sram

import chisel3._,
       chisel3.util._
import configs.Configs


class SRAMIO extends Bundle with Configs {
  val clk0  : Bool = Input(Bool())
  val csb0  : Bool = Input(Bool())
  val web0  : Bool = Input(Bool())
  val addr0 : UInt = Input(UInt(AddrWidth.W))
  val wmask0: UInt = Input(UInt(WMaskWidth.W))
  val din0  : UInt = Input(UInt(XLEN.W))

  val dout0: UInt = Output(UInt(XLEN.W))
}


class SRAM(HexFile :Option[String]) extends BlackBox(
  Map("IFILE" -> (if (HexFile.isDefined) HexFile.get else ""))
) with HasBlackBoxResource {
  val io: SRAMIO = IO(new SRAMIO)

  addResource("/SRAM.v")
}
