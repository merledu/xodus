package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.isa.I


class DecoderCtrl extends Bundle {
  val imm_gen_sel: UInt = Output(UInt(3.W))
}


class RegFileCtrl extends Bundle {
  val int_write: Bool = Output(Bool())
}


class ALUCtrl extends Bundle {
  val imm_sel: Bool = Output(Bool())
  val op_sel : UInt = Output(UInt(1.W))
}


class Controls extends Bundle {
  val decoder : DecoderCtrl = new DecoderCtrl
  val reg_file: RegFileCtrl = new RegFileCtrl
  val alu     : ALUCtrl     = new ALUCtrl
}


class ControlUnitIO extends Bundle {
  val opcode: UInt = Flipped(new DecoderIO().opcode)
  val funct3: UInt = Flipped(new DecoderIO().funct3)
  val funct7: UInt = Flipped(new DecoderIO().funct7)

  val ctrl: Controls = new Controls
}


class ControlUnit extends RawModule {
  val io: ControlUnitIO = IO(new ControlUnitIO())

  val opcodes: Seq[String] = new I().opcodes
  val insts  : Seq[String] = new I().insts

  // Selection Wires
  val opcode_sel: UInt = MuxCase(0.U, opcodes.indices.map(  // opcode_sel(x) = opcodes(x) + 1
    x => (io.opcode === ("b" + opcodes(x)).U) -> (x + 1).U
  ))

  val inst_sel: UInt = MuxCase(0.U, ((0 to 20).map(  // inst_sel(x) = insts(x) + 1
    (Cat(io.funct3, io.opcode), _)
  ) ++ (21 to 33).map(
    (Cat(io.funct7, io.funct3, io.opcode), _)
  )).map(
    x => (x._1 === ("b" + insts(x._2)).U) -> (x._2 + 1).U
  ))


   /*** Interconnections ***/

  io.ctrl.decoder.imm_gen_sel := MuxCase(0.U, Seq(
    1 to 3,  // I-Type = 1
    Seq(4),  // S-Type = 2
    Seq(5),  // B-Type = 3
    6 to 7,  // U-Type = 4
    Seq(8)   // J-Type = 5
  ).zipWithIndex.map(
    x => x._1.map(
      y => io.opcode === ("b" + opcodes(y)).U
    ).reduce(_ || _) -> (x._2 + 1).U
  ))

  Seq(
    io.ctrl.reg_file.int_write -> ((1 to 4) ++ (7 to 9)),
    io.ctrl.alu.imm_sel        -> ((2 to 5) ++ (7 to 9))
  ).foreach(
    x => x._1 := x._2.map(
      opcode_sel === _.U
    ).reduce(_ || _)
  )

