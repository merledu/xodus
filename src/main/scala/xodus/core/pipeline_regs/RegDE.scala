package xodus.core.pipeline_regs

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.{RegFileCtrl, ALUCtrl, DMemCtrl}


class RegDEIO extends Bundle with Configs {
  val pc           : UInt        = Output(UInt(XLEN.W))
  val r_addr       : Vec[UInt]   = Output(Vec(3, UInt(REG_ADDR_WIDTH.W)))
  val int_data     : Vec[SInt]   = Output(Vec(3, SInt(XLEN.W)))
  val reg_file_ctrl: RegFileCtrl = new RegFileCtrl
  val alu_ctrl     : ALUCtrl     = new ALUCtrl
  val dmem_ctrl    : DMemCtrl    = new DMemCtrl
}


class RegDE extends Module with Configs {
  val io = IO(new Bundle {
    val in      : RegDEIO   = Flipped(new RegDEIO)
    val fwd_sel : Vec[UInt] = Input(Vec(2, UInt(2.W)))
    val fwd_data: Vec[Vec[SInt]] = Input(Vec(2, Vec(2, SInt(XLEN.W))))

    val out: RegDEIO = new RegDEIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc            -> io.out.pc,
    io.in.r_addr        -> io.out.r_addr,
    io.in.int_data(2)   -> io.out.int_data(2),
    io.in.reg_file_ctrl -> io.out.reg_file_ctrl,
    io.in.alu_ctrl      -> io.out.alu_ctrl,
    io.in.dmem_ctrl     -> io.out.dmem_ctrl
  ) ++ Seq(
    MuxLookup(io.fwd_sel(0), io.in.int_data(0))(Seq(
      io.fwd_data(0)(0),
      io.fwd_data(0)(1)
    ).zipWithIndex.map(x => (x._2 + 1).U -> x._1)) -> io.out.int_data(0),
    MuxLookup(io.fwd_sel(1), io.in.int_data(1))(Seq(
      io.fwd_data(1)(0),
      io.fwd_data(1)(1)
    ).zipWithIndex.map(x => (x._2 + 1).U -> x._1)) -> io.out.int_data(1),
  ))
}