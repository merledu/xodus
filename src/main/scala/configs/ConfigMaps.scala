package xodus.configs


object ConfigMaps {
  val variants  : Seq[String] = Seq("32", "64")
  val extensions: Seq[String] = Seq("i")

  val variables : Map[Int, Map[String, Map[String, Int]]] = Map(
    "i" -> Map(
      "XLEN"         -> Seq(32, 64),
      "RegAddrWidth" -> Seq(5, 6)
    )
  ).map(
    x => extensions.indexOf(x._1) -> x._2.map(
      y => y._1 -> variants.map(
        z => (z -> y._2(variants.indexOf(z)))
      ).toMap
    )
  )
}
