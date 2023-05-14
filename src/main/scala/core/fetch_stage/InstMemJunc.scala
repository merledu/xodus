package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.io.InstMemJuncIO


class InstMemJunc extends RawModule with Configs {
  val io: InstMemJuncIO = IO(new InstMemJuncIO)


  /********************
   * Interconnections *
   ********************/

  io.iMemReqRsp.req.addr.bits  := io.addr
  io.iMemReqRsp.req.addr.valid := 1.B
  io.iMemReqRsp.req.data.valid := 0.B
  io.iMemReqRsp.req.data.bits  := 0.B
  io.inst                      := io.iMemReqRsp.rsp.data
}
