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


class DMemInterface extends Module with Configs {
  val io: DMemInterfaceIO = IO(new DMemInterfaceIO)

  val ctrl: Vec[Bool] = VecInit(Seq(
    io.ctrl.load,
    io.ctrl.store
  ).map(
    x => x.asUInt.orR
  ))

  val offset   : UInt = WireInit(io.alu(1, 0))
  val storeByte: UInt = WireInit(io.storeData(ByteWidth - 1, 0))


  /********************
   * Interconnections *
   ********************/

  io.dMemInterface.req.valid := !reset.asBool && ctrl.reduce(
    (x, y) => x || y
  )
  io.dMemInterface.req.bits.addr  := io.alu(ADDR_WIDTH + 1, 2)
  io.dMemInterface.req.bits.write := ctrl(1)

  io.dMemInterface.req.bits.data := MuxCase(io.storeData.asUInt, Seq(
    // sb
    io.ctrl.store(0) -> MuxLookup(offset, storeByte)((1 to 3).toSeq.map(
      x => x.U -> Cat(storeByte, 0.U((ByteWidth * x).W))
    ))
  ))
  io.dMemInterface.req.bits.wmask := MuxLookup(offset, 1.U)(Seq(
    "0010", "0100", "1000"
  ).zipWithIndex.map(
    x => (x._2 + 1).U -> ("b" + x._1).U
  ))

  io.load.valid := ctrl(0)
  io.load.bits  := io.dMemInterface.resp.data.asSInt
}
