package xodus.core.decode_stage

import chisel3._
import xodus.configs.Configs


class RegisterFileIO extends Bundle with Configs {
  val r_addr    : Vec[UInt]   = Flipped(new DecoderIO().r_addr)
  val write_data: SInt        = Input(SInt(XLEN.W))
  val ctrl      : RegFileCtrl = Flipped(new RegFileCtrl)

  val int_read: Vec[SInt] = Output(Vec(2, SInt(XLEN.W)))
}


class RegisterFile extends Module with Configs {
  val io: RegisterFileIO = IO(new RegisterFileIO)

  // Register File
  val int_reg_file: Vec[SInt] = Reg(Vec(XLEN, SInt(XLEN.W)))
 

   /*** Interconnections ***/

  // Hardcode x0
  int_reg_file(0) := 0.S

  // Write to Register File
  when (io.ctrl.int_write && io.r_addr(0).orR) {
    int_reg_file(io.r_addr(0)) := io.write_data
  }

  // Read from Register File
  for (i <- 0 until io.int_read.length) {
    io.int_read(i) := int_reg_file(io.r_addr(i + 1))
  }
}
