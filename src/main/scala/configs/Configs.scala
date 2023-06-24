package configs


trait Configs {
  // Core
  val XLEN         : Int = 32
  val ByteWidth    : Int = 8
  val HalfWordWidth: Int = 16
  val RegAddrWidth : Int = 5
  val OpcodeWidth  : Int = 7
  val Funct3Width  : Int = 3
  val Funct7Width  : Int = 7
  val OffsetWidth  : Int = 2

  // SRAM
  val WMaskWidth: Int = 4
  val AddrWidth : Int = 8
}
