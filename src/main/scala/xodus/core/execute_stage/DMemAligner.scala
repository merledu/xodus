//package core.execute_stage
//
//import chisel3._,
//       chisel3.util._
//import configs.Configs,
//       core.decode_stage.{DMemAlignerCtrl, DMemCtrl},
//       core.pipeline_regs.RegDEIO
//
//
//class DMemAlignerIO extends Bundle with Configs {
//  val alu     : SInt            = Flipped(new ALUIO().out)
//  val ctrl    : DMemAlignerCtrl = Flipped(new RegDEIO().dMemAlignerCtrl)
//  val dMemCtrl: DMemCtrl        = Flipped(new RegDEIO().dMemCtrl)
//  val store   : SInt            = Input(SInt(XLEN.W))
//
//  val wmask       : UInt      = Output(UInt(WMaskWidth.W))
//  val addr        : UInt      = Output(UInt(AddrWidth.W))
//  val alignedStore: UInt      = Output(UInt(XLEN.W))
//  val offset      : UInt      = Output(UInt(OffsetWidth.W))
//  val align       : Vec[Bool] = Output(Vec(3, Bool()))
//}
//
//
//class DMemAligner extends RawModule with Configs {
//  val io: DMemAlignerIO = IO(new DMemAlignerIO)
//
//  val offset: UInt = io.alu(1, 0)
//  val addr  : UInt = io.alu(AddrWidth + 1, 2)
//
//  val byte    : UInt = io.store(ByteWidth - 1, 0)
//  val halfWord: UInt = io.store(HalfWordWidth - 1, 0)
//
//
//  /********************
//   * Interconnections *
//   ********************/
//
//  io.addr   := Mux(io.ctrl.align, addr + 1.U, addr)
//  io.offset := offset
//  io.align  := VecInit(Seq(io.ctrl.align) ++ (1 to 2).toSeq.map(io.ctrl.store(_)))
//
//  when (io.ctrl.store(1)) {  // sh
//    io.wmask := Mux(io.ctrl.align, "b0001".U, MuxLookup(offset, "b0011".U)(Seq(
//      "0110", "1100", "1000"
//    ).zipWithIndex.map(
//      x => (x._2 + 1).U -> ("b" + x._1).U
//    )))
//
//    io.alignedStore := Mux(io.ctrl.align, io.store(15, 8), MuxLookup(offset, halfWord)((1 to 3).toSeq.map(
//      x => x.U -> Cat(halfWord, 0.U((ByteWidth * x).W))
//    )))
//  }.elsewhen (io.ctrl.store(2)) {  // sw
//    io.wmask := Mux(io.ctrl.align, MuxLookup(offset, "b0001".U)(Seq(
//      "0011", "0111"
//    ).zipWithIndex.map(
//      x => (x._2 + 2).U -> ("b" + x._1).U
//    )), MuxLookup(offset, "b1111".U)(Seq(
//      "1110", "1100", "1000"
//    ).zipWithIndex.map(
//      x => (x._2 + 1).U -> ("b" + x._1).U
//    )))
//
//    io.alignedStore := Mux(io.ctrl.align, MuxLookup(offset, io.store(31, 24))(Seq(
//      HalfWordWidth, ByteWidth
//    ).zipWithIndex.map(
//      x => (x._2 + 2).U -> io.store(XLEN - 1, x._1)
//    )), MuxLookup(offset, io.store.asUInt)((1 to 3).toSeq.map(
//      x => x.U -> Cat(io.store, 0.U((ByteWidth * x).W))
//    )))
//  } otherwise {  // sb
//    io.wmask := MuxLookup(offset, "b0001".U)(Seq(
//      "0010", "0100", "1000"
//    ).zipWithIndex.map(
//      x => x._2.U -> ("b" + x._1).U
//    ))
//
//    io.alignedStore := MuxLookup(offset, byte)((1 to 3).toSeq.map(
//      x => x.U -> Cat(byte, 0.U((ByteWidth * x).W))
//    ))
//  }
//}
