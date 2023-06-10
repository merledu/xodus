package core.decode_stage

import chisel3._,
       chisel3.util._
import configs.Configs


class IntRegFileIO extends Bundle with Configs {
  val rAddr: Vec[UInt]   = Flipped(new DecoderIO().rAddr)
  val write: Valid[SInt] = Flipped(Valid(SInt(XLEN.W)))

  val read: Vec[SInt] = Output(Vec(2, SInt(XLEN.W)))
}


class IntRegFile extends Module with Configs {
  val io: IntRegFileIO = IO(new IntRegFileIO)

  // Register File
  val regFile: Vec[SInt] = Reg(Vec(XLEN, SInt(XLEN.W)))
 

  /********************
   * Interconnections *
   ********************/

  // Hardcode x0
  regFile(0) := 0.S

  // Write to Register File
  when (io.write.valid && io.rAddr(0).orR) {
    regFile(io.rAddr(0)) := io.write.bits
  }

  // Read from Register File
  for (i <- 0 until io.read.length) {
    io.read(i) := regFile(io.rAddr(i + 1))
  }
}
