package xodus.core.pipeline_regs

import chisel3._
import xodus.configs.Configs,
       xodus.core.decode_stage.{RegFileCtrl, ALUCtrl, DMemCtrl, RegisterFileIO}


class RegDEIO extends Bundle with Configs {
  val pc           : UInt        = new RegFDIO().pc
  val rd_addr      : UInt        = Output(UInt(REG_ADDR_WIDTH.W))
  val int_data     : Vec[SInt]   = Output(Vec(new RegisterFileIO().int_read.length + 1, SInt(XLEN.W)))
  val reg_file_ctrl: RegFileCtrl = new RegFileCtrl
  val alu_ctrl     : ALUCtrl     = new ALUCtrl
  val dmem_ctrl    : DMemCtrl    = new DMemCtrl
}


class RegDE extends Module {
  val io = IO(new Bundle {
    val in: RegDEIO = Flipped(new RegDEIO)

    val out: RegDEIO = new RegDEIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc            -> io.out.pc,
    io.in.rd_addr       -> io.out.rd_addr,
    io.in.int_data      -> io.out.int_data,
    io.in.reg_file_ctrl -> io.out.reg_file_ctrl,
    io.in.alu_ctrl      -> io.out.alu_ctrl,
    io.in.dmem_ctrl     -> io.out.dmem_ctrl
  ))
}