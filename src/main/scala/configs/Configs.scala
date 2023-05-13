package xodus.configs


trait Configs {
  val conf = new ConfigMaps

  val Debug = true

  val XLEN         = conf.params("XLEN")
  val RegAddrWidth = conf.params("RegAddrWidth")
  val OpcodeWidth  = conf.params("OpcodeWidth")
  val Funct3Width  = conf.params("Funct3Width")
  val Funct7Width  = conf.params("Funct7Width")
  val MemDepth     = conf.params("MemDepth")

  val isa = Map(
    "opcodes" -> conf.iOpcodeMap,
    "insts"   -> conf.iInstMap
  )

  val arch = Map(
    "cuEn"  -> conf.iCuEn,
    "aluEn" -> conf.iAluEn
  )
}
