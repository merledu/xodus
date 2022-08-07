package ControlUnit

import chisel3._
import chisel3.util._

class ControlUnit_IO extends Bundle
{
    // Input pins
    val opcode      : UInt = Input(UInt(7.W))
    val func3       : UInt = Input(UInt(3.W))
    val func7       : UInt = Input(UInt(7.W))
    val imm         : SInt = Input(SInt(32.W))
    val r_id        : UInt = Input(UInt(7.W))
    val i_math_id   : UInt = Input(UInt(7.W))
    val i_load_id   : UInt = Input(UInt(7.W))
    val i_jalr_id   : UInt = Input(UInt(7.W))
    val s_id        : UInt = Input(UInt(7.W))
    val b_id        : UInt = Input(UInt(7.W))
    val u_auipc_id  : UInt = Input(UInt(7.W))
    val u_lui_id    : UInt = Input(UInt(7.W))
    val j_id        : UInt = Input(UInt(7.W))
    val stallControl: Bool = Input(Bool())

    // Output pins
    val wr_en                  : Bool = Output(Bool())
    val imm_en                 : Bool = Output(Bool())
    val str_en                 : Bool = Output(Bool())
    val load_en                : Bool = Output(Bool())
    val br_en                  : Bool = Output(Bool())
    val j_en                   : Bool = Output(Bool())
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
    val io          : ControlUnit_IO = IO(new ControlUnit_IO)
    val opcode      : UInt = dontTouch(WireInit(io.opcode))
    val func3       : UInt = dontTouch(WireInit(io.func3))
    val func7       : UInt = dontTouch(WireInit(io.func7))
    val imm         : SInt = dontTouch(WireInit(io.imm))
    val r_id        : UInt = dontTouch(WireInit(io.r_id))
    val i_math_id   : UInt = dontTouch(WireInit(io.i_math_id))
    val i_load_id   : UInt = dontTouch(WireInit(io.i_load_id))
    val i_jalr_id   : UInt = dontTouch(WireInit(io.i_jalr_id))
    val s_id        : UInt = dontTouch(WireInit(io.s_id))
    val b_id        : UInt = dontTouch(WireInit(io.b_id))
    val u_auipc_id  : UInt = dontTouch(WireInit(io.u_auipc_id))
    val u_lui_id    : UInt = dontTouch(WireInit(io.u_lui_id))
    val j_id        : UInt = dontTouch(WireInit(io.j_id))
    val stallControl: Bool = dontTouch(WireInit(io.stallControl))

    // Intermediate wires
    val func7_func3_opcode_id: UInt = dontTouch(WireInit(Cat(func7, func3, opcode)))
    val func3_opcode_id      : UInt = dontTouch(WireInit(Cat(func3, opcode)))
    val imm_func3_opcode_id  : UInt = dontTouch(WireInit(Cat(imm(11, 5), func3, opcode)))

    // Encoded ID wires
    // - R-Type IDs
    val add_id : UInt = dontTouch(WireInit(51.U(17.W)))
    val sub_id : UInt = dontTouch(WireInit(32819.U(17.W)))
    val sll_id : UInt = dontTouch(WireInit(179.U(17.W)))
    val slt_id : UInt = dontTouch(WireInit(307.U(17.W)))
    val sltu_id: UInt = dontTouch(WireInit(435.U(17.W)))
    val xor_id : UInt = dontTouch(WireInit(563.U(17.W)))
    val srl_id : UInt = dontTouch(WireInit(691.U(17.W)))
    val sra_id : UInt = dontTouch(WireInit(33459.U(17.W)))
    val or_id  : UInt = dontTouch(WireInit(819.U(17.W)))
    val and_id : UInt = dontTouch(WireInit(947.U(17.W)))
    
    // - I-Type IDs
    // -- Load IDs
    val lb_id : UInt = dontTouch(WireInit(3.U(10.W)))
    val lh_id : UInt = dontTouch(WireInit(131.U(10.W)))
    val lw_id : UInt = dontTouch(WireInit(259.U(10.W)))
    val lbu_id: UInt = dontTouch(WireInit(515.U(10.W)))
    val lhu_id: UInt = dontTouch(WireInit(643.U(10.W)))
    // -- Math IDs
    val addi_id : UInt = dontTouch(WireInit(19.U(10.W)))
    val slli_id : UInt = dontTouch(WireInit(147.U(17.W)))
    val slti_id : UInt = dontTouch(WireInit(275.U(10.W)))
    val sltiu_id: UInt = dontTouch(WireInit(403.U(10.W)))
    val xori_id : UInt = dontTouch(WireInit(531.U(10.W)))
    val srli_id : UInt = dontTouch(WireInit(659.U(17.W)))
    val srai_id : UInt = dontTouch(WireInit(33427.U(17.W)))
    val ori_id  : UInt = dontTouch(WireInit(787.U(10.W)))
    val andi_id : UInt = dontTouch(WireInit(915.U(10.W)))

    // - S-Type IDs
    val sb_id: UInt = dontTouch(WireInit(35.U(10.W)))
    val sh_id: UInt = dontTouch(WireInit(163.U(10.W)))
    val sw_id: UInt = dontTouch(WireInit(291.U(10.W)))

    // - B-Type IDs
    val beq_id : UInt = dontTouch(WireInit(99.U(10.W)))
    val bne_id : UInt = dontTouch(WireInit(227.U(10.W)))
    val blt_id : UInt = dontTouch(WireInit(611.U(10.W)))
    val bge_id : UInt = dontTouch(WireInit(739.U(10.W)))
    val bltu_id: UInt = dontTouch(WireInit(867.U(10.W)))
    val bgeu_id: UInt = dontTouch(WireInit(995.U(10.W)))
    
