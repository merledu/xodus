package ExecuteStage

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import scala.util.Random

import Configs._


class ALUTest extends AnyFreeSpec with ChiselScalatestTester {
  "ALU" in {
    test(new ALU(
      params  = Params.params("rv32i"),
      confAlu = RV32I.conf("alu"),
      debug   = false
    )) {
      alu =>
        val debug: Boolean = false
        val aluNum: Int = RV32I.conf("alu").length

        for (i <- 0 until 100) {
          var pc: Long = Random.nextLong() & 0xFFFFFFFFL
          while ((pc % 4) != 0) {
            pc = Random.nextLong() & 0xFFFFFFFFL
          }

          for (i <- 0 until 3) {
            alu.io.operands(i).poke(Random.nextInt().S)
          }

          alu.io.pc.poke(pc.U)

          for (i <- 0 until aluNum) {
            alu.io.en(i).poke(Random.nextInt(2).B)
          }

          alu.clock.step(1)
        }
    }
  }
}
