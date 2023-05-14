package xodus.core.fetch_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class InstMemJuncIO extends Bundle with Configs {
  // Input ports
  val addrIn: UInt = Input(UInt(MemDepth.W))
  val instIn: UInt = Input(UInt(XLEN.W))

  // Output ports
  val addrOut: UInt      = Flipped(addrIn)
  val instOut: UInt      = Flipped(instIn)
  val en     : Vec[Bool] = Output(Vec(2, Bool()))
}


class InstMemJunc extends RawModule with Configs {
  val io: InstMemJuncIO = IO(new InstMemJuncIO)

  // Wires
  val en: Map[String, Bool] = Map(
    "load"  -> 1.B,
    "store" -> 0.B
  )


  /********************
   * Interconnections *
   ********************/

  Seq(
    io.addrIn -> io.addrOut,
    io.instIn -> io.instOut
  ).map(
    x => x._2 := x._1
  )

  en.values.zipWithIndex.map(
    x => io.en(x._2) := x._1
  )



  // Debug
  if (Debug) {
    val debug_addrIn: UInt = dontTouch(WireInit(io.addrIn))
    val debug_instIn: UInt = dontTouch(WireInit(io.instIn))
    val debug_load_en: Bool = dontTouch(WireInit(en("load")))
    val debug_store_en: Bool = dontTouch(WireInit(en("store")))
  }
}
