package xodus.decode_stage

import chisel3._, chisel3.util._
import xodus.configs._


class RegFileIO extends Bundle with Configs {
  val rAddr : Vec[UInt]   = Input(Vec(3, UInt(RegAddrWidth.W)))
  val rdData: Valid[SInt] = Flipped(Valid(SInt(XLEN.W)))

  val rsData: Vec[SInt] = Output(Vec(2, SInt(XLEN.W)))
}


class RegFile extends Module with Configs {
  val io: RegFileIO = IO(new RegFileIO)

  val addrWires: Map[String, UInt] = (
    for ((addr, i) <- Seq("rdAddr", "rs1Addr", "rs2Addr").view.zipWithIndex)
      yield addr -> io.rAddr(i)
  ).toMap

  val regFile: Vec[SInt] = Reg(Vec(XLEN, SInt(XLEN.W)))
 
  // Data is written when valid bit is high
  when (io.rdData.valid && addrWires("rdAddr").orR) {
    regFile(addrWires("rdAddr")) := io.rdData.bits
  }

  // Reading from registers
  for (i <- 0 until io.rsData.length) {
    io.rsData(i) := Mux(
      addrWires(s"rs${i + 1}Addr").orR,
      regFile(addrWires(s"rs${i + 1}Addr")),
      0.S
    )
  }



  // TODO: Debug Section
  //if (debug) {
  //  val debug_addrWires_rdAddr : UInt = dontTouch(WireInit(io.rAddr(0)))
  //  val debug_rdData           : SInt = dontTouch(WireInit(io.rdData.bits))
  //  val debug_addrWires_rs1Addr: UInt = dontTouch(WireInit(io.rAddr(1)))
  //  val debug_addrWires_rs2Addr: UInt = dontTouch(WireInit(io.rAddr(2)))
  //  val debug_wrEn             : Bool = dontTouch(WireInit(io.rdData.valid))
  //} else None
}
