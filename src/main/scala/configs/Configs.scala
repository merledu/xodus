package xodus.configs


trait Configs {
  val conf: ConfigMaps = new ConfigMaps

  val Debug: Boolean = true

  val XLEN        : Int = conf.params("XLEN")
  val RegAddrWidth: Int = conf.params("RegAddrWidth")
  val OpcodeWidth : Int = conf.params("OpcodeWidth")
  val Funct3Width : Int = conf.params("Funct3Width")
  val Funct7Width : Int = conf.params("Funct7Width")
  val MemDepth    : Int = conf.params("MemDepth")

  val isa: Map[String, Map[String, Map[String, String]]] = Map(
    "opcodes" -> conf.iOpcodeMap,
    "insts"   -> conf.iInstMap
  )

  val arch: Map[String, Seq[String]] = Map(
    "cuEn"  -> conf.iCuEn,
    "aluEn" -> conf.iAluEn
  )
}
