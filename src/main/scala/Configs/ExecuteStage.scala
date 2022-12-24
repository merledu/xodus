package Configs


object ALUConf {
  val opSeq: Seq[String] = Seq(
    "imm",  "add",   "sub", "sll", "slt",
    "sltu", "xor",   "srl", "sra", "or",
    "and",  "auipc", "lui", "jal"
    )
}


object ControlUnitConf {
  val rv32i: Map[String, Seq[String]] = Map(
    "ctrlSeq" -> Seq(
      "add",   "sub",   "sll", "slt",  "sltu",
      "xor",   "srl",   "sra", "or",   "and",
      "imm",   "auipc", "lui", "load", "lb",
      "lh",    "lw",    "lbu",  "lhu", "lwu",
      "store", "sb",    "sh",   "sw"
    )
  )

  val rv64i: Map[String, Seq[String]] = rv32i.map(
    x => x._1 -> (x._2 ++ Seq(
      "ld", "sd"
    ))
  )
}
