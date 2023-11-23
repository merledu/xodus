package xodus.sram

import chisel3._
import xodus.configs.Configs


class SRAMReqIO extends Bundle with Configs {
  val write: Bool = Input(Bool())
  val wmask: UInt = Input(UInt(WMASK_WIDTH.W))
  val addr : UInt = Input(UInt(ADDR_WIDTH.W))
  val data : UInt = Input(UInt(XLEN.W))
}


class SRAMRespIO extends Bundle with Configs {
  val data: UInt = Output(UInt(XLEN.W))
}