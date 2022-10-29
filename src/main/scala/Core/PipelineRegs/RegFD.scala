package PipelineRegs

import chisel3._

class RegFDIO(PARAMS:Map[String, Int]) extends Bundle {
        // Input ports
        val pcIn  : Vec[UInt] = Input(Vec(2, UInt(PARAMS("XLEN").W)))
        val instIn: UInt      = Input(UInt(PARAMS("XLEN").W))
        
        // Output ports
        val pcOut  : Vec[UInt] = Output(Vec(2, UInt(PARAMS("XLEN").W)))
        val instOut: UInt      = Output(UInt(PARAMS("XLEN").W))
}

class RegFD(PARAMS:Map[String, Int]) extends Module {
        // Initializing IO ports
        val io           : RegFDIO = IO(new RegFDIO(PARAMS))
        val pcIn         : UInt    = dontTouch(WireInit(io.pcIn))
        val instIn       : UInt    = dontTouch(WireInit(io.instIn))
        
        // Initializing registers
        val pc  : UInt = dontTouch(RegInit(0.U(PARAMS("XLEN").W)))
        val inst: UInt = dontTouch(RegInit(0.U(PARAMS("XLEN").W)))
        val pc4 : UInt = dontTouch(RegInit(0.U(PARAMS("XLEN").W)))
        
        // Wiring ports
        io.instOut := inst

        val pcOut: Vec[UInt] = dontTouch(VecInit(pc, pc4))
        for (i <- 0 until pcOut.length) {
                io.pcOut(i) := pcOut(i)
        }

        Seq(
                // Output ports
                io.instOut,

                // Registers
                pc, inst, pc4
        ) zip Seq(
                // Output ports
                inst,

                // Registers
                io.pcIn(0), instIn, io.pcIn(1)
        ) foreach {
                x => x._1 := x._2
        }
}
