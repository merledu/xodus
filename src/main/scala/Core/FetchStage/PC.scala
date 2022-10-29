package FetchStage

import chisel3._
import chisel3.util._

class PCIO(PARAMS:Map[String, Int]) extends Bundle {
    // Input ports
    val pcIn     : Vec[UInt] = Input(Vec(4, UInt(PARAMS("XLEN").W)))
    val pcControl: Vec[Bool] = Input(Vec(5, Bool()))
    val imm      : SInt      = Input(SInt(PARAMS("XLEN").W))
    
    // Output ports
    val pcOut   : Vec[UInt] = Output(Vec(3, UInt(PARAMS("XLEN").W)))
    val instAddr: UInt      = Output(UInt(PARAMS("MDEPTH").W))
}

class PC(PARAMS:Map[String, Int]) extends Module {
    // Initializing IO ports
    val io         : PCIO = IO(new PCIO(PARAMS))

    // Assigning wires
    val stallPC    : UInt = dontTouch(WireInit(io.pcIn(0)))
    val stallUnitPC: UInt = dontTouch(WireInit(io.pcIn(1)))
    val regFDpc    : UInt = dontTouch(WireInit(io.pcIn(2)))
    val jalrPC     : UInt = dontTouch(WireInit(io.pcIn(3)))
    val stallEn    : Bool = dontTouch(WireInit(io.pcControl(0)))
    val brEn       : Bool = dontTouch(WireInit(io.pcControl(1)))
    val jalEn      : Bool = dontTouch(WireInit(io.pcControl(2)))
    val jalrEn     : Bool = dontTouch(WireInit(io.pcControl(3)))
    val jumpStallEn: Bool = dontTouch(WireInit(io.pcControl(4)))
    
    // Program Counter
    val pc: UInt = dontTouch(RegInit(0.U(PARAMS("XLEN").W)))
    
    // Intermediate wires
    val pcOut: UInt = dontTouch(WireInit(Mux(jumpStallEn, 0.U, pc)))
    val pc4  : UInt = dontTouch(WireInit(pcOut + 4.U))
    val npc  : UInt = dontTouch(WireInit(MuxCase(pc4, Seq(
        stallEn         -> stallUnitPC,
        (brEn || jalEn) -> (regFDpc + io.imm.asUInt),
        jalrEn          -> jalrPC
    ))))

    // Wiring ports
    Seq (
        (io.pcOut(0), Mux(stallEn, stallPC, pcOut)),
        (io.pcOut(1), pc4),
        (io.instAddr, pcOut(31, 2))
    ).map(x => x._1 := x._2)

    Seq(
        pc, io.pcOut(2)
    ).map(x => x := npc)
}
