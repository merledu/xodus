package xodus.configs


trait Configs {
  // Core
  val XLEN            = 32
  val BYTE_WIDTH      = 8
  val HALF_WORD_WIDTH = 16
  val REG_ADDR_WIDTH  = 5
  val OPCODE_WIDTH    = 7
  val FUNCT3_WIDTH    = 3
  val FUNCT7_WIDTH    = 7

  // SRAM
  val WMASK_WIDTH = 4
  val ADDR_WIDTH  = 8
}
