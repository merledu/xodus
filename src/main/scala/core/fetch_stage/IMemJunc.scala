package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.memory.MemoryIO


class IMemJuncIO extends Bundle with Configs {
  val addr: UInt = Flipped(new PCIO().addr)

  val inst: UInt = Output(UInt(XLEN.W))

  val iMemReqResp: MemoryIO = Flipped(new MemoryIO)
}


class IMemJunc extends RawModule with Configs {
  val io: IMemJuncIO = IO(new IMemJuncIO)


  /********************
   * Interconnections *
   ********************/

  io.iMemReqResp.req.addr.bits  := io.addr
  io.iMemReqResp.req.addr.valid := 1.B
  io.iMemReqResp.req.data.valid := 0.B
  io.iMemReqResp.req.data.bits  := 0.U
  io.inst                       := io.iMemReqResp.resp.data
}
