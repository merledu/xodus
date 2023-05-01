package xodus.core

import chisel3._, chiseltest._, org.scalatest.freespec.AnyFreeSpec


class CoreTest extends AnyFreeSpec with ChiselScalatestTester {
  "Core" in {
    test(new Core) {
      core =>
        core.clock.step(3)
    }
  }
}
