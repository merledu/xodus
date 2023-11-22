package xodus.core

import chiseltest._,
       org.scalatest.freespec.AnyFreeSpec


class CoreTest extends AnyFreeSpec with ChiselScalatestTester {
  "core" in {
    test(new Core).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      xodus =>
        xodus.clock.step(20)
    }
  }
}
