package decode_stage

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import scala.util.Random

import configs._


class RegFileTest extends AnyFreeSpec with ChiselScalatestTester {
  "Register File" in {
    test(new RegFile(
      params = Params.params("rv32i"),
      debug  = false
    )) {
      regfile =>
        val debug: Boolean = false

        // TODO: Add expects to output pins for testing
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
