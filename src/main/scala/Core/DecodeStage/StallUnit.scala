package DecodeStage

import chisel3._

class StallUnitIO(PARAMS:Map[String, Int]) extends Bundle {
        // Input pins
        val instIn: UInt      = Input(UInt(PARAMS("XLEN").W))
        val loadEn   : Bool      = Input(Bool())
        val pcIn     : Vec[UInt] = Input(Vec(2, UInt(PARAMS("XLEN").W)))
        val regAddr  : Vec[UInt] = Input(Vec(3, UInt(PARAMS("REGADDRLEN").W)))

        // Output pins
        val stallEn: Bool      = Output(Bool())
        val instOut: UInt      = Output(UInt(PARAMS("XLEN").W))
        val pcOut  : Vec[UInt] = Output(Vec(2, UInt(PARAMS("XLEN").W)))
}

class StallUnit(PARAMS:Map[String, Int]) extends Module {
        // Initializing IO pins
        val io         : StallUnitIO = IO(new StallUnitIO(PARAMS))
        val loadEn     : Bool        = dontTouch(WireInit(io.loadEn))
        val regDErdAddr: UInt        = dontTouch(WireInit(io.regAddr(0)))
        val rsAddr     : Vec[UInt]   = dontTouch(VecInit(io.regAddr.slice(1, 3)))
        //val pcIn       : UInt        = dontTouch(WireInit(io.pcIn(0)))
        //val stallPCin  : UInt        = dontTouch(WireInit(io.pcIn(1)))

        // Intermediate wires
        //val rs1_Hazard: Bool = dontTouch(WireInit(regDErdAddr === rsAddr(0)))
        //val rs2_Hazard: Bool = dontTouch(WireInit(regDErdAddr === rsAddr(1)))
        val loadHazardEn: Bool = dontTouch(WireInit(loadEn && (
                (regDErdAddr === rsAddr(0)) || (regDErdAddr === rsAddr(1))
        )))

        // Wiring to output pins
        io.stallEn := Mux(loadHazardEn, 1.B, 0.B)

        io.instOut := io.instIn

        for (i <- 0 until io.pcIn.length) {
                io.pcOut(i) := io.pcIn(i)
        }
}
