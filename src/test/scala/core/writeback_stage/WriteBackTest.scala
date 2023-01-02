package writeback_stage

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import scala.util.Random

import configs._


class WriteBackTest extends AnyFreeSpec with ChiselScalatestTester {
  "WriteBack" in {
    test(new WriteBack(
      params = Params.params("rv32i"),
      debug  = false
    )) {
      wb =>
        for (i <- 0 until 100) {
          val wbData: Seq[Int] = for (j <- 0 until 2) yield Random.nextInt()
          val loadEn: Boolean  = Random.nextBoolean()

          for (k <- 0 until 2) {
            wb.io.wbData(k).poke(wbData(k))
          }
          wb.io.loadEn.poke(loadEn.B)

          wb.clock.step(1)
        }
    }
  }
}
