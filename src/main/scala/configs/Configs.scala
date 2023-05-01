package xodus.configs


trait Configs {
  val conf: ConfigMaps = new ConfigMaps

  val variant   : String = "32"
  val extensions: String = "i"

  val Debug: Boolean = true

  val XLEN        : Int = conf.params("XLEN")(variant)
  val RegAddrWidth: Int = conf.params("RegAddrWidth")(variant)
  val OpcodeWidth : Int = conf.params("OpcodeWidth")(variant)
  val Funct3Width : Int = conf.params("Funct3Width")(variant)
  val Funct7Width : Int = conf.params("Funct7Width")(variant)
  val MemDepth    : Int = conf.params("MemDepth")(variant)

  val opcodes: Map[String, Map[String, String]] = (
    for (ext <- extensions)
      yield conf.opcodeMap(ext.toString)
  ).reduce(
    (x, y) => x ++ y
  )

  val insts: Map[String, Map[String, String]] = (
    for (ext <- extensions)
      yield conf.instMap(ext.toString)
  ).reduce(
    (x, y) => x ++ y
  )
}
