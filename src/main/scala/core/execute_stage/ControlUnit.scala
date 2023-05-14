package xodus.core.execute_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.core.decode_stage.DecoderIO


class ControlUnitIO extends Bundle with Configs {
  // Input ports
  val opcode     : UInt = Flipped(new DecoderIO().opcode)
  val funct3     : UInt = Flipped(new DecoderIO().funct3)
  val funct7_imm7: UInt = Flipped(new DecoderIO().funct7_imm7)

  // Output ports
  val en: Vec[Bool] = Output(Vec(arch("cuEn").length, Bool()))
}


class ControlUnit extends RawModule with Configs {
  val io: ControlUnitIO = IO(new ControlUnitIO)

  // Wires
  val idWires: Map[String, UInt] = Map(
    "opcode"                          -> io.opcode,
    "funct3_opcode"                   -> Cat(io.funct3, io.opcode),
    "funct7/imm(11, 5)_funct3_opcode" -> Cat(io.funct7_imm7, io.funct3, io.opcode),
  )

  val instEn: Map[String, Bool] = Seq(
    "funct3_opcode"                   -> (0, 21),
    "funct7/imm(11, 5)_funct3_opcode" -> (21, 34),
    "opcode"                          -> (34, isa("insts").size)
  ).map(
    x => isa("insts").slice(x._2._1, x._2._2).map(
      y => y._1 -> (y._2.values.map(
        z => z.U
      ).reduce(
        (a, b) => Cat(a, b)
      ) === idWires(x._1))
    )
  ).reduce(
    (x, y) => x ++ y
  )

  val enWires: Map[String, Bool] = (Seq(
    Seq(  // s+
      "addi", "lb", "lh", "lw", "lbu",
      "lhu",  "sb", "sh", "sw", "add"
    ),
    Seq("slti", "slt"),           // s<
    Seq("sltiu", "sltu"),         // u<
    Seq("andi", "and"),           // &
    Seq("ori", "or"),             // |
    Seq("xori", "xor"),           // ^
    Seq("slli", "sll"),           // <<
    Seq("srli", "srl"),           // >>
    Seq("srai", "sra"),           // >>>
    Seq("lui"),                   // lui
    Seq("auipc", "jalr", "jal"),  // u+
    Seq("sub")                    // -
  ).map(
    x => x.map(
      y => instEn(y)
    ).reduce(
      (y, z) => y || z
    )
  ) ++ Seq(
    Seq(  // imm
      "I" -> Seq("load", "iArith"),
      "S" -> Seq("store"),
      "U" -> Seq("lui", "auipc")
    ),
    Seq("U" -> Seq("auipc"))  // auipc
  ).map(
    x => x.map(
      y => isa("opcodes")(y._1).map(
        z => io.opcode === isa("opcodes")(y._1)(z._1).U
      )
    ).reduce(
      (y, z) => y ++ z
    ).reduce(
      (y, z) => y || z
    )
  )).zipWithIndex.map(
    x => arch("cuEn")(x._2) -> x._1
  ).toMap


  /********************
   * Interconnections *
   ********************/

  for (i <- 0 until io.en.length) {
    io.en(i) := enWires.values.toSeq(i)
  }



