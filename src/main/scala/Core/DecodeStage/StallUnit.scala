//package DecodeStage
//
//import chisel3._
//import chisel3.util._
//
//
//class StallUnitIO(params :Map[String, Int]) extends Bundle {
//  // Input pins
//  val rAddr: Valid[Vec[UInt]] = Flipped(Valid(Vec(3, UInt(params("regAddrLen").W))))
//
//  // Output pins
//  val stallEn: Bool = Output(Bool())
//}
//
//
//class StallUnit(
//  params :Map[String, Int],
//  debug  :Boolean
//) extends Module {
//  val io: StallUnitIO = IO(new StallUnitIO(params))
//
//  // Wires
//  val uintWires: Map[String, UInt] = Map(
//    "rdAddr"  -> io.rAddr.bits(0),
//    "rs1Addr" -> io.rAddr.bits(1),
//    "rs2Addr" -> io.rAddr.bits(2),
//  )
//
//  val enWires: Map[String, Bool] = Map(
//    "load" -> io.rAddr.valid
//  )
//
//  // Connections
//  io.stallEn := enWires("load") && ((uintWires("rdAddr") === uintWires("rs1Addr")) || (uintWires("rdAddr") === uintWires("rs2Addr")))
//
//
//
//  // Debug Section
//  if (debug) {
//    val debug_rdAddr : UInt = dontTouch(WireInit(uintWires("rdAddr")))
//    val debug_rs1Addr: UInt = dontTouch(WireInit(uintWires("rs1Addr")))
//    val debug_rs2Addr: UInt = dontTouch(WireInit(uintWires("rs2Addr")))
//    val debug_loadEn : Bool = dontTouch(WireInit(enWires("load")))
//  } else None
//}
