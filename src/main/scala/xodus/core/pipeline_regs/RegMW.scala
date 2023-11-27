package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.RegFileCtrl


class RegMWIO extends Bundle with Configs {
  val rd_addr      : UInt        = Output(UInt(REG_ADDR_WIDTH.W))
  val reg_file_ctrl: RegFileCtrl = new RegFileCtrl
  val alu          : SInt        = Output(SInt(XLEN.W))
  val load         : Valid[SInt] = Valid(SInt(XLEN.W))
}


class RegMW extends Module {
  val io = IO(new Bundle {
    val in: RegMWIO = Flipped(new RegMWIO)

    val out: RegMWIO = new RegMWIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.rd_addr       -> io.out.rd_addr,
    io.in.reg_file_ctrl -> io.out.reg_file_ctrl,
    io.in.alu           -> io.out.alu,
    io.in.load          -> io.out.load
  ))
}