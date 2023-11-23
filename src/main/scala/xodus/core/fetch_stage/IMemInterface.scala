package xodus.core.fetch_stage

import chisel3._
import xodus.configs.Configs,
       xodus.sram.IMemTopIO


class IMemInterfaceIO extends Bundle with Configs {
  val pc: UInt = Flipped(new PCIO().pc)

  val inst: UInt = new IMemTopIO().resp.data

  val imem: IMemTopIO = Flipped(new IMemTopIO)
}


class IMemInterface extends Module with Configs {
  val io: IMemInterfaceIO = IO(new IMemInterfaceIO)


   /*** Interconnections ***/

  io.imem.req.valid      := !reset.asBool
  io.imem.req.bits.addr  := io.pc(ADDR_WIDTH + 1, 2)
  io.imem.req.bits.data  := 0.U
  io.imem.req.bits.write := 0.B
  io.imem.req.bits.wmask := 0.U

  io.inst := io.imem.resp.data
}