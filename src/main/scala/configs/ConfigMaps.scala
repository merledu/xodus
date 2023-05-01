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

  val iInstMap: Map[String, Map[String, String]] = ((
    Map(
      "addi"  -> "b000",
      "slti"  -> "b010",
      "sltiu" -> "b011",
      "andi"  -> "b111",
      "ori"   -> "b110",
      "xori"  -> "b100",
    ).map(
      x => x._1 -> (x._2, "iArith")
    ) ++ Map(
      "lb"  -> "b000",
      "lh"  -> "b001",
      "lw"  -> "b010",
      "lbu" -> "b100",
      "lhu" -> "b101"
    ).map(
      x => x._1 -> (x._2, "load")
    ) ++ Map(
      "jalr" -> ("b000", "jalr")
    )).map(
      x => x._1 -> (x._2, "I")
    ) ++ Map(
      "sb" -> "b000",
      "sh" -> "b001",
      "sw" -> "b010"
    ).map(
      x => x._1 -> ((x._2, "store"), "S")
    ) ++ Map(
      "beq"  -> "b000",
      "bne"  -> "b001",
      "blt"  -> "b100",
      "bge"  -> "b101",
      "bltu" -> "b110",
      "bgeu" -> "b111"
    ).map(
      x => x._1 -> ((x._2, "branch"), "B")
    )
  ).map(
    x => x._1 -> Map(
      "opcode" -> iOpcodeMap(x._2._2)(x._2._1._2),
      "funct3" -> x._2._1._1
    )
  ) ++ Map(
    "slli"  -> ("b001", "b0000000"),
    "srli"  -> ("b101", "b0000000"),
    "srai"  -> ("b101", "b0100000")
  ).map(
    x => x._1 -> Map(
      "opcode"   -> iOpcodeMap("I")("iArith"),
      "funct3"   -> x._2._1,
      "imm31_25" -> x._2._2
    )
  ) ++ (
    Seq(
      "lui", "auipc"
    ).map(
      x => x -> "U"
    ) ++ Seq(
      "jal" -> "J"
    )).map(
    x => x._1 -> Map(
      "opcode" -> iOpcodeMap(x._2)(x._1)
    )
  ).toMap ++ Map(
    "add"  -> ("b000", "b0000000"),
    "sub"  -> ("b000", "b0100000"),
    "sll"  -> ("b001", "b0000000"),
    "slt"  -> ("b010", "b0000000"),
    "sltu" -> ("b011", "b0000000"),
    "xor"  -> ("b100", "b0000000"),
    "srl"  -> ("b101", "b0000000"),
    "sra"  -> ("b101", "b0100000"),
    "or"   -> ("b110", "b0000000"),
    "and"  -> ("b111", "b0000000")
  ).map(
    x => x._1 -> Map(
      "opcode" -> iOpcodeMap("R")("iArith"),
      "funct3" -> x._2._1,
      "funct7" -> x._2._2
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
