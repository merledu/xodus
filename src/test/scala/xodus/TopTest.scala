package xodus

import chiseltest._,
       org.scalatest.freespec.AnyFreeSpec


class TopTest extends AnyFreeSpec with ChiselScalatestTester {
  def getMemFiles: Seq[Option[String]] = Seq(
    "imem_file", "dmem_file"
  ).map(
    x => if (scalaTestContext.value.get.configMap.contains(x))
      Some(scalaTestContext.value.get.configMap(x).toString)
    else
      None
  )

  def getISA: Option[String] = if (scalaTestContext.value.get.configMap.contains("isa"))
    Some(scalaTestContext.value.get.configMap("isa").toString)
  else
    None


  "XODUS" in {
    test(new Top(getMemFiles)).withAnnotations(Seq(VerilatorBackendAnnotation)) {
      xodus => xodus.clock.step(500)
    }
  }
}