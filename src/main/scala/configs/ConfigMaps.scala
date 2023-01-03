package configs


object ConfigMaps {
  val params: Map[String, Map[String, Int]] = Map(
    "rv32i" -> Map(
      "XLEN" -> 32,
      "REG_ADDR_LEN" -> 5,
      "OPCODE_LEN"   -> 7,
      "F3LEN"        -> 3,
      "F7LEN"        -> 7,

      "NRET"         -> 1,
      "ILEN"         -> 32
    ),
    "rv64i" -> Map(
      "XLEN" -> 64
    ),
    "sram" -> Map(
      "DEPTH" -> 16
    )
  )

  val opcodes: Map[String, Map[String, Int]] = Map(
    "R" -> Map(
      "math"  -> 0x33,
      "mathw" -> 0x3B
    ),
    "I" -> Map(
      "math"  -> 0x13,
      "mathw" -> 0x1B,
      "load"  -> 0x03,
      "jalr"  -> 0x67,
      "fence" -> 0x0F,
      "csr"   -> 0x73
    ),
    "S" -> Map(
      "store" -> 0x23
    ),
    "B" -> Map(
      "branch" -> 0x63
    ),
    "U" -> Map(
      "auipc" -> 0x17,
      "lui"   -> 0x37
    ),
    "J" -> Map(
      "jal" -> 0x6F
    )
  )

  val components: Map[String, Seq[Int]] = Map(
    "func3" -> Seq(
      0x0, 0x1, 0x2, 0x3, 0x4,
      0x5, 0x6, 0x7
    ),
    "func7" -> Seq(0x00, 0x20),
    "imm" -> Seq(0x000, 0x001),
    "imm11_5" -> Seq(0x00, 0x20)
  )

