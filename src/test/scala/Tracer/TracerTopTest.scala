package Tracer

import chiseltest._
import org.scalatest._/*freespec.AnyFreeSpec*/
import chiseltest.internal._
import chiseltest.experimental.TestOptionBuilder._

class TracerTopTest extends FreeSpec/*AnyFreeSpec*/ with ChiselScalatestTester {
        def getProgFile: Option[String] = {
                if (scalaTestContext.value.get.configMap.contains("progFile")) {
                        Some(scalaTestContext.value.get.configMap("progFile").toString)
                } else {
                        None
                }
        }

        def getDataFile: Option[String] = {
                if (scalaTestContext.value.get.configMap.contains("dataFile")) {
                        Some(scalaTestContext.value.get.configMap("dataFile").toString)
                } else {
                        None
                }
        }

        val progFile = Some("./asm/assembly.hex")
        val dataFile = None

        "Tracer" in {
                test(new TracerTop(progFile, dataFile)).withAnnotations(Seq(VerilatorBackendAnnotation)) {
                        x => x.clock.step(500)
                }
        }
}

