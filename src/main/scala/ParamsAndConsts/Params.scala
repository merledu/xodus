package ParamsAndConsts

object Params {
    val PARAMS: Map[String, Int] = Map(
        // Core Parameters
        "XLEN"       -> 32,  // Instruction/Register length
        "MDEPTH"     -> 16,  // Memory Depth
        "REGADDRLEN" -> 5,   // Register Address length
        "OPCODELEN"  -> 7,   // Opcode length
        "F3LEN"      -> 3,   // Func3 length
        "F7LEN"      -> 7,   // Func7 length

        // RVFI Parameters
        "NRET"       -> 1,
        "ILEN"       -> 32
    )
}
