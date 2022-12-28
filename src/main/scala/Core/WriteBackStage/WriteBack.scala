package WriteBackStage

import chisel3._, chisel3.util._


class WriteBackIO(params :Map[String, Int]) extends Bundle {
  // Input pins
  val wbData: Vec[SInt] = Input(Vec(2, SInt(params("XLEN").W)))
  val loadEn: Bool      = Input(Bool())

  // Output pins
  val rdData: SInt = Output(SInt(params("XLEN").W))
}


class WriteBack(
  params :Map[String, Int],
  debug  :Boolean
) extends Module {
  // Initializing IO pins
  val io: WriteBackIO = IO(new WriteBackIO(params))

  // Wires
  val wbWires: Map[String, SInt] = Map(
    "alu" -> io.wbData(0),
    "mem" -> io.wbData(1)
  )

  // Connections
  io.rdData := Mux(
    io.loadEn,
    wbWires("mem"),
    wbWires("alu")
  )
}
