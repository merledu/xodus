package xodus.decode_stage

import chisel3._,
       chiseltest._,
       org.scalatest.freespec.AnyFreeSpec,
       scala.util.Random

import xodus.configs._


class RegFileTest extends AnyFreeSpec with ChiselScalatestTester {
  "Register File" in {
    test(new RegFile()) {
      regfile =>
        for (i <- 0 until 100) {
          val rdAddr : Int = Random.nextInt(32)
          val rs1Addr: Int = Random.nextInt(32)
          val rs2Addr: Int = Random.nextInt(32)

          val rdData: Int = Random.nextInt()
          val wrEn: Int = Random.nextInt(2)

          regfile.io.rAddr(0).poke(rdAddr.U)
          regfile.io.rAddr(1).poke(rs1Addr.U)
          regfile.io.rAddr(2).poke(rs2Addr.U)

          regfile.io.rdData.valid.poke(wrEn.B)
          regfile.io.rdData.bits.poke(rdData.S)

          regfile.clock.step(1)
        }
    }
  }
}
