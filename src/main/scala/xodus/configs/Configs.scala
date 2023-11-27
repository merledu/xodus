package xodus.configs


trait Configs {
  // Core
  val XLEN             : Int = 32
  val BYTE_WIDTH       : Int = 8
  val HALF_WORD_WIDTH  : Int = 16
  val REG_ADDR_WIDTH   : Int = 5
  val OPCODE_WIDTH     : Int = 7
  val FUNCT3_WIDTH     : Int = 3
  val FUNCT7_WIDTH     : Int = 7

  // SRAM
  val WMASK_WIDTH: Int = 4
  val ADDR_WIDTH : Int = 8
}
