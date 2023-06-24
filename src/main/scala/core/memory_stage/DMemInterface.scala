package core.memory_stage

import chisel3._,
       chisel3.util._
import configs.Configs,
       core.decode_stage.DMemCtrl,
       core.pipeline_regs.RegEMIO,
       sram.SRAMTopIO


class DMemInterfaceIO extends Bundle with Configs {
  val ctrl : DMemCtrl = Flipped(new RegEMIO().dMemCtrl)
  val addr : UInt     = Flipped(new RegEMIO().dMemAddr)
  val wmask: UInt     = Flipped(new RegEMIO().wmask)
  val store: UInt     = Flipped(new RegEMIO().store)

  val load: Valid[SInt] = Valid(SInt(XLEN.W))

  val dMemInterface: SRAMTopIO = Flipped(new SRAMTopIO)
}


class DMemInterface extends Module with Configs {
  val io: DMemInterfaceIO = IO(new DMemInterfaceIO)


  /********************
   * Interconnections *
   ********************/

  io.dMemInterface.req.valid      := !reset.asBool && (io.ctrl.load || io.ctrl.store)
  io.dMemInterface.req.bits.addr  := io.addr
  io.dMemInterface.req.bits.write := io.ctrl.store
  io.dMemInterface.req.bits.wmask := io.wmask
  io.dMemInterface.req.bits.data  := io.store

  io.load.valid := io.ctrl.load
  io.load.bits  := io.dMemInterface.resp.data.asSInt
}
