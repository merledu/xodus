package Simulation

import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class SimulationTest extends AnyFreeSpec with ChiselScalatestTester
{
    "Simulation" in
    {
        test(new Simulation())
        {
            x => x.clock.step(100)
        }
    }
}
