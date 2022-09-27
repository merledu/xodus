package Top

import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class TopTest extends AnyFreeSpec with ChiselScalatestTester
{
    "XODUS32_5S" in
    {
        test(new Top())
        {
            x => x.clock.step(500)
        }
    }
}
