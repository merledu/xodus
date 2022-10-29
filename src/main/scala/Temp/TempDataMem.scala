package Temp

import chisel3._
import scala.math.pow

class DataMemIO extends Bundle {
        val addr   : UInt = Input(UInt(32.W))
        val dataIn : SInt = Input(SInt(32.W))
        val storeEn: Bool = Input(Bool())
        val loadEn : Bool = Input(Bool())

        val out: SInt = Output(SInt(32.W))
}

class DataMem() extends Module {
        val io     : DataMemIO = IO(new DataMemIO)
        val addr   : UInt      = dontTouch(WireInit(io.addr))
        val dataIn : SInt      = dontTouch(WireInit(io.dataIn))
        val storeEn: Bool      = dontTouch(WireInit(io.storeEn))
        val loadEn : Bool      = dontTouch(WireInit(io.loadEn))

        val mem = Mem(pow(2, 16).toInt, SInt(32.W))

        when (io.storeEn) {
                mem.write(io.addr, io.dataIn)
        }

        io.out := Mux(loadEn, mem.read(io.addr), 0.S)
}
