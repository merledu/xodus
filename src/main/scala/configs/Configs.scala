package xodus.configs


trait Configs {
  val Debug: Boolean = true

  val XLEN        : Int = 32
  val RegAddrWidth: Int = 5
  val OpcodeWidth : Int = 7
  val Funct3Width : Int = 3
  val Funct7Width : Int = 7
  val MemDepth    : Int = 16
}
