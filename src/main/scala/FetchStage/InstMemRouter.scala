package FetchStage

import chisel3._
import chisel3.util._

class InstMemRouterIO extends Bundle {
        // Input ports
        val addrIn      = Input(UInt(16.W))
        val jumpStallEn = Input(Bool())
        val stallEn     = Input(Bool())
        val memInstIn   = Input(UInt(32.W))
        val stallInst   = Input(UInt(32.W))

        // Output ports
        val addrOut = Output(UInt(16.W))
        val instOut = Output(UInt(32.W))
}

class InstMemRouter extends Module {
        // Initializing IO ports
        val io = IO(new InstMemRouterIO)

        // Wiring ports
        Seq(
                (io.addrOut, io.addrIn),
                (io.instOut, MuxCase(io.memInstIn, Seq(
                        io.jumpStallEn -> 0.U,
                        io.stallEn     -> io.stallInst
                )))
        ) map (x => x._1 := dontTouch(x._2))
}
