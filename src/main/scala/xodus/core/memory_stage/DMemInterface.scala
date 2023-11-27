package xodus.core.memory_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DMemCtrl,
       xodus.sram.DMemTopIO


class DMemInterfaceIO extends Bundle with Configs {
  val ctrl      : DMemCtrl = Flipped(new DMemCtrl)
  val addr      : UInt     = Input(UInt(ADDR_WIDTH.W))
  val store_data: SInt     = Input(SInt(XLEN.W))

  val load: Valid[SInt] = Valid(SInt(XLEN.W))

  val dmem: DMemTopIO = Flipped(new DMemTopIO)
}


class DMemInterface extends Module with Configs {
  val io: DMemInterfaceIO = IO(new DMemInterfaceIO)

  val addr_offset: UInt = io.addr(1, 0)

  val en: Vec[Bool] = VecInit(Seq(
    1 to 5,  // load
    6 to 8   // store
  ).map(
    x => x.map(io.ctrl.op_sel === _.U).reduce( _ || _)
  ))

  val wmask: UInt = WireDefault(UInt((WMASK_WIDTH * 2).W), MuxLookup(io.ctrl.op_sel, 0.U)(Seq(
    6 -> "00000001",  // sb
    7 -> "00000011",  // sh
    8 -> "00001111"   // sw
  ).map(
    x => x._1.U -> (("b" + x._2).U << addr_offset).asUInt
  )))

  val store_data: UInt = (io.store_data << (addr_offset * 8.U)).asUInt
  val load_data : UInt = (Cat(io.dmem.resp(1).data, io.dmem.resp(0).data) >> (addr_offset * 8.U)).asUInt


  /*** Interconnections ***/

  io.dmem.req.valid         := !reset.asBool && en.reduce(_ || _)
  io.dmem.req.bits(0).write := en(1)
  io.dmem.req.bits(1).write := en(1) && wmask(7, 4).orR
  io.dmem.req.bits(0).addr  := io.addr
  io.dmem.req.bits(1).addr  := io.addr + 1.U
  Seq(
    Seq(0, 0),
    Seq(4, 32)
  ).zipWithIndex.foreach(
    x => {
      io.dmem.req.bits(x._2).wmask := wmask(x._1.head + 3, x._1.head)
      io.dmem.req.bits(x._2).data  := store_data(x._1(1) + 31, x._1(1))
    }
  )

  io.load.valid := en(0)
  io.load.bits  := MuxLookup(io.ctrl.op_sel, 0.S)(Seq(
    BYTE_WIDTH,       // lb
    HALF_WORD_WIDTH,  // lh
    XLEN              // lw
  ).zipWithIndex.map(
    x => (x._2 + 1).U -> load_data(x._1, 0).asSInt
  ) ++ Seq(
    BYTE_WIDTH,      // lbu
    HALF_WORD_WIDTH  // lhu
  ).zipWithIndex.map(
    x => (x._2 + 4).U -> Cat(Fill(XLEN - x._1 - 2, 0.U(1.W)), load_data(x._1 - 1, 0)).asSInt
  ))
}