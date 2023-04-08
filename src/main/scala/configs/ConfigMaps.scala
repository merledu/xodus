package xodus.configs


object ConfigMaps {
  val variants  : Seq[String] = Seq("32", "64")
  val extensions: Seq[String] = Seq("i")
  val variables : Map[String, (Int, Int)] = Map(
    "XLEN"         -> (32, 64),
    "RegAddrWidth" -> (5, 6)
  )

  val varMap: Map[String, Map[String, Map[String, Int]]] = extensions.map(
    x => x -> variables.map(
      y => y -> variants
    )
  )
}
