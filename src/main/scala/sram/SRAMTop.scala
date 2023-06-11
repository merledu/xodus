package sram

import chisel3._,
       chisel3.util._
import configs.Configs


class SRAMReqIO extends Bundle with Configs {
  val addr : UInt = new SRAMIO().addr0
  val data : UInt = new SRAMIO().din0
  val wmask: UInt = new SRAMIO().wmask0
  val write: Bool = new SRAMIO().web0
}


class SRAMRespIO extends Bundle with Configs {
  val data: UInt = new SRAMIO().dout0
}


class SRAMTopIO extends Bundle {
  val req: Valid[SRAMReqIO] = Flipped(Valid(new SRAMReqIO))

  val resp: SRAMRespIO = new SRAMRespIO
}


class SRAMTop(HexFile:Option[String]) extends Module with Configs {
  val io: SRAMTopIO = IO(new SRAMTopIO)

  // Memory
  val sram: SRAMIO = Module(new SRAM(HexFile)).io


  /********************
   * Interconnections *
   ********************/

  sram.clk0   := clock.asBool
  sram.csb0   := !io.req.valid       // active-low
  sram.web0   := !io.req.bits.write  // active-low
  sram.addr0  := io.req.bits.addr
  sram.wmask0 := io.req.bits.wmask
  sram.din0   := io.req.bits.data

  io.resp.data := sram.dout0
}
