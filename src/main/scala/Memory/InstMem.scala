package Memory

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile
import scala.math.pow

class InstMemIO extends Bundle {
        // Input ports
        val addr: UInt = Input(UInt(16.W))

        // Output ports
        val inst: UInt = Output(UInt(32.W))
}

class InstMem extends Module {
        // Initializing IO ports
        val io = IO(new InstMemIO)

        // Initializing Memory
        val instMem = Mem(pow(2, 16).toInt, UInt(32.W))

        // Loading instructions from file
        loadMemoryFromFile(instMem, "assembly/assembly.hex")

        // Wiring ports
        io.inst := dontTouch(instMem.read(io.addr))
}
