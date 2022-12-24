package Configs

object Params {
  val params: Map[String, Map[String, Int]] = Map(
    "rv32i" -> Map(
      // Core Parameters
      "XLEN"       -> 32,  // Instruction/Register length
      "regAddrLen" -> 5,   // Register Address length
      "opcodeLen"  -> 7,   // Opcode length
      "f3Len"      -> 3,   // Func3 length
      "f7Len"      -> 7,   // Func7 length

      // RVFI Parameters
      "NRET"       -> 1,
      "ILEN"       -> 32
    ),
    "rv64i" -> Map(
      // Core Parameters
      "XLEN" -> 64
    ),
    "sram" -> Map(
      "depth" -> 16  // SRAM memory depth
    )
  )
}
