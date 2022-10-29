package DecodeStage

import chisel3._

class RegFileIO(PARAMS:Map[String, Int]) extends Bundle {
    // Input ports
    val rAddr : UInt = Input(UInt(PARAMS("REGADDRLEN").W))
    val rdData: SInt = Input(SInt(PARAMS("XLEN").W))
    val wrEn  : Bool = Input(Bool())

    // Output ports
    val rsData: SInt = Output(SInt(PARAMS("XLEN").W))
}

class RegFile(PARAMS:Map[String, Int], DEBUG:Boolean=False) extends Module {
    // Initializing IO ports
    val io: RegFileIO = IO(new RegFileIO(PARAMS))

    // Register File
    val regFile: Vec[SInt] = Reg(Vec(32, SInt(32.W)))
    
    // Data is written when wrEn is high
    when (io.wrEn && (io.rAddr(0) =/= 0.U)) {
        regFile(io.rAddr(0)) := io.rdData
    }

    // Connections
    Seq(
        (io.rsData(0), rAddr(1), regFile(io.rAddr(1))),
        (io.rsData(1), rAddr(2), regFile(io.rAddr(2)))
    ).map(x => x._1 := Mux(x._2 === 0.U, 0.S, x._3))


    // Debug Section
    if (DEBUG) {
        val rdAddr : UInt = dontTouch(WireInit(io.rAddr(0)))
        val rdData : SInt = dontTouch(WireInit(io.rdData))
        val rs1Addr: UInt = dontTouch(WireInit(io.rAddr(1)))
        val rs2Addr: UInt = dontTouch(WireInit(io.rAddr(2)))
        val wrEn   : Bool = dontTouch(WireInit(io.wrEn))

        val rs1Data: SInt = dontTouch(WireInit(regFile(rs1_addr)))
        val rs2Data: SInt = dontTouch(WireInit(regFile(rs2_addr)))
    } else None
}

