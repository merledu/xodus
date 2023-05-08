package xodus.configs


trait Configs {
  val conf = new ConfigMaps

  val variant    = "32"
  val extensions = "i"

  val Debug = true

  val XLEN         = conf.params("XLEN")(variant)
  val RegAddrWidth = conf.params("RegAddrWidth")(variant)
  val OpcodeWidth  = conf.params("OpcodeWidth")(variant)
  val Funct3Width  = conf.params("Funct3Width")(variant)
  val Funct7Width  = conf.params("Funct7Width")(variant)
  val MemDepth     = conf.params("MemDepth")(variant)

  val isaMaps = Map(
    "opcodes" -> conf.opcodeMap,
    "insts"   -> conf.instMap
  )

  val archMaps = Map(
    "cuEn" -> conf.cuEnMap
  )

  val isa = isaMaps.map(
    x => x._1 -> (
      for (ext <- extensions)
        yield x._2(ext.toString)
    ).reduce(
      (y, z) => y ++ z
    )
  )

  val arch = archMaps.map(
    x => x._1 -> (
      for (ext <- extensions)
        yield x._2(ext.toString)
    ).reduce(
      (y, z) => y ++ z
    )
  )
}
