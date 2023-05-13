package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs._


class RegFileIO extends Bundle with Configs {
  // Input ports
  val rAddr = Flipped(new DecoderIO().rAddr)
  val write = Flipped(Valid(SInt(XLEN.W)))

  // Output ports
  val read = Output(Vec(2, SInt(XLEN.W)))
}


class RegFile extends Module with Configs {
  val io = IO(new RegFileIO)

  // Wires
  val addrWires = (
    for (i <- 1 to 2)
      yield s"rs${i}" -> io.rAddr(i)
  ).toMap ++ Map(
    "rd" -> io.rAddr(0)
  )

  // Register File
  val regFile = Reg(Vec(XLEN, SInt(XLEN.W)))
 

  /********************
   * Interconnections *
   ********************/

  // Data is written to Register File
  // when valid bit is high and rd is not 0
  when (io.write.valid && addrWires("rd").orR) {
    regFile(addrWires("rd")) := io.write.bits
  }

  // Reading from Register File
  for (i <- 0 until io.read.length) {
    io.read(i) := Mux(
      addrWires(s"rs${i + 1}").orR,
      regFile(addrWires(s"rs${i + 1}")),
      0.S
    )
  }



  // Debug
  if (Debug) {
    val debug_rd          = dontTouch(WireInit(addrWires("rd")))
    val debug_write_bits  = dontTouch(WireInit(io.write.bits))
    val debug_rs1         = dontTouch(WireInit(addrWires("rs1")))
    val debug_rs2         = dontTouch(WireInit(addrWires("rs2")))
    val debug_write_valid = dontTouch(WireInit(io.write.valid))
  }
}
