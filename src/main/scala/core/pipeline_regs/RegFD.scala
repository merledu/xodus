package core.pipeline_regs

import chisel3._
import configs.Configs,
       core.fetch_stage.{PCIO, IMemInterfaceIO},
       core.decode_stage.RegFDCtrl


class RegFDIO extends Bundle with Configs {
  val pc  : UInt = new PCIO().pc
  val inst: UInt = new IMemInterfaceIO().inst
}


class RegFD extends Module {
  val io = IO(new Bundle {
    val in   : RegFDIO = Flipped(new RegFDIO)
    val stall: Bool    = Flipped(new RegFDCtrl().stall)

    val out: RegFDIO = new RegFDIO
  })


  // Pipeline
  genPipeline(Seq(
    io.in.pc   -> io.out.pc,
    io.in.inst -> io.out.inst
  ).map(
    x => Mux(io.stall, x._2, x._1) -> x._2
  ))
}
