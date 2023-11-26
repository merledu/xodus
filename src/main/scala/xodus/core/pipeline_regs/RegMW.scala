package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.core.decode_stage.RegFileCtrl,
       xodus.core.memory_stage.DMemInterfaceIO


class RegMWIO extends Bundle {
  val rd_addr      : UInt        = new RegEMIO().rd_addr
  val reg_file_ctrl: RegFileCtrl = new RegEMIO().reg_file_ctrl
  val alu          : SInt        = new RegEMIO().alu
  val load         : Valid[SInt] = new DMemInterfaceIO().load
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