package core.memory_stage

import chisel3._,
       chisel3.util._
import configs.Configs,
       memory.{MemReqIO, MemoryIO},
       core.decode_stage.DMemEN,
       core.pipeline_regs.RegEMIO


class DMemAlignerIO extends Bundle with Configs {
  val en       : DMemEN      = Flipped(new RegEMIO().dMemEN)
  val addr     : SInt        = Flipped(new RegEMIO().alu)
  val storeData: SInt        = Flipped(new RegEMIO().storeData)

  val load: Valid[SInt] = Valid(SInt(XLEN.W))

  val dMemReqResp: MemoryIO = Flipped(new MemoryIO)
}


class DMemAligner extends RawModule with Configs {
  val io: DMemAlignerIO = IO(new DMemAlignerIO)

  val loadData: Vec[SInt] = VecInit(Seq(
    7, 15
  ).map(
    x => io.dMemReqResp.resp.data(x, 0).asSInt
  ) ++ Seq(io.dMemReqResp.resp.data.asSInt) ++ Seq(
    7, 15
  ).map(
    x => Cat(0.U((XLEN - (x + 1)).W), io.dMemReqResp.resp.data(x, 0)).asSInt
  ))

  val en: Vec[Bool] = VecInit(Seq(
    io.en.load,
    io.en.store
  ).map(
    x => x.reduce(
      (x, y) => x || y
    )
  ))


  /********************
   * Interconnections *
   ********************/

  // Write to Data Memory
  io.dMemReqResp.req.addr.bits  := io.addr.asUInt
  io.dMemReqResp.req.addr.valid := en(0)
  io.dMemReqResp.req.data.bits  := MuxCase(io.storeData.asUInt, Seq(
    7, 15
  ).zipWithIndex.map(
    x => io.en.store(x._2) -> io.storeData(x._1, 0).asUInt
  ))
  io.dMemReqResp.req.data.valid := en(1)

  // Read from Data Memory
  io.load.valid := en(0)
  io.load.bits  := MuxCase(0.S, (0 until loadData.length).toSeq.map(
    x => io.en.load(x) -> loadData(x)
  ))
}
