//package MemoryStage
//
//import chisel3._
//import chisel3.util._
//
//
//class DataMemRouterIO(
//  params :Map[String, Int],
//  enNum  :Int
//) extends Bundle {
//  // Input pins
//  val dataVec: SInt = Input(Vec(3, SInt(params("XLEN"))))
//  val en     : Bool = Input(Vec(enNum, Bool()))
//
//  // Output pins
//  val memAddr: UInt = Output(UInt(params("depth").W))
//  val dataOut: SInt = Output(SInt(params("XLEN").W))
//  val storeEn: Bool = Output(Bool())
//  val loadEn : Bool = Output(Bool())
//  val out    : SInt = Output(SInt(PARAMS("XLEN").W))
//}
//
//
//class DataMemRouter(
//  params      :Map[String, Int],
//  confDataMem :Seq[String],
//  debug       :Boolean
//) extends Module {
//  val enNum: Int = confDataMem.length
//  val io: DataMemRouterIO = IO(new DataMemRouterIO(
//    params = params,
//    enNum  = enNum
//  ))
//
//  val alu_in  : SInt      = dontTouch(WireInit(io.alu_in))
//  val str_en  : Bool      = dontTouch(WireInit(io.str_en))
//  val load_en : Bool      = dontTouch(WireInit(io.load_en))
//  val sb_en   : Bool      = dontTouch(WireInit(io.sb_en))
//  val sh_en   : Bool      = dontTouch(WireInit(io.sh_en))
//  val sw_en   : Bool      = dontTouch(WireInit(io.sw_en))
//  val lb_en   : Bool      = dontTouch(WireInit(io.lb_en))
//  val lh_en   : Bool      = dontTouch(WireInit(io.lh_en))
//  val lw_en   : Bool      = dontTouch(WireInit(io.lw_en))
//  val lbu_en  : Bool      = dontTouch(WireInit(io.lbu_en))
//  val lhu_en  : Bool      = dontTouch(WireInit(io.lhu_en))
//
//  // Wires
//  val dataWires: Map[String, SInt] = Map(
//    "rs2"  -> io.dataVec(1),
//    "load" -> io.dataVec(2)
//  )
//
//  val enWires: Map[String, Bool] = (
//    for (i <- 0 until enNum)
//      yield confDataMem(i) -> io.enVec(i)
//  ).toMap
//
//  val uintWires: Ma
//
//  // Intermediate wires
//  val address : UInt = dontTouch(WireInit(alu_in.asUInt))
//
//  // Store wires
//  val sb      : SInt = dontTouch(WireInit(rs2_data(7, 0).asSInt))
//  val sh      : SInt = dontTouch(WireInit(rs2_data(15, 0).asSInt))
//  val sw      : SInt = dontTouch(WireInit(rs2_data))
//
//  // Storing to data memory
//  io.dataOut := MuxCase(0.S, Seq(
//          sb_en -> sb,
//          sh_en -> sh,
//          sw_en -> sw
//  ))
//  
//  // Load wires
//  val lb : SInt = dontTouch(WireInit(dataIn(7, 0).asSInt))
//  val lh : SInt = dontTouch(WireInit(dataIn(15, 0).asSInt))
//  val lw : SInt = dontTouch(WireInit(dataIn))
//  val lbu: UInt = dontTouch(WireInit(dataIn(7, 0)))
//  val lhu: UInt = dontTouch(WireInit(dataIn(15, 0)))
//
//  // Loading data from data memory
//  io.out := MuxCase(0.S, Seq(
//          lb_en  -> lb,
//          lh_en  -> lh,
//          lw_en  -> lw,
//          lbu_en -> lbu.asSInt,
//          lhu_en -> lhu.asSInt
//  ))
//
//  Seq(
//          io.addrOut, io.loadEn, io.storeEn
//  ) zip Seq(
//          address, load_en, str_en
//  ) foreach {
//          x => x._1 := x._2
//  }
//}
