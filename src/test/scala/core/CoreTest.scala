package xodus.core

import chisel3._,
       chiseltest._,
       chiseltest.simulator.VerilatorBackendAnnotation,
       org.scalatest.freespec.AnyFreeSpec


class CoreTest extends AnyFreeSpec with ChiselScalatestTester {
  "Core" in {
    test(new Core).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      core =>
        core.clock.step(3)
    }
  }
}
