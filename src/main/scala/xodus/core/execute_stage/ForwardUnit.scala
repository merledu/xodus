package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.RegFileCtrl


class ForwardUnitIO extends Bundle with Configs {
  val r_addr       : Vec[UInt]        = Input(Vec(4, UInt(REG_ADDR_WIDTH.W)))
  val reg_file_ctrl: Vec[RegFileCtrl] = Flipped(Vec(2, new RegFileCtrl))

  val fwd_sel: Vec[UInt] = Output(Vec(2, UInt(2.W)))
}


class ForwardUnit extends RawModule {
  val io: ForwardUnitIO = IO(new ForwardUnitIO)


  /*** Interconnections ***/

  io.fwd_sel(0) := MuxCase(0.U, Seq(
    (io.reg_file_ctrl(0).int_write && io.r_addr(2).orR && (io.r_addr(0) === io.r_addr(2))) -> 1.U,  // EM
    (io.reg_file_ctrl(1).int_write && io.r_addr(3).orR && (io.r_addr(0) === io.r_addr(3))) -> 2.U,  // MW
  ))
  io.fwd_sel(1) := MuxCase(0.U, Seq(
    (io.reg_file_ctrl(0).int_write && io.r_addr(2).orR && (io.r_addr(1) === io.r_addr(2))) -> 1.U,  // EM
    (io.reg_file_ctrl(1).int_write && io.r_addr(3).orR && (io.r_addr(1) === io.r_addr(3))) -> 2.U,  // MW
  ))
}