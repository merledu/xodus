package xodus.memory

import chisel3._,
       chisel3.util.experimental.loadMemoryFromFile,
       scala.math.pow
import xodus.configs.Configs


class MemoryIO extends Bundle {
  val req: MemReqIO  = new MemReqIO
  val rsp: MemRespIO = new MemRespIO
}


// Default: Instruction Memory
class Memory extends Module with Configs {
  val io: MemoryIO = IO(new MemoryIO)

  val memDepth: Int = pow(2, MemDepth).toInt
  
  // Memory
  val mem: Mem[UInt] = Mem(memDepth, UInt(XLEN.W))
  loadMemoryFromFile(mem, "hex/inst.hex")
  //loadMemoryFromFile(
  //  mem,
  //  if (Data) "hex/data.hex"
  //  else "hex/inst.hex"
  //)

  // Wires
  val en: Map[String, Bool] = Seq(
    "load", "store"
  ).view.zipWithIndex.map(
    x => x._1 -> io.req.en(x._2)
  ).toMap


  /********************
   * Interconnections *
   ********************/

  // Write to memory
  when (en("store")) {
    mem.write(io.req.addr, io.req.data)
  }

  // Read from memory
  io.rsp.data := Mux(en("load"), mem.read(io.req.addr), 0.U)



  // Debug
  if (Debug) {
    val debug_req_addr  : UInt = dontTouch(WireInit(io.req.addr))
    val debug_load_en   : Bool = dontTouch(WireInit(io.req.en(0)))
    val debug_store_en  : Bool = dontTouch(WireInit(io.req.en(1)))
    val debug_store_data: UInt = dontTouch(WireInit(io.req.data))
  }
}