  io.ctrl.alu.op_sel := MuxCase(0.U, (Seq(
    Seq(16, 25),  // signed addition
    Seq(17, 28),  // signed less than
    Seq(18, 29),  // unsigned less than
    Seq(19, 30),  // xor
    Seq(20, 33),  // or
    Seq(21, 34),  // and
    Seq(22, 27),  // shift left logical
    Seq(23, 31),  // shift right logical
    Seq(24, 32),  // shift right arithmetic
    Seq(26),      // subtraction
    Seq(1)        // jalr
  ).map((inst_sel, _)) ++ Seq(
    Seq(7),    // lui
    Seq(8, 9)  // unsigned addition
  ).map((opcode_sel, _))).zipWithIndex.map(
    x => x._1._2.map(
      x._1._1 === _.U
    ).reduce(_ || _) -> (x._2 + 1).U
  ))
}
//class DMemCtrl extends Bundle {
//  val load : Bool = Output(Bool())
//  val store: Bool = Output(Bool())
//}
//
//
//class Controls extends Bundle {
//  val pc         : PCCtrl          = new PCCtrl
//  val regFD      : RegFDCtrl       = new RegFDCtrl
//  val decoder    : DecoderCtrl     = new DecoderCtrl
//  val regFile    : RegFileCtrl     = new RegFileCtrl
//  val regDE      : RegDECtrl       = new RegDECtrl
//  val alu        : ALUCtrl         = new ALUCtrl
//  val dMemAligner: DMemAlignerCtrl = new DMemAlignerCtrl
//  val dMem       : DMemCtrl        = new DMemCtrl
//}
//
//
//class ControlUnitIO extends Bundle with Configs {
//  val opcode    : UInt      = Flipped(new DecoderIO().opcode)
//  val funct3    : UInt      = Flipped(new DecoderIO().funct3)
//  val funct7    : UInt      = Flipped(new DecoderIO().funct7)
//  val dMemOffset: UInt      = Flipped(new DMemAlignerIO().offset)
//  val dMemAlign : Vec[Bool] = Flipped(new DMemAlignerIO().align)
//
//  val ctrl: Controls = new Controls
//}
//
//
//class ControlUnit extends RawModule {
//  val io: ControlUnitIO = IO(new ControlUnitIO)
//
//  val opcodes: Map[String, Seq[String]] = new ISA().opcodes
//  val instID : Map[String, Seq[String]] = new ISA().instID
//
//  // Control wires
//  val opcode: Vec[Bool] = VecInit(Seq(
//    "R", "I", "S", "B", "U", "J"
//  ).map(opcodes(_).map(
//    y => io.opcode === ("b" + y).U
//  ).reduce(_ || _)))
//
//  val inst: Vec[Bool] = VecInit((Seq(
//    "lui", "auipc", "jal"  // 0 - 2
//  ).map(io.opcode -> _) ++ Seq(
//    "jalr", "lb",   "lh",   "lw",    "lbu",   // 3 - 7
//    "lhu",  "addi", "slti", "sltiu", "xori",  // 8 - 12
//    "ori",  "andi", "sb",   "sh",    "sw",    // 13 - 17
//    "beq",  "bne",  "blt",  "bge",   "bltu",  // 18 - 22
//    "bgeu"                                    // 23
//  ).map(Cat(io.funct3, io.opcode) -> _) ++ Seq(
//    "slli", "srli", "srai", "add", "sub",  // 24 - 28
//    "sll",  "slt",  "sltu", "xor", "srl",  // 29 - 33
//    "sra",  "or",   "and"                  // 34 - 36
//  ).map(Cat(io.funct7, io.funct3, io.opcode) -> _)).map(
//    x => x._1 === ("b" + instID(x._2).reduce(_ + _)).U
//  ))
//
//  // Decoder
//  val decoderImmSel: Vec[Bool] = VecInit((0 until io.ctrl.decoder.immSel.length).toSeq.map(
//    x => opcode(x + 2)
//  ))
//
//  // Register File
//  val regFileIntWrite: Bool = Seq(0, 1, 4, 5).map(opcode(_)).reduce(_ || _)
//
//  // ALU
//  val aluOpSel: Vec[Bool] = VecInit(Seq(
//    Seq(28),         // subtraction
//    Seq(24, 29),     // shift left logical
//    Seq(10, 30),     // signed less than
//    Seq(11, 31),     // unsigned less than
//    Seq(12, 32),     // xor
//    Seq(25, 33),     // shift right logical
//    Seq(26, 34),     // shift right arithmetic
//    Seq(13, 35),     // or
//    Seq(14, 36),     // and
//    Seq(0),          // lui
//    (1 to 3).toSeq,  // unsigned addition
//    Seq(1)           // auipc
//  ).map(_.map(inst(_)).reduce(_ || _)))
//
//  val aluImmSel: Bool = Seq(1, 2, 4).map(opcode(_)).reduce(_ || _)
//
//  // Data Memory Aligner
//  val dMemAlignerLoad: Vec[Bool] = VecInit((0 until io.ctrl.dMemAligner.load.length).toSeq.map(
//    x => inst(x + 4)
//  ))
//
//  val dMemAlignerStore: Vec[Bool] = VecInit((0 until io.ctrl.dMemAligner.store.length).toSeq.map(
//    x => inst(x + 15)
//  ))
//
//  val dMemAlign: Bool = MuxCase(0.B, Seq(
//    io.dMemAlign(1) -> (io.dMemOffset === 3.U),
//    io.dMemAlign(2) -> (1 to 3).toSeq.map(io.dMemOffset === _.U).reduce(_ || _)
//  ))
//
//  // Data Memory
//  val dMem: Vec[Bool] = VecInit(Seq(
//    4 to 8,   // load
//    15 to 17  // store
//  ).map(_.toSeq.map(inst(_)).reduce(_ || _)))
//
//  // TODO: Remove this wire if not necessary
//  // Stall
//  val stall: Bool = WireInit(0.B)
//
//
//  /********************
//   * Interconnections *
//   ********************/
//
//  // Vec[Bool]
//  Seq(
//    io.ctrl.decoder.immSel    -> decoderImmSel,
//    io.ctrl.alu.opSel         -> aluOpSel,
//    io.ctrl.dMemAligner.load  -> dMemAlignerLoad,
//    io.ctrl.dMemAligner.store -> dMemAlignerStore,
//  ).foreach(
//    x => x._1.zipWithIndex.map(
//      y => y._1 := x._2(y._2) && !stall
//    )
//  )
//
//  // Bool
//  (Seq(
//    io.ctrl.regFile.intWrite -> regFileIntWrite,
//    io.ctrl.alu.immSel       -> aluImmSel
//  ) ++ Seq(
//    io.ctrl.dMem.load,
//    io.ctrl.dMem.store
//  ).zipWithIndex.map(
//    x => x._1 -> dMem(x._2)
//  )).foreach(
//    x => x._1 := x._2 && !stall
//  )
//
//  // Stalls
//  Seq(
//    io.ctrl.dMemAligner.align,
//    io.ctrl.pc.stall,
//    io.ctrl.regFD.stall,
//    io.ctrl.regDE.stall
//  ).foreach(_ := !io.dMemAlign(0) && dMemAlign)
//}
