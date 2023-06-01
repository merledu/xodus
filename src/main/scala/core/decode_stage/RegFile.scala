package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs._


class RegFileIO extends Bundle with Configs {
  val rAddr: Vec[UInt]   = Flipped(new DecoderIO().rAddr)
  val write: Valid[SInt] = Flipped(Valid(SInt(XLEN.W)))

  val read: Vec[SInt] = Output(Vec(2, SInt(XLEN.W)))
}


class RegFile extends Module with Configs {
  val io: RegFileIO = IO(new RegFileIO)

  // Register File
  val regFile: Vec[SInt] = Reg(Vec(XLEN, SInt(XLEN.W)))
 

  /********************
   * Interconnections *
   ********************/

  // Write to Register File
  when (io.write.valid && io.rAddr(0).orR) {
    regFile(io.rAddr(0)) := io.write.bits
  }

  // Read from Register File
  for (i <- 0 until io.read.length) {
    io.read(i) := Mux(io.rAddr(i + 1).orR, regFile(io.rAddr(i + 1)), 0.S)
  }
}
