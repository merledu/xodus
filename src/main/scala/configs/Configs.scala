package configs


trait Configs {
  // Core
  val XLEN        : Int = 32
  val ByteWidth   : Int = 8
  val RegAddrWidth: Int = 5
  val OpcodeWidth : Int = 7
  val Funct3Width : Int = 3
  val Funct7Width : Int = 7

  // SRAM
  val NUM_WMASKS: Int    = 4
  val ADDR_WIDTH: Int    = 8
}
