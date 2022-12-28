package MemoryStage
import chisel3._, chisel3.util._

import Memory.{MemReqIO, MemRespIO}


class DataMemRouterIO(
  params   :Map[String, Int],
  paramMap :Map[String, Map[String, Int]],
  enNum    :Int
) extends Bundle {
  // Input pins
  val memVec: SInt = Input(Vec(2, SInt(params("XLEN"))))
  val en    : Bool = Input(Vec(enNum, Bool()))

  // Output pins
  val out: SInt = Output(SInt(PARAMS("XLEN").W))

  // Data Memory Interface
  val req: MemReqIO  = Decoupled(new MemReqIO(
    params  = paramMap,
    dataMem = true
  ))
  val resp: MemRespIO = Flipped(Decoupled(new MemRespIO(params)))
}


class DataMemRouter(
  params      :Map[String, Int],
  paramMap    :Map[String, Map[String, Int]],
  confDataMem :Seq[String],
  debug       :Boolean
) extends Module {
  val enNum: Int = confDataMem.length
  val io: DataMemRouterIO = IO(new DataMemRouterIO(
    params   = params,
    paramMap = paramMap,
    enNum    = enNum
  ))

  // Wires
  val uintWires: Map[String, UInt] = Map(
    "addr" -> io.dataVec(0).asUInt,
    "sb"   -> io.data
  )

  val enWires: Map[String, Bool] = (
    for (i <- 0 until enNum)
      yield confDataMem(i) -> io.enVec(i)
  ).toMap

  // Store wires
  val sb      : SInt = dontTouch(WireInit(rs2_data(7, 0).asSInt))
  val sh      : SInt = dontTouch(WireInit(rs2_data(15, 0).asSInt))
  val sw      : SInt = dontTouch(WireInit(rs2_data))

  // Storing to data memory
  io.dataOut := MuxCase(0.S, Seq(
          sb_en -> sb,
          sh_en -> sh,
          sw_en -> sw
  ))
  
  // Load wires
  val lb : SInt = dontTouch(WireInit(dataIn(7, 0).asSInt))
  val lh : SInt = dontTouch(WireInit(dataIn(15, 0).asSInt))
  val lw : SInt = dontTouch(WireInit(dataIn))
  val lbu: UInt = dontTouch(WireInit(dataIn(7, 0)))
  val lhu: UInt = dontTouch(WireInit(dataIn(15, 0)))

  // Loading data from data memory
  io.out := MuxCase(0.S, Seq(
          lb_en  -> lb,
          lh_en  -> lh,
          lw_en  -> lw,
          lbu_en -> lbu.asSInt,
          lhu_en -> lhu.asSInt
  ))

  Seq(
          io.addrOut, io.loadEn, io.storeEn
  ) zip Seq(
          address, load_en, str_en
  ) foreach {
          x => x._1 := x._2
  }
}
