package xodus.core.decode_stage

import chisel3._,
       chisel3.util._
import xodus.configs.Configs,
       xodus.isa.I


class DecoderCtrl extends Bundle {
  val imm_gen_sel: UInt = Output(UInt(3.W))
}


class RegFileCtrl extends Bundle {
  val int_write: Bool = Output(Bool())
}


class JumpUnitCtrl extends Bundle {
  val en    : Bool = Output(Bool())
  val op_sel: UInt = Output(UInt(4.W))
}


class ALUCtrl extends Bundle {
  val imm_sel: Bool = Output(Bool())
  val op_sel : UInt = Output(UInt(4.W))
}


class DMemCtrl extends Bundle {
  val op_sel: UInt = Output(UInt(4.W))
}


class Controls extends Bundle {
  val decoder  : DecoderCtrl  = new DecoderCtrl
  val reg_file : RegFileCtrl  = new RegFileCtrl
  val jump_unit: JumpUnitCtrl = new JumpUnitCtrl
  val alu      : ALUCtrl      = new ALUCtrl
  val dmem     : DMemCtrl     = new DMemCtrl
}


class ControlUnitIO extends Bundle with Configs {
  val opcode: UInt = Input(UInt(OPCODE_WIDTH.W))
  val funct3: UInt = Input(UInt(FUNCT3_WIDTH.W))
  val funct7: UInt = Input(UInt(FUNCT7_WIDTH.W))

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
    io.ctrl.jump_unit.en       -> Seq(1, 5, 8),
    io.ctrl.alu.imm_sel        -> ((2 to 5) ++ (7 to 9))
  ).foreach(
    x => x._1 := x._2.map(
      opcode_sel === _.U
    ).reduce(_ || _)
  )

  Seq(
    io.ctrl.jump_unit.op_sel -> Seq(
      Seq(1 to 7),  // jalr, branch
      Seq(Seq(9))        // jal
    ),
    io.ctrl.alu.op_sel -> Seq(
      Seq(
        (8 to 16) ++ Seq(25), // signed addition
        Seq(17, 28),          // signed less than
        Seq(18, 29),          // unsigned less than
        Seq(19, 30),          // xor
        Seq(20, 33),          // or
        Seq(21, 34),          // and
        Seq(22, 27),          // shift left logical
        Seq(23, 31),          // shift right logical
        Seq(24, 32),          // shift right arithmetic
        Seq(26)               // subtraction
      ),
      Seq(
        Seq(7),   // lui
        Seq(8, 9) // unsigned addition
      )
    )
  ).foreach(
    x => x._1 := MuxCase(0.U, (x._2.head.map((inst_sel, _)) ++ x._2(1).map((opcode_sel, _))).zipWithIndex.map(
      y => y._1._2.map(y._1._1 === _.U).reduce(_ || _) -> (y._2 + 1).U
    ))
  )

  io.ctrl.dmem.op_sel := MuxCase(0.U, (8 to 15).zipWithIndex.map(
    x => (inst_sel === x._1.U) -> (x._2 + 1).U
  ))
}