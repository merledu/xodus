package xodus.configs


class ConfigMaps {
  val params = Map(
    "XLEN"         -> 32,
    "RegAddrWidth" -> 5,
    "OpcodeWidth"  -> 7,
    "Funct3Width"  -> 3,
    "Funct7Width"  -> 7,

    // Other
    "MemDepth" -> 32
  )

  val iOpcodeMap = Map(
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

  val iInstMap = ((
    Map(  // 0 - 5
      "addi"  -> "b000",
      "slti"  -> "b010",
      "sltiu" -> "b011",
      "andi"  -> "b111",
      "ori"   -> "b110",
      "xori"  -> "b100",
    ).map(
      x => x._1 -> (x._2, "iArith")
    ) ++ Map(  // 6 - 10
      "lb"  -> "b000",
      "lh"  -> "b001",
      "lw"  -> "b010",
      "lbu" -> "b100",
      "lhu" -> "b101"
    ).map(
      x => x._1 -> (x._2, "load")
    ) ++ Map(  // 11
      "jalr" -> ("b000", "jalr")
    )).map(
      x => x._1 -> (x._2, "I")
    ) ++ Map(  // 12 - 14
      "sb" -> "b000",
      "sh" -> "b001",
      "sw" -> "b010"
    ).map(
      x => x._1 -> ((x._2, "store"), "S")
    ) ++ Map(  // 15 - 20
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
      "funct3" -> x._2._1._1,
      "opcode" -> iOpcodeMap(x._2._2)(x._2._1._2)
    )
  ) ++ Map(  // 21 - 23
    "slli"  -> ("b001", "b0000000"),
    "srli"  -> ("b101", "b0000000"),
    "srai"  -> ("b101", "b0100000")
  ).map(
    x => x._1 -> Map(
      "imm31_25" -> x._2._2,
      "funct3"   -> x._2._1,
      "opcode"   -> iOpcodeMap("I")("iArith")
    )
  ) ++ (
    Seq(  // 24 - 25
      "lui", "auipc"
    ).map(
      x => x -> "U"
    ) ++ Seq(  // 26
      "jal" -> "J"
    )).map(
    x => x._1 -> Map(
      "opcode" -> iOpcodeMap(x._2)(x._1)
    )
  ).toMap ++ Map(  // 27 - 36
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
      "funct7" -> x._2._2,
      "funct3" -> x._2._1,
      "opcode" -> iOpcodeMap("R")("iArith")
    )
  )

  val iAluEn = Seq(
    "+", "s<", "u<", "&",   "|",
    "^", "<<", ">>", ">>>", "lui",
    "-", "imm"
  )

  val iCuEn = iAluEn
}
