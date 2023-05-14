package xodus.top

import chisel3._,
       chiseltest._,
       org.scalatest.freespec.AnyFreeSpec


class TopTest extends AnyFreeSpec with ChiselScalatestTester {
  "XODUS" in {
    test(new Top()).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      xodus => xodus.clock.step(500)
    }
  }
}
