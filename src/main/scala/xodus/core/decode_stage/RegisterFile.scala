package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class RegisterFileIO extends Bundle with Configs {
  val r_addr: Vec[UInt]   = Flipped(new DecoderIO().r_addr)
  val write : Valid[SInt] = Flipped(Valid(SInt(XLEN.W)))

  val read: Vec[SInt] = Output(Vec(2, SInt(XLEN.W)))
}


class RegisterFile extends Module with Configs {
  val io: RegisterFileIO = IO(new RegisterFileIO)

  // Register File
  val int_reg_file: Vec[SInt] = Reg(Vec(XLEN, SInt(XLEN.W)))
 

   /*** Interconnections ***/

  // Hardcode x0
  int_reg_file(0) := 0.S

  // Write to Register File
  when (io.write.valid && io.r_addr(0).orR) {
    int_reg_file(io.r_addr(0)) := io.write.bits
  }

  // Read from Register File
  for (i <- 0 until io.read.length) {
    io.read(i) := int_reg_file(io.r_addr(i + 1))
  }
}
