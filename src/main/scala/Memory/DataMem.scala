package Memory

import chisel3._
import chisel3.util._
import scala.math.pow

class DataMemIO extends Bundle {
        // Input ports
        val addr    = Input(UInt(16.W))
        val storeEn = Input(Bool())
        val loadEn  = Input(Bool())
        val dataIn  = Input(SInt(32.W))

        // Output ports
        val dataOut = Output(SInt(32.W))
}

class DataMem extends Module {
        // Initializing IO ports
        val io = IO(new DataMemIO)

        // Initializing Memory
        val dataMem = Mem(pow(2, 16).toInt, SInt(32.W))

        // Writing data to Memory
        when (io.storeEn) {
                dataMem.write(io.addr, io.dataIn)
        }

        // Loading data to Memory
        io.dataOut := dontTouch(Mux(io.loadEn, dataMem.read(io.addr), 0.S))
}
