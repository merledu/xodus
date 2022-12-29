package MemoryStage

import chisel3._, chisel3.util._

import Memory.{MemReqIO, MemRespIO}


class DataMemRouterIO(
  params   :Map[String, Int],
  paramMap :Map[String, Map[String, Int]],
  enNum    :Int
) extends Bundle {
  // Input pins
  val data: SInt = Input(Vec(2, SInt(params("XLEN"))))
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
  val io   : DataMemRouterIO = IO(new DataMemRouterIO(
    params   = params,
    paramMap = paramMap,
    enNum    = enNum
  ))

  // Wires
  val storeWires: Map[String, SInt] = Map(
    "sb" -> (7, 0),
    "sh" -> (15, 0)
  ).map(
    x._1 -> io.data(1)(x._2._1, x._2._2).asSInt
  ) ++ Map(
    "sw" -> io.data(1)
  )

  val loadWires: Map[String, SInt] = Map(
    "lb" -> (7, 0),
    "lh" -> (15, 0)
  ).map(
    x => x._1 -> io.resp.bits.data(x._2._1, x._2._2)
  )
  /*++ Map(
    "lw" -> io.resp.bits.data
  )*/

  val enWires: Map[String, Bool] = (
    for (i <- 0 until enNum)
      yield confDataMem(i) -> io.en(i)
  ).toMap

  // Connections
  Seq(
    (io.out, MuxCase(
      0.S,
      for (i <- 1 to 3)
    ))
  )

  //// Storing to data memory
  //io.dataOut := MuxCase(0.S, Seq(
  //        sb_en -> sb,
  //        sh_en -> sh,
  //        sw_en -> sw
  //))
  //
  //// Loading data from data memory
  //io.out := MuxCase(0.S, Seq(
  //        lb_en  -> lb,
  //        lh_en  -> lh,
  //        lw_en  -> lw,
  //        lbu_en -> lbu.asSInt,
  //        lhu_en -> lhu.asSInt
  //))

  //Seq(
  //        io.addrOut, io.loadEn, io.storeEn
  //) zip Seq(
  //        address, load_en, str_en
  //) foreach {
  //        x => x._1 := x._2
  //}
}
