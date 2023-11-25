package xodus.sram

import chisel3._,
       chisel3.util._


class DMemTopIO extends Bundle {
  val req: Valid[Vec[SRAMReqIO]] = Flipped(Valid(Vec(2, new SRAMReqIO)))

  val resp: Vec[SRAMRespIO] = Vec(2, new SRAMRespIO)
}


class DMemTop(dmem_file: Option[String]) extends Module {
  val io: DMemTopIO = IO(new DMemTopIO)

  // Memory
  val dmem: DMemIO = Module(new DMem(dmem_file)).io


  /*** Interconnections ***/

  Seq(
    clock.asBool  -> Seq(dmem.clk0, dmem.clk1),
    !io.req.valid -> Seq(dmem.csb0, dmem.csb1)  // active-low
  ).foreach(
    x => x._2.foreach(_ := x._1)
  )

  dmem.web0   := !io.req.bits(0).write  // active-low
  dmem.web1   := !io.req.bits(1).write  // active-low
  dmem.wmask0 := io.req.bits(0).wmask
  dmem.wmask1 := io.req.bits(1).wmask
  dmem.addr0  := io.req.bits(0).addr
  dmem.addr1  := io.req.bits(1).addr
  dmem.din0   := io.req.bits(0).data
  dmem.din1   := io.req.bits(1).data

  io.resp(0).data := dmem.dout0
  io.resp(1).data := dmem.dout1
}
