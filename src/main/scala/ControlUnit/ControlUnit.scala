package ControlUnit

import chisel3._
import chisel3.util._

class ControlUnit_IO extends Bundle
{
    // Input pins
    val opcode   : UInt = Input(UInt(7.W))
    val func3    : UInt = Input(UInt(3.W))
    val func7    : UInt = Input(UInt(7.W))
    val i_s_b_imm: SInt = Input(SInt(12.W))

    // Output pins
    val wr_en                  : Bool = Output(Bool())
    val imm_en                 : Bool = Output(Bool())
    val str_en                 : Bool = Output(Bool())
    val ld_en                  : Bool = Output(Bool())
    val br_en                  : Bool = Output(Bool())
    val jal_en                 : Bool = Output(Bool())
    val jalr_en                : Bool = Output(Bool())
    val auipc_en               : Bool = Output(Bool())
    val lui_en                 : Bool = Output(Bool())
    val addition_en            : Bool = Output(Bool())
    val shiftLeftLogical_en    : Bool = Output(Bool())
    val lessThan_en            : Bool = Output(Bool())
    val lessThanU_en           : Bool = Output(Bool())
    val XOR_en                 : Bool = Output(Bool())
    val shiftRightLogical_en   : Bool = Output(Bool())
    val shiftRightArithmetic_en: Bool = Output(Bool())
    val OR_en                  : Bool = Output(Bool())
    val AND_en                 : Bool = Output(Bool())
    val subtraction_en         : Bool = Output(Bool())
    val equal_en               : Bool = Output(Bool())
    val notEqual_en            : Bool = Output(Bool())
    val greaterThanEqual_en    : Bool = Output(Bool())
    val greaterThanEqualU_en   : Bool = Output(Bool())
    val jalrAddition_en        : Bool = Output(Bool())
    val auipcAddition_en       : Bool = Output(Bool())
    val luiAddition_en         : Bool = Output(Bool())
    val jalAddition_en         : Bool = Output(Bool())
    val sb_en                  : Bool = Output(Bool())
    val sh_en                  : Bool = Output(Bool())
    val sw_en                  : Bool = Output(Bool())
    val lb_en                  : Bool = Output(Bool())
    val lh_en                  : Bool = Output(Bool())
    val lw_en                  : Bool = Output(Bool())
    val lbu_en                 : Bool = Output(Bool())
    val lhu_en                 : Bool = Output(Bool())
}
class ControlUnit extends Module
{
    // Initializing IO pins
    val io: ControlUnit_IO = IO(new ControlUnit_IO())

    // Input wires
    val opcode   : UInt = dontTouch(WireInit(io.opcode))
    val func3    : UInt = dontTouch(WireInit(io.func3))
    val func7    : UInt = dontTouch(WireInit(io.func7))
    val i_s_b_imm: SInt = dontTouch(WireInit(io.i_s_b_imm))

    // Intermediate wires
    val func7_func3_opcode_id: UInt = dontTouch(WireInit(Cat(func7, func3, opcode)))
    val func3_opcode_id      : UInt = dontTouch(WireInit(Cat(func3, opcode)))
    val imm_func3_opcode_id  : UInt = dontTouch(WireInit(Cat(i_s_b_imm(11, 5), func3, opcode)))

