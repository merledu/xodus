package Memories

import chisel3._


class MemRouterIO(
  params :Map[String, Map[String, Int]],
  ram    :String
) extends Bundle {
  // Input pins
  // - Core Interface
  val addr: UInt = if (ram == "inst") {
    Some(Input(UInt(params("sram")("depth").W)))
  } else if (ram == "data") {
    Some(Input(SInt(params("sram")("depth").W)))
  } else None
  val loadData: UInt = Input(UInt(params("rv32i")("XLEN").W))

//  InstMem
//  addrIn = Input(UInt(params("MDEPTH").W))
//  stallEn = Input(Bool())
//  jumpStall = Input(Bool())
//  instIn = Input(UInt(params("XLEN").W))
//  stallInstIn = Input(UInt(params("XLEN").W))
//
//  DataMem
//  aluAddr = Input(SInt(params("depth")))
//  rs2Data = Input(SInt(params("XLEN")))
//  loadData = Input(SInt(params("XLEN")))
//  load/store_en = Input(Bool())
}


class MemRouter(
  params :Map[String, Map[String, Int]],
  ram    :String
) extends Module {
  val ramMap: Map[String, ] = Map(
    "instMem" -> ,
    "dataMem" ->
    )
}
