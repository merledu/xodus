package xodus.configs


class ConfigMaps {
  val variants: Seq[String] = Seq("32", "64")

  val params: Map[String, Map[String, Int]] = Map(
    // I Extension
    "XLEN"         -> Seq(32, 64),
    "RegAddrWidth" -> Seq(5, 6),
    "OpcodeWidth"  -> Seq(7, 0),
    "Funct3Width"  -> Seq(3, 0),
    "Funct7Width"  -> Seq(7, 0),

    // M Extension
    // A Extension

    // F Extension
    "FLEN"     -> Seq(32, 64),
    "ExpWidth" -> Seq(8, 11),
    "SigWidth" -> Seq(23, 52),

    // Other
    "MemDepth" -> (
      for (i <- 0 until 2)
        yield 32
    )
  ).map(
    x => x._1 -> (
      variants zip x._2
    ).map(
      y => y._1 -> y._2
    ).toMap
  )

  // I Extension
  val iOpcodeMap: Map[String, Map[String, String]] = Map(
    "R" -> Map("iArith" -> "b0110011"),
    "I" -> Map(
      "jalr"   -> "b1100111",
      "load"   -> "b0000011",
      "iArith" -> "b0010011"
    ),
    "S" -> Map("store" -> "b0100011"),
    "B" -> Map("branch" -> "b1100011"),
    "U" -> Map(
      "lui"   -> "b0110111",
      "auipc" -> "b0010111"
    ),
    "J" -> Map("jal" -> "b1101111")
  )

  val iInstMap: Map[String, Map[String, String]] = Map(
    "addi"  -> "b000",
    "slti"  -> "b010",
    "sltiu" -> "b011",
    "andi"  -> "b111",
    "ori"   -> "b110",
    "xori"  -> "b100",
    "slli"  -> "b001"
  ).map(
    x => x._1 -> Map(
      "opcode" -> iOpcodeMap("I")("iArith"),
      "funct3" -> x._2
    )
  )

  // Accumulated Maps
  val opcodeMap: Map[String, Map[String, Map[String, String]]] = Map(
    "i" -> iOpcodeMap
  )

  val instMap: Map[String, Map[String, Map[String, String]]] = Map(
    "i" -> iInstMap
  )
}
