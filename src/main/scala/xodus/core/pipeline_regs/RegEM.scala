package xodus.core.pipeline_regs

import chisel3._
import xodus.configs.Configs,
       xodus.core.decode_stage.{RegFileCtrl, DMemCtrl}


class RegEMIO extends Bundle with Configs {
  val rd_addr      : UInt        = Output(UInt(REG_ADDR_WIDTH.W))
  val store_data   : SInt        = Output(SInt(XLEN.W))
  val reg_file_ctrl: RegFileCtrl = new RegFileCtrl
  val alu          : SInt        = Output(SInt(XLEN.W))
  val dmem_ctrl    : DMemCtrl    = new DMemCtrl
}


class RegEM extends Module {
  val io = IO(new Bundle {
    val in: RegEMIO = Flipped(new RegEMIO)

    val out: RegEMIO = new RegEMIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.rd_addr       -> io.out.rd_addr,
    io.in.store_data    -> io.out.store_data,
    io.in.reg_file_ctrl -> io.out.reg_file_ctrl,
    io.in.alu           -> io.out.alu,
    io.in.dmem_ctrl     -> io.out.dmem_ctrl
  ))
}
