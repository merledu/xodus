package PipelineRegs

import chisel3._

class RegFDIO(PARAMS:Map[String, Int]) extends Bundle {
    // Input ports
    val in: Vec[UInt] = Input(Vec(3, UInt(PARAMS("XLEN").W)))
    
    // Output ports
    val out: Vec[UInt] = Output(Vec(3, UInt(PARAMS("XLEN").W)))
}

class RegFD(PARAMS:Map[String, Int]) extends Module {
    // Initializing IO ports
    val io: RegFDIO = IO(new RegFDIO(PARAMS))

    // Registers
    val regs: Vec[UInt] = RegInit(Vec(3, 0.U(PARAMS("XLEN").W)))

    // Connections
    for (i <- 0 until regs.length) {
        regs(i)   := io.in(i)
        io.out(i) := regs(i)
    }



    // Debug Section
    val debug_inst: UInt = dontTouch(WireInit(regs(0)))
    val debug_pc  : UInt = dontTouch(WireInit(regs(1)))
    val debug_npc : UInt = dontTouch(WireInit(regs(2)))
}
