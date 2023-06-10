package core.decode_stage

import chisel3._,
       chisel3.util._
import configs.{Configs, ISA}


class DecoderCtrl extends Bundle {
  val immSel: Vec[Bool] = Output(Vec(4, Bool()))
}


class RegFileCtrl extends Bundle {
  val write: Bool = Output(Bool())
}


class ALUCtrl extends Bundle {
  val immSel: Bool      = Output(Bool())
  val opSel : Vec[Bool] = Output(Vec(12, Bool()))
}


class DMemCtrl extends Bundle {
  val load : Vec[Bool] = Output(Vec(5, Bool()))
  val store: Vec[Bool] = Output(Vec(3, Bool()))
}


class Controls extends Bundle {
  val decoder: DecoderCtrl = new DecoderCtrl
  val regFile: RegFileCtrl = new RegFileCtrl
  val alu    : ALUCtrl     = new ALUCtrl
  val dMem   : DMemCtrl    = new DMemCtrl
}


class ControlUnitIO extends Bundle with Configs {
  val opcode     : UInt = Flipped(new DecoderIO().opcode)
  val funct3     : UInt = Flipped(new DecoderIO().funct3)
  val funct7_imm7: UInt = Flipped(new DecoderIO().funct7_imm7)

  val ctrl: Controls = new Controls
}


class ControlUnit extends RawModule {
  val io: ControlUnitIO = IO(new ControlUnitIO)

  val opcodes: Map[String, Seq[String]] = new ISA().opcodes
  val instID : Map[String, Seq[String]] = new ISA().instID

  // Enable wires
  val opcode: Vec[Bool] = VecInit(Seq(
    "R", "I", "S", "B", "U", "J"
  ).map(
    x => opcodes(x).map(
      y => io.opcode === ("b" + y).U
    ).reduce(
      (y, z) => y || z
    )
  ))

  val inst: Vec[Bool] = VecInit((Seq(
    "lui", "auipc", "jal"  // 0 - 2
  ).map(
    x => io.opcode -> x
  ) ++ Seq(
    "jalr", "lb",   "lh",   "lw",    "lbu",   // 3 - 7
    "lhu",  "addi", "slti", "sltiu", "xori",  // 8 - 12
    "ori",  "andi", "sb",   "sh",    "sw",    // 13 - 17
    "beq",  "bne",  "blt",  "bge",   "bltu",  // 18 - 22
    "bgeu"                                    // 23
  ).map(
    x => Cat(io.funct3, io.opcode) -> x
  ) ++ Seq(
    "slli", "srli", "srai", "add", "sub",  // 24 - 28
    "sll",  "slt",  "sltu", "xor", "srl",  // 29 - 33
    "sra",  "or",   "and"                  // 34 - 36
  ).map(
    x => Cat(io.funct7_imm7, io.funct3, io.opcode) -> x
  )).map(
    x => x._1 === ("b" + instID(x._2).reduce(
      (y, z) => y + z
    )).U
  ))


  /********************
   * Interconnections *
   ********************/

  // Decoder
  for (i <- 0 until io.ctrl.decoder.immSel.length) {
    io.ctrl.decoder.immSel(i) := opcode(i + 2)
  }

  // Register File
  io.ctrl.regFile.write := Seq(0, 1, 4, 5).map(
    x => opcode(x)
  ).reduce(
    (y, z) => y || z
  )

  // ALU
  Seq(
    Seq(28),
    Seq(24, 29),
    Seq(10, 30),
    Seq(11, 31),
    Seq(12, 32),
    Seq(25, 33),
    Seq(26, 34),
    Seq(13, 35),
    Seq(14, 36),
    Seq(0),
    (1 to 3).toSeq,
    Seq(1)
  ).zipWithIndex.foreach(
    x => io.ctrl.alu.opSel(x._2) := x._1.map(
      y => inst(y)
    ).reduce(
      (y, z) => y || z
    )
  )
  io.ctrl.alu.immSel := Seq(1, 2, 4).map(
    x => opcode(x)
  ).reduce(
    (y, z) => y || z
  )

  // Data Memory
  for (i <- 0 until io.ctrl.dMem.load.length) {
    io.ctrl.dMem.load(i) := inst(i + 4)
  }
  for (i <- 0 until io.ctrl.dMem.store.length) {
    io.ctrl.dMem.store(i) := inst(i + 15)
  }
}
