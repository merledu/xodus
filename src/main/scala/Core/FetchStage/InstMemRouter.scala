package FetchStage

import chisel3._
import chisel3.util._

class InstMemRouterIO(PARAMS:Map[String, Int]) extends Bundle {
    // Input ports
    val addrIn : UInt      = Input(UInt(PARAMS("MDEPTH").W))
    val stallEn: Vec[Bool] = Input(Vec(2, Bool()))
    val in     : Vec[UInt] = Input(Vec(2, UInt(PARAMS("XLEN").W)))

    // Output ports
    val addrOut: UInt = Output(UInt(PARAMS("MDEPTH").W))
    val out    : UInt = Output(UInt(PARAMS("XLEN").W))
}

class InstMemRouter(PARAMS:Map[String, Int]) extends Module {
    // Initializing IO ports
    val io: InstMemRouterIO = IO(new InstMemRouterIO(PARAMS))

    // Assigning wires
    val stallEn    : Bool = dontTouch(WireInit(io.stallEn(0)))
    val jumpStallEn: Bool = dontTouch(WireInit(io.stallEn(1)))
    val inst       : UInt = dontTouch(WireInit(io.instIn(0)))
    val stallInst  : UInt = dontTouch(WireInit(io.instIn(1)))

    // Wiring ports
    Seq(
        (io.addrOut, io.addrIn),
        (io.instOut, MuxCase(inst, Seq(
            jumpStallEn -> 0.U,
            stallEn     -> stallInst
        )))
    ).map(x => x._1 := x._2)
}
