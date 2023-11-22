//package top
//
//import chisel3._,
//       chiseltest._,
//       org.scalatest.freespec.AnyFreeSpec
//
//
//class TopTest extends AnyFreeSpec with ChiselScalatestTester {
//  def getHexFiles: Seq[Option[String]] = Seq(
//    "iMemFile", "dMemFile"
//  ).map(
//    x => if (scalaTestContext.value.get.configMap.contains(x))
//      Some(scalaTestContext.value.get.configMap(x).toString)
//    else
//      None
//  )
//
//
//  "XODUS" in {
//    test(new Top(getHexFiles)).withAnnotations(Seq(VerilatorBackendAnnotation)) {
//      xodus => xodus.clock.step(500)
//    }
//  }
//}
