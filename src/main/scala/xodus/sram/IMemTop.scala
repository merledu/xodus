package xodus.sram

import chisel3._,
       chisel3.util._


class IMemTopIO extends Bundle {
  val req: Valid[SRAMReqIO] = Flipped(Valid(new SRAMReqIO))

  val resp: SRAMRespIO = new SRAMRespIO
}


class IMemTop(imem_file: Option[String]) extends Module {
  val io: IMemTopIO = IO(new IMemTopIO)

  // Memory
  val imem: IMemIO = Module(new IMem(imem_file)).io


  /********************
   * Interconnections *
   ********************/

  imem.clk0   := clock.asBool
  imem.csb0   := !io.req.valid       // active-low
  imem.web0   := !io.req.bits.write  // active-low
  imem.addr0  := io.req.bits.addr
  imem.wmask0 := io.req.bits.wmask
  imem.din0   := io.req.bits.data

  io.resp.data := imem.dout0
}
