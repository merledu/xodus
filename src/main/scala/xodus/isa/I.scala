package xodus.isa


class I {
  val opcodes: Seq[String] = Seq(
    // R-Type -> 0
    "0110011",  // integer arithmetic -> 0
    // I-Type -> 1 to 3
    "1100111",  // jalr               -> 1
    "0000011",  // integer load       -> 2
    "0010011",  // integer arithmetic -> 3
    // S-Type -> 4
    "0100011",  // integer store -> 4
    // B-Type -> 5
    "1100011",  // branch -> 5
    // U-Type -> 6 to 7
    "0110111",  // lui   -> 6
    "0010111",  // auipc -> 7
    // J-Type -> 8
    "1101111"   // jal -> 8
  )

  val insts: Seq[String] = Seq(
    // 0
    "000" + opcodes(1)  // jalr -> 0
  ) ++ Seq(
    // 1 to 6
    "000",  // beq  -> 1
    "001",  // bne  -> 2
    "100",  // blt  -> 3
    "101",  // bge  -> 4
    "110",  // bltu -> 5
    "111"   // bgeu -> 6
  ).map(_ + opcodes(5)) ++ Seq(
    // 7 to 11
    "000",  // lb  -> 7
    "001",  // lh  -> 8
    "010",  // lw  -> 9
    "100",  // lbu -> 10
    "101"   // lhu -> 11
  ).map(_ + opcodes(2)) ++ Seq(
    // 12 to 14
    "000",  // sb -> 12
    "001",  // sh -> 13
    "010"   // sw -> 14
  ).map(_ + opcodes(4)) ++ Seq(
    // 15 to 23
    "000",         // addi  -> 15
    "010",         // slti  -> 16
    "011",         // sltiu -> 17
    "100",         // xori  -> 18
    "110",         // ori   -> 19
    "111",         // andi  -> 20
    "0000000001",  // slli  -> 21
    "0000000101",  // srli  -> 22
    "0100000101"   // srai  -> 23
  ).map(_ + opcodes(3)) ++ Seq(
    // 24 to 33
    "0000000000",  // add  -> 24
    "0100000000",  // sub  -> 25
    "0000000001",  // sll  -> 26
    "0000000010",  // slt  -> 27
    "0000000011",  // sltu -> 28
    "0000000100",  // xor  -> 29
    "0000000101",  // srl  -> 30
    "0100000101",  // sra  -> 31
    "0000000110",  // or   -> 32
    "0000000111"   // and  -> 33
  ).map(_ + opcodes.head)
}