package core.decode_stage

import chisel3._,
       chisel3.util._
import configs.Configs


class RegisterFileIO extends Bundle with Configs {
  val rAddr: Vec[UInt]   = Flipped(new DecoderIO().rAddr)
  val write: Valid[SInt] = Flipped(Valid(SInt(XLEN.W)))

  val read: Vec[SInt] = Output(Vec(2, SInt(XLEN.W)))
}


class RegisterFile extends Module with Configs {
  val io: RegisterFileIO = IO(new RegisterFileIO)

  // Register File
  val intRegFile: Vec[SInt] = Reg(Vec(XLEN, SInt(XLEN.W)))
 

  /********************
   * Interconnections *
   ********************/

  // Hardcode x0
  intRegFile(0) := 0.S

  // Write to Register File
  when (io.write.valid && io.rAddr(0).orR) {
    intRegFile(io.rAddr(0)) := io.write.bits
  }

  // Read from Register File
  for (i <- 0 until io.read.length) {
    io.read(i) := intRegFile(io.rAddr(i + 1))
  }
}
