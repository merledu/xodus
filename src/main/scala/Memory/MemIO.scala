package Memories

import chisel3._


class MemReqIO(
  params  :Map[String, Map[String, Int]],
  dataMem :Boolean
) extends Bundle {
  val addr: UInt = Input(UInt(params("sram")("depth").W))
  val data = if (dataMem) {
    Some(Input(UInt(params("rv32i")("XLEN").W)))
  } else None
  val store = if (dataMem) {
    Some(Input(Bool()))
  } else None

  println(data.getClass)
}


class MemRespIO(params :Map[String, Int]) extends Bundle {
  val data: UInt = Input(UInt(params("XLEN").W))
}
