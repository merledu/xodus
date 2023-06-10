package core.fetch_stage

import chisel3._
import configs.Configs,
       sram.{SRAMTopIO, SRAMRespIO}


class IMemInterfaceIO extends Bundle with Configs {
  val pc: UInt = Flipped(new PCIO().pc)

  val inst: UInt = new SRAMRespIO().data

  val iMemInterface: SRAMTopIO = Flipped(new SRAMTopIO)
}


class IMemInterface extends Module with Configs {
  val io: IMemInterfaceIO = IO(new IMemInterfaceIO)


  /********************
   * Interconnections *
   ********************/

  io.iMemInterface.req.valid      := Mux(reset.asBool, 0.B, 1.B)  // Always ready to send requests
  io.iMemInterface.req.bits.addr  := Mux(io.iMemInterface.req.ready, io.pc(ADDR_WIDTH + 1, 2), 0.U)
  io.iMemInterface.req.bits.data  := 0.U
  io.iMemInterface.req.bits.write := 0.B
  io.iMemInterface.req.bits.wmask := "b1111".U
  io.iMemInterface.resp.ready     := 1.B  // Always ready to receive responds

  io.inst := Mux(io.iMemInterface.resp.valid, io.iMemInterface.resp.bits.data, 0.U)
}
