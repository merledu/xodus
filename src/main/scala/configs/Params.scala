package configs


object Params {
  val params: Map[String, Map[String, Int]] = Map(
    "rv32i" -> Map(
      "XLEN" -> 32,
      "REG_ADDR_LEN" -> 5,
      "OPCODE_LEN"   -> 7,
      "F3LEN"        -> 3,
      "F7LEN"        -> 7,

      "NRET"         -> 1,
      "ILEN"         -> 32
    ),
    "rv64i" -> Map(
      "XLEN" -> 64
    ),
    "sram" -> Map(
      "DEPTH" -> 16
    )
  )
}


trait Configs {
  // Core Parameters
  val ext: String = "rv32i"
  val XLEN        : Int = Params.params(ext)("XLEN")
  val REG_ADDR_LEN: Int = Params.params(ext)("REG_ADDR_LEN")
  val OPCODE_LEN  : Int = Params.params(ext)("OPCODE_LEN")
  val F3LEN       : Int = Params.params(ext)("F3LEN")
  val F7LEN       : Int = Params.params(ext)("F7LEN")

  // ISA Constants
  val opcodes: Map[String, Map[String, Int]] = RV32I.opcodes
  val components

  // RVFI Parameters
  val NRET: Int = 1
  val ILEN: Int = 32

  // SRAM Parameters
  val DEPTH: Int = 16
}
