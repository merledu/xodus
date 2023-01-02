package memory_stage

import chisel3._, chisel3.util._

import Memory.{MemReqIO, MemRespIO}


class DMemRouterIO(
  params   :Map[String, Int],
  paramMap :Map[String, Map[String, Int]],
  enNum    :Int
) extends Bundle {
  // Input pins
  val mem: Vec[SInt] = Input(Vec(2, SInt(params("XLEN").W)))
  val en : Vec[Bool] = Input(Vec(enNum, Bool()))

  // Output pins
  //val out: SInt = Output(SInt(params("XLEN").W))

  // Data Memory Interface
  val req: DecoupledIO[MemReqIO]  = Decoupled(new MemReqIO(
    params  = paramMap,
    dataMem = true
  ))
  //val resp: DecoupledIO[MemRespIO] = Flipped(Decoupled(new MemRespIO(params)))
}


class DMemRouter(
  params      :Map[String, Int],
  paramMap    :Map[String, Map[String, Int]],
  confDMem :Seq[String],
  debug       :Boolean
) extends Module {
  val enNum: Int = confDataMem.length
  val io   : DataMemRouterIO = IO(new DataMemRouterIO(
    params   = params,
    paramMap = paramMap,
    enNum    = enNum
  ))
  val dataSegments/*: Map[String, String]*/ = Map(
    "byte"      -> (7, 0),
    "half-word" -> (15, 0),
  )
  dataSegments.getClass

  // Wires
  val storeWires: Map[String, UInt] = Map(
    confDataMem(7) -> dataSegments("byte"),
    confDataMem(8) -> dataSegments("half-word")
  ).map(
    x => x._1 -> io.mem(1)(x._2._1, x._2._2)
  ) ++ Map(
    confDataMem(9) -> io.mem(1).asUInt
  )

  //val loadWires: Map[String, UInt] = Map(
  //  confDataMem(1) -> dataSegments("byte"),
  //  confDataMem(2) -> dataSegments("half-word")
  //).map(
  //  x => x._1 -> io.resp.bits.data(x._2._1, x._2._2).asUInt
  //) ++ Map(
  //  confDataMem(3) -> io.resp.bits.data
  //)

  val enWires: Map[String, Bool] = (
    for (i <- 0 until enNum)
      yield confDataMem(i) -> io.en(i)
  ).toMap

  // Connections
  // - Store data to DMem
  Seq(
    (io.req.valid, io.enWires())
    (io.req.bits.addr, io.mem(0).asUInt),
    (io.req.bits.data, MuxCase(
      0.U,
      for (i <- 7 to 9)
        yield confDataMem(i) -> storeWires(confDataMem(i))
    )),
  ).map(
    x => x._1 := Mux(io.req.ready, x._2, 0.U)
  )
}