  // Debug
  if (Debug) {
    val debug_addi_en                  : Bool = dontTouch(WireInit(instEn("addi")))
    val debug_slti_en                  : Bool = dontTouch(WireInit(instEn("slti")))
    val debug_sltiu_en                 : Bool = dontTouch(WireInit(instEn("sltiu")))
    val debug_andi_en                  : Bool = dontTouch(WireInit(instEn("andi")))
    val debug_ori_en                   : Bool = dontTouch(WireInit(instEn("ori")))
    val debug_xori_en                  : Bool = dontTouch(WireInit(instEn("xori")))
    val debug_lb_en                    : Bool = dontTouch(WireInit(instEn("lb")))
    val debug_lh_en                    : Bool = dontTouch(WireInit(instEn("lh")))
    val debug_lw_en                    : Bool = dontTouch(WireInit(instEn("lw")))
    val debug_lbu_en                   : Bool = dontTouch(WireInit(instEn("lbu")))
    val debug_lhu_en                   : Bool = dontTouch(WireInit(instEn("lhu")))
    val debug_jalr_en                  : Bool = dontTouch(WireInit(instEn("jalr")))
    val debug_sb_en                    : Bool = dontTouch(WireInit(instEn("sb")))
    val debug_sh_en                    : Bool = dontTouch(WireInit(instEn("sh")))
    val debug_sw_en                    : Bool = dontTouch(WireInit(instEn("sw")))
    val debug_beq_en                   : Bool = dontTouch(WireInit(instEn("beq")))
    val debug_bne_en                   : Bool = dontTouch(WireInit(instEn("bne")))
    val debug_blt_en                   : Bool = dontTouch(WireInit(instEn("blt")))
    val debug_bge_en                   : Bool = dontTouch(WireInit(instEn("bge")))
    val debug_bltu_en                  : Bool = dontTouch(WireInit(instEn("bltu")))
    val debug_bgeu_en                  : Bool = dontTouch(WireInit(instEn("bgeu")))
    val debug_slli_en                  : Bool = dontTouch(WireInit(instEn("slli")))
    val debug_srli_en                  : Bool = dontTouch(WireInit(instEn("srli")))
    val debug_srai_en                  : Bool = dontTouch(WireInit(instEn("srai")))
    val debug_add_en                   : Bool = dontTouch(WireInit(instEn("add")))
    val debug_sub_en                   : Bool = dontTouch(WireInit(instEn("sub")))
    val debug_sll_en                   : Bool = dontTouch(WireInit(instEn("sll")))
    val debug_slt_en                   : Bool = dontTouch(WireInit(instEn("slt")))
    val debug_sltu_en                  : Bool = dontTouch(WireInit(instEn("sltu")))
    val debug_xor_en                   : Bool = dontTouch(WireInit(instEn("xor")))
    val debug_srl_en                   : Bool = dontTouch(WireInit(instEn("srl")))
    val debug_sra_en                   : Bool = dontTouch(WireInit(instEn("sra")))
    val debug_or_en                    : Bool = dontTouch(WireInit(instEn("or")))
    val debug_and_en                   : Bool = dontTouch(WireInit(instEn("and")))
    val debug_lui_en                   : Bool = dontTouch(WireInit(instEn("lui")))
    val debug_auipc_en                 : Bool = dontTouch(WireInit(instEn("auipc")))
    val debug_jal_en                   : Bool = dontTouch(WireInit(instEn("jal")))
    val debug_signed_addition_en       : Bool = dontTouch(WireInit(enWires("s+")))
    val debug_signed_less_than_en      : Bool = dontTouch(WireInit(enWires("s<")))
    val debug_unsigned_less_than_en    : Bool = dontTouch(WireInit(enWires("u<")))
    val debug_and_op_en                : Bool = dontTouch(WireInit(enWires("&")))
    val debug_or_op_en                 : Bool = dontTouch(WireInit(enWires("|")))
    val debug_xor_op_en                : Bool = dontTouch(WireInit(enWires("^")))
    val debug_shift_left_logical_en    : Bool = dontTouch(WireInit(enWires("<<")))
    val debug_shift_right_logical_en   : Bool = dontTouch(WireInit(enWires(">>")))
    val debug_shift_right_arithmetic_en: Bool = dontTouch(WireInit(enWires(">>>")))
    val debug_lui_op_en                : Bool = dontTouch(WireInit(enWires("lui")))
    val debug_unsigned_addition_en     : Bool = dontTouch(WireInit(enWires("u+")))
    val debug_subtraction_en           : Bool = dontTouch(WireInit(enWires("-")))
    val debug_imm_en                   : Bool = dontTouch(WireInit(enWires("imm")))
    val debug_auipc_op_en              : Bool = dontTouch(WireInit(enWires("auipc")))

    val debug_en = dontTouch(WireInit(io.en))
  }
}
