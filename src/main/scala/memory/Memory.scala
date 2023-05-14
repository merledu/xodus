package xodus.memory

import chisel3._,
       chisel3.util.experimental.loadMemoryFromFile,
       scala.math.pow
import xodus.configs.Configs,
       xodus.io.MemoryIO


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


  /********************
   * Interconnections *
   ********************/

  // Write to memory
  when (io.req.data.valid) {
    mem.write(io.req.addr.bits, io.req.data.bits)
  }

  // Read from memory
  //io.rsp.data := Mux(io.req.addr.valid, mem.read(io.req.addr.bits), 0.U)
  io.rsp.data := mem.read(io.req.addr.bits)
}
