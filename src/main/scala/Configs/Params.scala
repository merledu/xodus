package Configs

object Params {
  val params: Map[String, Map[String, Int]] = Map(
    "rv32i" -> Map(
      // Core Parameters
      "XLEN"       -> 32,  // Instruction/Register length
      "memDepth"   -> 16,  // Memory Depth
      "regAddrLen" -> 5,   // Register Address length
      "opcodeLen"  -> 7,   // Opcode length
      "f3Len"      -> 3,   // Func3 length
      "f7Len"      -> 7,   // Func7 length
      "numRegAddr" -> 3,

      // RVFI Parameters
      "NRET"       -> 1,
      "ILEN"       -> 32
      ),
    "rv64i" -> Map(
      // Core Parameters
      "XLEN" -> 64
      )
    )
}
