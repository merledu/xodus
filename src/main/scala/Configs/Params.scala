package ParamsAndConsts

object Params {
    val params: Map[String, Int] = Map(
        // Core Parameters
        "XLEN"       -> 32,  // Instruction/Register length
        "memDepth"   -> 16,  // Memory Depth
        "regAddrLen" -> 5,   // Register Address length
        "opcodeLen"  -> 7,   // Opcode length
        "f3Len"      -> 3,   // Func3 length
        "f7Len"      -> 7,   // Func7 length

        // RVFI Parameters
        "NRET"       -> 1,
        "ILEN"       -> 32
    )
}