    // Output wires
    val wr_en                  : Bool = dontTouch(WireInit(
//              R          lb, lh, lw, bu, lhu            I                jalr               auipc               lui                jal
        opcode === 51.U ||    opcode === 3.U   || opcode === 19.U || opcode === 103.U || opcode === 23.U || opcode === 55.U || opcode === 111.U
    ))
    val str_en                 : Bool = dontTouch(WireInit(opcode === 35.U))   // sb, sh, sw, sbu, shu
    val ld_en                  : Bool = dontTouch(WireInit(opcode === 3.U))    // lb, lh, lw, lbu, lhu
    val br_en                  : Bool = dontTouch(WireInit(opcode === 99.U))   // beq, bne, blt, bge, bltu, bgeu
    val jal_en                 : Bool = dontTouch(WireInit(opcode === 111.U))  // jal
    val jalr_en                : Bool = dontTouch(WireInit(opcode === 103.U))  // jalr
    val auipc_en               : Bool = dontTouch(WireInit(opcode === 23.U))   // auipc
    val lui_en                 : Bool = dontTouch(WireInit(opcode === 55.U))   // lui
    val imm_en                 : Bool = dontTouch(WireInit(
//              I          sb, sh, sw, sbu, shu    lb, lh, lw, bu, lhu          jalr
        opcode === 19.U ||    opcode === 35.U   ||    opcode === 3.U   || opcode === 103.U
    ))
    val addition_en            : Bool = dontTouch(WireInit(
//   lb, lh, lw, lbu, lhu                addi            sb, sh, sw, sbu, shu                   add
        opcode === 3.U    || func3_opcode_id === 19.U ||    opcode === 35.U   || func7_func3_opcode_id === 51.U
    ))
    val shiftLeftLogical_en    : Bool = dontTouch(WireInit(
//                    slli                              sll
        imm_func3_opcode_id === 147.U || func7_func3_opcode_id === 179.U
    ))
    val lessThan_en            : Bool = dontTouch(WireInit(
//                  slti                           slt                              blt
        func3_opcode_id === 275.U || func7_func3_opcode_id === 307.U || func3_opcode_id === 611.U
    ))
    val lessThanU_en           : Bool = dontTouch(WireInit(
//                  sltiu                         sltu                              bltu
        func3_opcode_id === 403.U || func7_func3_opcode_id === 435.U || func3_opcode_id === 867.U
    ))
    val XOR_en                 : Bool = dontTouch(WireInit(
//                 xori                             xor
        func3_opcode_id === 531.U || func7_func3_opcode_id === 563.U
    ))
    val shiftRightLogical_en   : Bool = dontTouch(WireInit(
//                    srli                               srl
        imm_func3_opcode_id === 659.U || func7_func3_opcode_id === 691.U
    ))
    val shiftRightArithmetic_en: Bool = dontTouch(WireInit(
//                     srai                                sra
        imm_func3_opcode_id === 33427.U || func7_func3_opcode_id === 33459.U
    ))
    val OR_en                  : Bool = dontTouch(WireInit(
//                   ori                              or
        func3_opcode_id === 787.U || func7_func3_opcode_id === 819.U
    ))
    val AND_en                 : Bool = dontTouch(WireInit(
//                   andi                          and
        func3_opcode_id === 915.U || func7_func3_opcode_id === 947.U
    ))
    val subtraction_en         : Bool = dontTouch(WireInit(func7_func3_opcode_id === 32819.U))  // sub
    val equal_en               : Bool = dontTouch(WireInit(func3_opcode_id === 99.U))  // beq
    val notEqual_en            : Bool = dontTouch(WireInit(func3_opcode_id === 227.U))  // bne
    val greaterThanEqual_en    : Bool = dontTouch(WireInit(func3_opcode_id === 739.U))  // bge
    val greaterThanEqualU_en   : Bool = dontTouch(WireInit(func3_opcode_id === 995.U))  // bgeu
    val jalrAddition_en        : Bool = dontTouch(WireInit(func3_opcode_id === 103.U))  // jalr
    val auipcAddition_en       : Bool = dontTouch(WireInit(opcode === 23.U))  // auipc
    val luiAddition_en         : Bool = dontTouch(WireInit(opcode === 55.U))  // lui
    val jalAddition_en         : Bool = dontTouch(WireInit(opcode === 111.U))  // jal
    val sb_en                  : Bool = dontTouch(WireInit(func3 === "b000".U))  // sb
    val sh_en                  : Bool = dontTouch(WireInit(func3 === "b001".U))  // sh
    val sw_en                  : Bool = dontTouch(WireInit(func3 === "b010".U))  // sw
    val lb_en                  : Bool = dontTouch(WireInit(func3 === "b000".U))  // lb
    val lh_en                  : Bool = dontTouch(WireInit(func3 === "b001".U))  // lh
    val lw_en                  : Bool = dontTouch(WireInit(func3 === "b010".U))  // lw
    val lbu_en                 : Bool = dontTouch(WireInit(func3 === "b100".U))  // lbu
    val lhu_en                 : Bool = dontTouch(WireInit(func3 === "b101".U))  // lhu

    // Wiring to outpin pins
    Array(
        io.wr_en,                   io.imm_en,              io.str_en,               io.ld_en,           io.br_en,
        io.jal_en,                  io.jalr_en,             io.auipc_en,             io.lui_en,          io.addition_en,
        io.shiftLeftLogical_en,     io.lessThan_en,         io.lessThanU_en,         io.XOR_en,          io.shiftRightLogical_en,
        io.shiftRightArithmetic_en, io.OR_en,               io.AND_en,               io.subtraction_en,  io.equal_en,
        io.notEqual_en,             io.greaterThanEqual_en, io.greaterThanEqualU_en, io.jalrAddition_en, io.auipcAddition_en,
        io.luiAddition_en,          io.jalAddition_en,      io.sb_en,                io.sh_en,           io.sw_en,
        io.lb_en,                   io.lh_en,               io.lw_en,                io.lbu_en,          io.lhu_en
    ) zip Array(
        wr_en,                      imm_en,                 str_en,                  ld_en,              br_en,
        jal_en,                     jalr_en,                auipc_en,                lui_en,             addition_en,
        shiftLeftLogical_en,        lessThan_en,            lessThanU_en,            XOR_en,             shiftRightLogical_en,
        shiftRightArithmetic_en,    OR_en,                  AND_en,                  subtraction_en,     equal_en,
        notEqual_en,                greaterThanEqual_en,    greaterThanEqualU_en,    jalrAddition_en,    auipcAddition_en,
        luiAddition_en,             jalAddition_en,         sb_en,                   sh_en,              sw_en,
        lb_en,                      lh_en,                  lw_en,                   lbu_en,             lhu_en
    ) foreach
    {
        x => x._1 := x._2
    }
}
