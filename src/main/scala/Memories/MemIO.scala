package Memories

import chisel3._


class MemReqIO(
  params  :Map[String, Int],
  dataMem :Boolean
) extends Bundle {
  val addr: UInt = Input(UInt(params("sram")("depth").W))
  val data: UInt = if (dataMem) {
    Some(Input(UInt(params("XLEN").W)))
  } else None
  val store: Bool = if (dataMem) {
    Some(Input(Bool()))
  } else None
}


class MemRespIO(params :Map[String, Int]) extends Bundle {
  val data: UInt = Input(UInt(params("XLEN").W))
}
