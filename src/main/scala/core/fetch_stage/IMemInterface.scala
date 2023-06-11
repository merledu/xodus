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

  io.iMemInterface.req.valid      := !reset.asBool
  io.iMemInterface.req.bits.addr  := io.pc(ADDR_WIDTH + 1, 2)
  io.iMemInterface.req.bits.data  := 0.U
  io.iMemInterface.req.bits.write := 0.B
  io.iMemInterface.req.bits.wmask := 0.U

  io.inst := io.iMemInterface.resp.data
}
