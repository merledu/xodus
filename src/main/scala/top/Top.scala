package xodus.top

import chisel3._
import xodus.core.{Core, CoreIO},
       xodus.memory.{Memory, MemoryIO},
       xodus.configs.Configs


class Top extends Module with Configs {
  // Modules
  val core: CoreIO   = Module(new Core).io
  val iMem: MemoryIO = Module(new Memory).io


  /********************
   * Interconnections *
   ********************/

  Seq(
    core.iMem.req -> iMem.req,
    iMem.rsp      -> core.iMem.rsp
  ).map(
    x => x._2 <> x._1
  )



  // Debug
  if (Debug) {
    val debug_core_iMem_req_addr    : UInt = dontTouch(WireInit(core.iMem.req.addr))
    val debug_core_iMem_req_load_en : Bool = dontTouch(WireInit(core.iMem.req.en(0)))
    val debug_core_iMem_req_store_en: Bool = dontTouch(WireInit(core.iMem.req.en(1)))
    val debug_core_iMem_req_data    : UInt = dontTouch(WireInit(core.iMem.req.data))
    
    val debug_iMem_rsp_data: UInt = dontTouch(WireInit(iMem.rsp.data))
  }
}
