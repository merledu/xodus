package xodus.core.memory_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DMemCtrl,
       xodus.core.pipeline_regs.RegEMIO,
       xodus.sram.DMemTopIO


class DMemInterfaceIO extends Bundle with Configs {
  val ctrl : DMemCtrl = Flipped(new RegEMIO().dmem_ctrl)
  val addr : UInt     = Flipped(new RegEMIO().alu.asUInt)
  val store_data: SInt = Flipped(new RegEMIO().store_data)

  val load: Valid[SInt] = Valid(SInt(XLEN.W))

  val dmem: DMemTopIO = Flipped(new DMemTopIO)
}


class DMemInterface extends Module with Configs {
  val io: DMemInterfaceIO = IO(new DMemInterfaceIO)

  val addr_offset: UInt = WireInit(io.addr(1, 0))
  val en: Vec[Bool] = VecInit(Seq(
    1 to 5,  // load
    6 to 8   // store
  ).map(
    x => x.map(io.ctrl.en_sel === _.U).reduce( _ || _)
  ))


  /*** Interconnections ***/

  io.dmem.req.valid         := !reset.asBool && en.reduce(_ || _)
  io.dmem.req.bits(0).write := en(1)
  io.dmem.req.bits(1).write := en(1) && (addr_offset > 0.U)
  io.dmem.req.bits(0).wmask := MuxLookup(io.ctrl.en_sel, 0.U)(Seq(
    6 -> "0001",
    7 -> "0011",
    8 -> "1111"
  ).map(
    x => x._1.U -> (("b" + x._2).U(OFFSET_WIDTH.W) << addr_offset)//.asUInt
  ))
  io.dmem.req.bits(0).addr := io.addr
  io.dmem.req.bits(1).addr := io.addr + 1.U

  io.load.valid := en(0)
}
//class DMemInterface extends Module with Configs {
//  val io: DMemInterfaceIO = IO(new DMemInterfaceIO)
//
//
//  /********************
//   * Interconnections *
//   ********************/
//
//  io.dMemInterface.req.valid      := !reset.asBool && (io.ctrl.load || io.ctrl.store)
//  io.dMemInterface.req.bits.addr  := io.addr
//  io.dMemInterface.req.bits.write := io.ctrl.store
//  io.dMemInterface.req.bits.wmask := io.wmask
//  io.dMemInterface.req.bits.data  := io.store
//
//  io.load.valid := io.ctrl.load
//  io.load.bits  := io.dMemInterface.resp.data.asSInt
//}
