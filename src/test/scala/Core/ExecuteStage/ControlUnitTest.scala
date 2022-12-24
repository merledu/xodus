package ExecuteStage

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import scala.util.Random

import Configs._


class ControlUnitTest extends AnyFreeSpec with ChiselScalatestTester {
  "Control Unit" in {
    test(new ControlUnit(
      params  = Params.params("rv32i"),
      opcodes = RV32I.opcodes,
      opID    = RV32I.opID,
      ctrlSeq = ControlUnitConf.rv32i("ctrlSeq"),
      debug   = false
    )) {
      controlunit =>
        controlunit.io.opcode.poke(0x13)
        controlunit.io.func3.poke(0x3)
        controlunit.io.func7.poke(0x00)
        controlunit.io.imm.poke(Random.nextInt().S)
        controlunit.io.stallEn.poke(Random.nextBoolean().B)

        controlunit.clock.step(1)
    }
  }
}
