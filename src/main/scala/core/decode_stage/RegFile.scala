package decode_stage

import chisel3._, chisel3.util._


class RegFileIO extends Bundle with Configs {
  // Input ports
  val rAddr : Vec[UInt]   = Input(Vec(3, UInt(regAddrLen.W)))
  val rdData: Valid[SInt] = Flipped(Valid(SInt(xlen.W)))

  // Output ports
  val rsData: Vec[SInt] = Output(Vec(2, SInt(xlen.W)))
}


class RegFile extends Module with Configs {
  val io: RegFileIO = IO(new RegFileIO)

  // Wires
  val addrWires: Map[String, UInt] = Map(
    "rdAddr"  -> io.rAddr(0),
    "rs1Addr" -> io.rAddr(1),
    "rs2Addr" -> io.rAddr(2)
  )

  // Register File
  val regFile: Vec[SInt] = Reg(Vec(xlen, SInt(xlen.W)))
  
  // Data is written when valid bit is high
  when (io.rdData.valid && addrWires("rdAddr").orR) {
    regFile(addrWires("rdAddr") - 1.U) := io.rdData.bits
  }

  // Connections
  for (i <- 0 until io.rsData.length) {
    io.rsData(i) := Mux(
      addrWires(s"rs${i + 1}Addr").orR,
      regFile(addrWires(s"rs${i + 1}Addr")),
      0.S
    )
  }



  // Debug Section
  if (debug) {
    val debug_addrWires_rdAddr : UInt = dontTouch(WireInit(io.rAddr(0)))
    val debug_rdData           : SInt = dontTouch(WireInit(io.rdData.bits))
    val debug_addrWires_rs1Addr: UInt = dontTouch(WireInit(io.rAddr(1)))
    val debug_addrWires_rs2Addr: UInt = dontTouch(WireInit(io.rAddr(2)))
    val debug_wrEn             : Bool = dontTouch(WireInit(io.rdData.valid))
  } else None
}
