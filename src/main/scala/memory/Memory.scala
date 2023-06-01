package xodus.memory

import chisel3._,
       chisel3.util._,
       chisel3.util.experimental.loadMemoryFromFile,
       scala.math.pow
import xodus.configs.Configs


class MemReqIO extends Bundle with Configs {
  val addr: Valid[UInt] = Flipped(Valid(UInt(MemDepth.W)))
  val data: Valid[UInt] = Flipped(Valid(UInt(XLEN.W)))
}


class MemRespIO extends Bundle with Configs {
  val data: UInt = Output(UInt(XLEN.W))
}


class MemoryIO extends Bundle {
  val req: MemReqIO = new MemReqIO

  val resp: MemRespIO = new MemRespIO
}


// Default: Instruction Memory
class Memory(Data :Boolean=false) extends Module with Configs {
  val io: MemoryIO = IO(new MemoryIO)

  val memDepth: Int = pow(2, MemDepth).toInt
  
  // Memory
  val mem: Mem[UInt] = Mem(memDepth, UInt(XLEN.W))
  loadMemoryFromFile(
    mem,
    if (Data) "hex/data.hex"
    else "hex/inst.hex"
  )


  /********************
   * Interconnections *
   ********************/

  // Write to memory
  when (io.req.data.valid) {
    mem.write(io.req.addr.bits, io.req.data.bits)
  }

  // Read from memory
  io.resp.data := Mux(io.req.addr.valid, mem.read(io.req.addr.bits), 0.U)
}
