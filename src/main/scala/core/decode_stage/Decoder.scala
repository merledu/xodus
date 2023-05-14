package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs


class DecoderIO extends Bundle with Configs {
  // Input ports
  val inst: UInt = Input(UInt(XLEN.W))
  
  // Output ports
  val opcode     : UInt      = Output(UInt(OpcodeWidth.W))
  val rAddr      : Vec[UInt] = Output(Vec(3, UInt(RegAddrWidth.W)))
  val funct3     : UInt      = Output(UInt(Funct3Width.W))
  val funct7_imm7: UInt      = Output(UInt(Funct7Width.W))
  val imm        : SInt      = Output(SInt(XLEN.W))
}


class Decoder extends RawModule with Configs {
  val io: DecoderIO = IO(new DecoderIO)

  // Wires
  val uintWires: Map[String, UInt] = Map(
    "opcode"            -> (6, 0),
    "rd"                -> (11, 7),
    "funct3"            -> (14, 12),
    "rs1"               -> (19, 15),
    "rs2"               -> (24, 20),
    "funct7/imm(11, 5)" -> (31, 25)
  ).map(
    x => x._1 -> io.inst(x._2._1, x._2._2)
  )

  // Immediate Generation
  val imm: Map[String, SInt] = Map(
    "I" -> io.inst(31, 20),
    "S" -> Cat(io.inst(31, 25), io.inst(11, 7)),
    "B" -> Cat(io.inst(31), io.inst(7), io.inst(30, 25), io.inst(11, 8), 0.U(1.W)),
    "U" -> Cat(io.inst(31, 12), 0.U(12.W)),
    "J" -> Cat(io.inst(31), io.inst(19, 12), io.inst(20), io.inst(30, 21), 0.U(1.W))
  ).map(
    x => x._1 -> x._2.asSInt
  )

  /********************
   * Interconnections *
   ********************/

  // Default: I immediate
  io.imm := MuxCase(imm("I"), Seq(
    "S", "B", "U", "J"
  ).map(
    x => isa("opcodes")(x).values.map(
      y => uintWires("opcode") === y.U
    ).reduce(
      (a, b) => a || b
    ) -> imm(x)
  ))

  Seq(
    "opcode"            -> io.opcode,
    "rd"                -> io.rAddr(0),
    "funct3"            -> io.funct3,
    "funct7/imm(11, 5)" -> io.funct7_imm7
  ).map(
    x => x._2 := uintWires(x._1)
  )

  for (i <- 1 to 2) {
    io.rAddr(i) := uintWires(s"rs${i}")
  }



  // Debug
  if (Debug) {
    val debug_opcode     : UInt = dontTouch(WireInit(uintWires("opcode")))
    val debug_rd         : UInt = dontTouch(WireInit(uintWires("rd")))
    val debug_funct3     : UInt = dontTouch(WireInit(uintWires("funct3")))
    val debug_rs1        : UInt = dontTouch(WireInit(uintWires("rs1")))
    val debug_rs2        : UInt = dontTouch(WireInit(uintWires("rs2")))
    val debug_funct7_imm7: UInt = dontTouch(WireInit(uintWires("funct7/imm(11, 5)")))
    val debug_I_imm      : SInt = dontTouch(WireInit(imm("I")))
    val debug_S_imm      : SInt = dontTouch(WireInit(imm("S")))
    val debug_B_imm      : SInt = dontTouch(WireInit(imm("B")))
    val debug_U_imm      : SInt = dontTouch(WireInit(imm("U")))
    val debug_J_imm      : SInt = dontTouch(WireInit(imm("J")))
  }
}
