package Configs


object ALUConf {
  val opSeq: Seq[String] = Seq(
    "imm",  "add",   "sub", "sll", "slt",
    "sltu", "xor",   "srl", "sra", "or",
    "and",  "auipc", "lui", "jal"
  )
}


object ControlUnitConf {
  val ctrlSeq: Seq[String] = Seq(
    "add", "sub",   "sll", "slt",  "sltu",
    "xor", "srl",   "sra", "or",   "and",
    "imm", "auipc", "lui", "load", "lb",
    "lh",  "lw",    "ld",  "lbu",  "lhu",
    "lwu", "store", "sb",  "sh",   "sw",
    "sd",  "wr"
    )
}
