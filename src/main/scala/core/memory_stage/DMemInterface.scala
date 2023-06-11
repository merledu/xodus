package core.memory_stage

import chisel3._,
       chisel3.util._
import configs.Configs,
       core.decode_stage.DMemCtrl,
       core.pipeline_regs.RegEMIO,
       sram.SRAMTopIO


class DMemInterfaceIO extends Bundle with Configs {
  val ctrl     : DMemCtrl = Flipped(new RegEMIO().dMemCtrl)
  val alu      : SInt     = Flipped(new RegEMIO().alu)
  val storeData: SInt     = Flipped(new RegEMIO().storeData)

  val load: Valid[SInt] = Valid(SInt(XLEN.W))

  val dMemInterface: SRAMTopIO = Flipped(new SRAMTopIO)
}


class DMemInterface extends Module {
  val io: DMemInterfaceIO = IO(new DMemInterfaceIO)

  val ctrl: Vec[Bool] = VecInit(Seq(
    io.ctrl.load,
    io.ctrl.store
  ).map(
    x => x.reduce(
      (x, y) => x || y
    )
  ))


  /********************
   * Interconnections *
   ********************/

  io.dMemInterface.req.valid := !reset.asBool && ctrl.reduce(
    (x, y) => x || y
  )
  io.dMemInterface.req.bits.addr  := io.alu.asUInt
  io.dMemInterface.req.bits.data  := io.storeData.asUInt
  io.dMemInterface.req.bits.wmask := "b1111".U
  io.dMemInterface.req.bits.write := ctrl(1)

  io.load.valid := ctrl(0)
  io.load.bits  := io.dMemInterface.resp.data.asSInt
}