    // Data memory control
    val lb_en : Bool = dontTouch(WireInit(func3_opcode_id === lb_id))
    val lh_en : Bool = dontTouch(WireInit(func3_opcode_id === lh_id))
    val lw_en : Bool = dontTouch(WireInit(func3_opcode_id === lw_id))
    val lbu_en: Bool = dontTouch(WireInit(func3_opcode_id === lbu_id))
    val lhu_en: Bool = dontTouch(WireInit(func3_opcode_id === lhu_id))
    val sb_en : Bool = dontTouch(WireInit(func3_opcode_id === sb_id))
    val sh_en : Bool = dontTouch(WireInit(func3_opcode_id === sh_id))
    val sw_en : Bool = dontTouch(WireInit(func3_opcode_id === sw_id))
    val str_en: Bool = dontTouch(WireInit(opcode === s_id))
    val load_en : Bool = dontTouch(WireInit(opcode === i_load_id))

    // WriteBack control
    val br_en   : Bool = dontTouch(WireInit(opcode === b_id))
    val j_en  : Bool = dontTouch(WireInit(opcode === j_id))
    val jalr_en : Bool = dontTouch(WireInit(opcode === i_jalr_id))
    val auipc_en: Bool = dontTouch(WireInit(opcode === u_auipc_id))
    val lui_en  : Bool = dontTouch(WireInit(opcode === u_lui_id))

    // ALU control
    val imm_en                 : Bool = dontTouch(WireInit(
        opcode === i_math_id || str_en || load_en || jalr_en
    ))
    val addition_en            : Bool = dontTouch(WireInit(
        load_en || func3_opcode_id === addi_id || str_en || func7_func3_opcode_id === add_id
    ))
    val shiftLeftLogical_en    : Bool = dontTouch(WireInit(
        imm_func3_opcode_id === slli_id || func7_func3_opcode_id === sll_id
    ))
    val lessThan_en            : Bool = dontTouch(WireInit(
        func3_opcode_id === slti_id || func7_func3_opcode_id === slt_id || func3_opcode_id === blt_id
    ))
    val lessThanU_en           : Bool = dontTouch(WireInit(
        func3_opcode_id === sltiu_id || func7_func3_opcode_id === sltu_id || func3_opcode_id === bltu_id
    ))
    val XOR_en                 : Bool = dontTouch(WireInit(
        func3_opcode_id === xori_id || func7_func3_opcode_id === xor_id
    ))
    val shiftRightLogical_en   : Bool = dontTouch(WireInit(
        imm_func3_opcode_id === srli_id || func7_func3_opcode_id === srl_id
    ))
    val shiftRightArithmetic_en: Bool = dontTouch(WireInit(
        imm_func3_opcode_id === srai_id || func7_func3_opcode_id === sra_id
    ))
    val OR_en                  : Bool = dontTouch(WireInit(
        func3_opcode_id === ori_id || func7_func3_opcode_id === or_id
    ))
    val AND_en                 : Bool = dontTouch(WireInit(
        func3_opcode_id === andi_id || func7_func3_opcode_id === and_id
    ))
    val subtraction_en         : Bool = dontTouch(WireInit(func7_func3_opcode_id === sub_id))
    val equal_en               : Bool = dontTouch(WireInit(func3_opcode_id === beq_id))
    val notEqual_en            : Bool = dontTouch(WireInit(func3_opcode_id === bne_id))
    val greaterThanEqual_en    : Bool = dontTouch(WireInit(func3_opcode_id === bge_id))
    val greaterThanEqualU_en   : Bool = dontTouch(WireInit(func3_opcode_id === bgeu_id))
    val jalrAddition_en        : Bool = dontTouch(WireInit(func3_opcode_id === i_jalr_id))

    // RegFile control
    val wr_en: Bool = dontTouch(WireInit(
        opcode === r_id || load_en || opcode === i_math_id || jalr_en || auipc_en || lui_en || j_en
    ))

    // Wiring to outpin pins
    Array(
        io.wr_en,                   io.imm_en,              io.str_en,               io.load_en,         io.br_en,
        io.j_en,                    io.jalr_en,             io.auipc_en,             io.lui_en,          io.addition_en,
        io.shiftLeftLogical_en,     io.lessThan_en,         io.lessThanU_en,         io.XOR_en,          io.shiftRightLogical_en,
        io.shiftRightArithmetic_en, io.OR_en,               io.AND_en,               io.subtraction_en,  io.equal_en,
        io.notEqual_en,             io.greaterThanEqual_en, io.greaterThanEqualU_en, io.jalrAddition_en, io.sb_en,
        io.sh_en,                   io.sw_en,               io.lb_en,                io.lh_en,           io.lw_en,
        io.lbu_en,                  io.lhu_en
    ) zip Array(
        wr_en,                      imm_en,                 str_en,                  load_en,            br_en,
        j_en,                       jalr_en,                auipc_en,                lui_en,             addition_en,
        shiftLeftLogical_en,        lessThan_en,            lessThanU_en,            XOR_en,             shiftRightLogical_en,
        shiftRightArithmetic_en,    OR_en,                  AND_en,                  subtraction_en,     equal_en,
        notEqual_en,                greaterThanEqual_en,    greaterThanEqualU_en,    jalrAddition_en,    sb_en,
        sh_en,                      sw_en,                  lb_en,                   lh_en,              lw_en,
        lbu_en,                     lhu_en
    ) foreach
    {
        x => x._1 := Mux(stallControl, 0.B, x._2)
    }
}

