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
  val req: DecoupledIO[SRAMReqIO] = Flipped(Decoupled(new SRAMReqIO))

  val resp: DecoupledIO[SRAMRespIO] = Decoupled(new SRAMRespIO)
}


class SRAMTop(HexFile:Option[String]) extends Module with Configs {
  val io: SRAMTopIO = IO(new SRAMTopIO)

  // Memory
  val sram: SRAMIO = Module(new SRAM(HexFile)).io


  /********************
   * Interconnections *
   ********************/

  // Memory is always ready to accept requests
  // and always has valid output
  Seq(
    io.req.ready,
    io.resp.valid
  ).map(
    x => x := 1.B
  )

  sram.clk0  := clock.asBool
  sram.csb0  := !io.req.valid       // active-low
  sram.web0  := !io.req.bits.write  // active-low
  sram.addr0 := io.req.bits.addr

  // Write to memory
  sram.wmask0 := io.req.bits.wmask
  sram.din0   := io.req.bits.data

  // Read from memory
  io.resp.bits.data := Mux(io.resp.ready, sram.dout0, 0.U)
}