  val opID: Map[String, Map[String, Map[String, Int]]] = Map(
    "R" -> Map(
      "math" -> Map(
        "add"  -> ((components("func7")(0) << 10) | (components("func3")(0) << 7) | opcodes("R")("math")),
        "sub"  -> ((components("func7")(1) << 10) | (components("func3")(0) << 7) | opcodes("R")("math")),
        "sll"  -> ((components("func7")(0) << 10) | (components("func3")(1) << 7) | opcodes("R")("math")),
        "slt"  -> ((components("func7")(0) << 10) | (components("func3")(2) << 7) | opcodes("R")("math")),
        "sltu" -> ((components("func7")(0) << 10) | (components("func3")(3) << 7) | opcodes("R")("math")),
        "xor"  -> ((components("func7")(0) << 10) | (components("func3")(4) << 7) | opcodes("R")("math")),
        "srl"  -> ((components("func7")(0) << 10) | (components("func3")(5) << 7) | opcodes("R")("math")),
        "sra"  -> ((components("func7")(1) << 10) | (components("func3")(5) << 7) | opcodes("R")("math")),
        "or"   -> ((components("func7")(0) << 10) | (components("func3")(6) << 7) | opcodes("R")("math")),
        "and"  -> ((components("func7")(0) << 10) | (components("func3")(7) << 7) | opcodes("R")("math"))
      ),
      "mathw" -> Map(
        "addw" -> ((components("func7")(0) << 10) | (components("func3")(0) << 7) | opcodes("R")("mathw")),
        "subw" -> ((components("func7")(1) << 10) | (components("func3")(0) << 7) | opcodes("R")("mathw")),
        "sllw" -> ((components("func7")(0) << 10) | (components("func3")(1) << 7) | opcodes("R")("mathw")),
        "srlw" -> ((components("func7")(0) << 10) | (components("func3")(5) << 7) | opcodes("R")("mathw")),
        "sraw" -> ((components("func7")(1) << 10) | (components("func3")(5) << 7) | opcodes("R")("mathw"))
      )
    ),
    "I" -> Map(
      "math" -> Map(
        "addi"  -> ((components("func3")(0) << 7) | opcodes("I")("math")),
        "slli"  -> ((components("imm11_5")(0) << 10) | (components("func3")(1) << 7) | opcodes("I")("math")),
        "slti"  -> ((components("func3")(2) << 7) | opcodes("I")("math")),
        "sltiu" -> ((components("func3")(3) << 7) | opcodes("I")("math")),
        "xori"  -> ((components("func3")(4) << 7) | opcodes("I")("math")),
        "srli"  -> ((components("imm11_5")(0) << 10) | (components("func3")(5) << 7) | opcodes("I")("math")),
        "srai"  -> ((components("imm11_5")(1) << 10) | (components("func3")(5) << 7) | opcodes("I")("math")),
        "ori"   -> ((components("func3")(6) << 7) | opcodes("I")("math")),
        "andi"  -> ((components("func3")(7) << 7) | opcodes("I")("math")),
      ),
      "mathw" -> Map(
        "addiw" -> ((components("func3")(0) << 7) | opcodes("I")("mathw")),
        "slliw" -> ((components("imm11_5")(0) << 10) | (components("func3")(1) << 7) | opcodes("I")("mathw")),
        "srliw" -> ((components("imm11_5")(0) << 10) | (components("func3")(5) << 7) | opcodes("I")("mathw")),
        "srliw" -> ((components("imm11_5")(0) << 10) | (components("func3")(5) << 7) | opcodes("I")("mathw"))
      ),
      "load" -> Map(
        "lb"  -> ((components("func3")(0) << 7) | opcodes("I")("load")),
        "lh"  -> ((components("func3")(1) << 7) | opcodes("I")("load")),
        "lw"  -> ((components("func3")(2) << 7) | opcodes("I")("load")),
        "lbu" -> ((components("func3")(4) << 7) | opcodes("I")("load")),
        "lhu" -> ((components("func3")(5) << 7) | opcodes("I")("load")),
        "ld"  -> ((components("func3")(3) << 7) | opcodes("I")("load")),
        "lwu" -> ((components("func3")(6) << 7) | opcodes("I")("load"))
      ),
      "jalr" -> Map("jalr" -> ((components("func3")(0) << 7) | opcodes("I")("jalr"))),
      "fence" -> Map(
        "fence"   -> ((components("func3")(0) << 7) | opcodes("I")("fence")),
        "fence.i" -> ((components("func3")(1) << 7) | opcodes("I")("fence"))
      ),
      "csr" -> Map(
        "ecall"  -> ((components("imm")(0) << 10) | (components("func3")(0) << 7) | opcodes("I")("csr")),
        "ebreak" -> ((components("imm")(1) << 10) | (components("func3")(0) << 7) | opcodes("I")("csr")),
        "CSRRW"  -> ((components("func3")(1) << 7) | opcodes("I")("csr")),
        "CSRRS"  -> ((components("func3")(2) << 7) | opcodes("I")("csr")),
        "CSRRC"  -> ((components("func3")(3) << 7) | opcodes("I")("csr")),
        "CSRRWI" -> ((components("func3")(5) << 7) | opcodes("I")("csr")),
        "CSRRSI" -> ((components("func3")(6) << 7) | opcodes("I")("csr")),
        "CSRRCI" -> ((components("func3")(7) << 7) | opcodes("I")("csr"))
      )
    ),
    "S" -> Map(
      "store" -> Map(
        "sb" -> ((components("func3")(0) << 7) | opcodes("S")("store")),
        "sh" -> ((components("func3")(1) << 7) | opcodes("S")("store")),
        "sw" -> ((components("func3")(2) << 7) | opcodes("S")("store")),
        "sd" -> ((components("func3")(3) << 7) | opcodes("S")("store"))
      )
    ),
    "B" -> Map(
      "branch" -> Map(
        "beq"  -> ((components("func3")(0) << 7) | opcodes("B")("branch")),
        "bne"  -> ((components("func3")(1) << 7) | opcodes("B")("branch")),
        "blt"  -> ((components("func3")(4) << 7) | opcodes("B")("branch")),
        "bge"  -> ((components("func3")(5) << 7) | opcodes("B")("branch")),
        "bltu" -> ((components("func3")(6) << 7) | opcodes("B")("branch")),
        "bgeu" -> ((components("func3")(7) << 7) | opcodes("B")("branch"))
      )
    )
  )

  val conf: Map[String, Map[String, Seq[String]]] = Map(
    "imm" -> Seq(
      "rv32i", "rv64i"
    ).map(
      x => x -> Seq("immI", "immS", "immB", "immU", "immJ")
    ).toMap
  ) ++ Map(
    "alu" -> (
      Map(
        "rv32i" -> Seq[String](),
        "rv64i" -> Seq("addw", "sllw", "srlw", "sraw")
      ),
      Seq(
        "add",   "sub", "sll", "slt", "sltu",
        "xor",   "srl", "sra", "or",  "and",
        "auipc", "lui", "imm", "jump"
      )
    ),
    "dmem" -> (
      Map(
        "rv32i" -> Seq[String](),
        "rv64i" -> Seq("ld", "lwu", "sd")
      ),
      Seq(
        "load", "lb",    "lh", "lw", "lbu",
        "lhu",  "store", "sb", "sh", "sw"
      )
    )
  ).map(
    x => x._1 -> x._2._1.map(
      y => y._1 -> (y._2 ++ x._2._2)
    )
  )
}
