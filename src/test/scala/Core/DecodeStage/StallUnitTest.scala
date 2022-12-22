//package DecodeStage
//
//import chisel3._
//import chiseltest._
//import org.scalatest.freespec.AnyFreeSpec
//import scala.util.Random
//
//import Configs._
//
//
//class StallUnitTest extends AnyFreeSpec with ChiselScalatestTester {
//  "Stall Unit" in {
//    test(new StallUnit(Params.params, false)) {
//      stallUnit =>
//        val debug: Boolean = false
//
//        for (i <- 0 until 100) {
//          val rdAddr: Int = Random.nextInt(32)
//          val rs1Addr: Int = Random.nextInt(32)
//          val rs2Addr: Int = Random.nextInt(32)
//
//          val loadEn: Int = Random.nextInt(2)
//
//          stallUnit.io.rAddr.bits(0).poke(rdAddr.U)
//          stallUnit.io.rAddr.bits(1).poke(rs1Addr.U)
//          stallUnit.io.rAddr.bits(2).poke(rs2Addr.U)
//
//          stallUnit.io.rAddr.valid.poke(loadEn.B)
//
//          stallUnit.clock.step(1)
//        }
//    }
//  }
//}
