package ExecuteStage

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import scala.util.Random

import ParamsAndConsts._


class ALUTest extends AnyFreeSpec with ChiselScalatestTester {
  "ALU" in {
    test(new ALU(Params.params, ALUConsts.opSeq, false)) {
      alu =>
        val debug: Boolean = false

        for (i <- 0 until 100) {
          val rs1Data: Int = Random.nextInt()
          val rs2Data: Int = Random.nextInt()
          val imm: Int = Random.nextInt()

          var pc: Long = Random.nextLong() & 0xFFFFFFFFL
          while ((pc % 4) != 0) {
            pc = Random.nextLong() & 0xFFFFFFFFL
          }

          val en: Seq[Int] = for (i <- 0 until ALUConsts.opSeq.length) yield Random.nextInt(2)

          alu.io.operands(0).poke(imm.S)
          alu.io.operands(1).poke(rs1Data.S)
          alu.io.operands(2).poke(rs2Data.S)

          alu.io.pc.poke(pc.U)

          for (i <- 0 until ALUConsts.opSeq.length) {
            alu.io.en(i).poke(en(i))
          }

          alu.clock.step(1)
        }
    }
  }
}
