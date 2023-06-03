package configs


class ISA {
  val opcodes: Map[String, Seq[String]] = Map(
    "R" -> Seq(
      "0110011"   // integer arithmetic
    ),
    "I" -> Seq(
      "1100111",  // jalr
      "0000011",  // integer load
      "0010011"   // integer arithmetic
    ),
    "S" -> Seq(
      "0100011"   // integer store
    ),
    "B" -> Seq(
      "1100011"   // branch
    ),
    "U" -> Seq(
      "0110111",  // lui
      "0010111"   // auipc
    ),
    "J" -> Seq(
      "1101111"   // jal
    )
  )

  val instID: Map[String, Seq[String]] = (Seq(
    "lui", "auipc"
  ).zipWithIndex.map(
    x => x._1 -> ("U", x._2)
  ) ++ Seq(
    "jal" -> ("J", 0)
  )).toMap.map(
    x => x._1 -> Seq(opcodes(x._2._1)(x._2._2))
  ) ++ ((Map(
    "jalr" -> (Seq("000"), 0)
  ) ++ Map(
    "lb"  -> Seq("000"),
    "lh"  -> Seq("001"),
    "lw"  -> Seq("010"),
    "lbu" -> Seq("100"),
    "lhu" -> Seq("101")
  ).map(
    x => x._1 -> (x._2, 1)
  ) ++ Map(
    "addi"  -> Seq("000"),
    "slti"  -> Seq("010"),
    "sltiu" -> Seq("011"),
    "xori"  -> Seq("100"),
    "ori"   -> Seq("110"),
    "andi"  -> Seq("111"),
    "slli"  -> Seq("0000000", "001"),
    "srli"  -> Seq("0000000", "101"),
    "srai"  -> Seq("0100000", "101")
  ).map(
    x => x._1 -> (x._2, 2)
  )).map(
    x => x._1 -> (x._2._1, "I", x._2._2)
  ) ++ (Map(
    "sb" -> Seq("000"),
    "sh" -> Seq("001"),
    "sw" -> Seq("010")
  ).map(
    x => x._1 -> (x._2, "S")
  ) ++ Map(
    "beq"  -> Seq("000"),
    "bne"  -> Seq("001"),
    "blt"  -> Seq("100"),
    "bge"  -> Seq("101"),
    "bltu" -> Seq("110"),
    "bgeu" -> Seq("111")
  ).map(
    x => x._1 -> (x._2, "B")
  ) ++ Map(
    "add"  -> Seq("0000000", "000"),
    "sub"  -> Seq("0100000", "000"),
    "sll"  -> Seq("0000000", "001"),
    "slt"  -> Seq("0000000", "010"),
    "sltu" -> Seq("0000000", "011"),
    "xor"  -> Seq("0000000", "100"),
    "srl"  -> Seq("0000000", "101"),
    "sra"  -> Seq("0100000", "101"),
    "or"   -> Seq("0000000", "110"),
    "and"  -> Seq("0000000", "111")
  ).map(
    x => x._1 -> (x._2, "R")
  )).map(
    x => x._1 -> (x._2._1, x._2._2, 0)
  )).map(
    x => x._1 -> (x._2._1 ++ Seq(opcodes(x._2._2)(x._2._3)))
  )
}
