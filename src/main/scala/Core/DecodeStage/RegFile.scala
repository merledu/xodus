package DecodeStage

import chisel3._
import chisel3.util._

class RegFileIO(PARAMS:Map[String, Int]) extends Bundle {
    // Input ports
    val rAddr : Vec[UInt]   = Input(Vec(3, UInt(PARAMS("REGADDRLEN").W)))
    val rdData: Valid[SInt] = Flipped(Valid(SInt(PARAMS("XLEN").W)))

    // Output ports
    val rsData: Vec[SInt] = Output(Vec(2, SInt(PARAMS("XLEN").W)))
}

class RegFile(PARAMS:Map[String, Int], DEBUG:Boolean) extends Module {
    // Initializing IO ports
    val io: RegFileIO = IO(new RegFileIO(PARAMS))

    // Register File
    val regFile: Vec[SInt] = RegInit(Vec(32, 0.S(PARAMS("XLEN").W)))
    
    // Data is written when wrEn is high
    when (io.rdData.valid && (io.rAddr(0) =/= 0.U)) {
        regFile(io.rAddr(0)) := io.rdData.bits
    }

    // Connections
    for (i <- 0 until io.rsData.length) {
        io.rsData(i) := Mux(io.rAddr(i + 1) === 0.U, 0.S, regFile(io.rAddr(i + 1)))
    }



    // Debug Section
    if (DEBUG) {
        val debug_rdAddr : UInt = dontTouch(WireInit(io.rAddr(0)))
        val debug_rdData : SInt = dontTouch(WireInit(io.rdData.bits))
        val debug_rs1Addr: UInt = dontTouch(WireInit(io.rAddr(1)))
        val debug_rs2Addr: UInt = dontTouch(WireInit(io.rAddr(2)))
        val debug_wrEn   : Bool = dontTouch(WireInit(io.rdData.valid))

        val debug_rs1Data: SInt = dontTouch(WireInit(regFile(debug_rs1Addr)))
        val debug_rs2Data: SInt = dontTouch(WireInit(regFile(debug_rs2Addr)))
    } else None
}
