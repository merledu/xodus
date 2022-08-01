package PipelineRegs

import chisel3._

class RegDA_IO extends Bundle
{
    // Input pins
    val PC_in                     : UInt = Input(UInt(32.W))
    val opcode_in                 : UInt = Input(UInt(7.W))
    val rd_addr_in                : UInt = Input(UInt(5.W))
    val func3_in                  : UInt = Input(UInt(3.W))
    val rs1_data_in               : SInt = Input(SInt(32.W))
    val rs2_data_in               : SInt = Input(SInt(32.W))
    val func7_in                  : UInt = Input(UInt(7.W))
    val i_s_b_imm_in              : SInt = Input(SInt(12.W))
    val u_j_imm_in                : SInt = Input(SInt(20.W))
    val wr_en_in                  : Bool = Input(Bool())
    val imm_en_in                 : Bool = Input(Bool())
    val str_en_in                 : Bool = Input(Bool())
    val ld_en_in                  : Bool = Input(Bool())
    val br_en_in                  : Bool = Input(Bool())
    val jal_en_in                 : Bool = Input(Bool())
    val jalr_en_in                : Bool = Input(Bool())
    val auipc_en_in               : Bool = Input(Bool())
    val lui_en_in                 : Bool = Input(Bool())
    val addition_en_in            : Bool = Input(Bool())
    val shiftLeftLogical_en_in    : Bool = Input(Bool())
    val lessThan_en_in            : Bool = Input(Bool())
    val lessThanU_en_in           : Bool = Input(Bool())
    val XOR_en_in                 : Bool = Input(Bool())
    val shiftRightLogical_en_in   : Bool = Input(Bool())
    val shiftRightArithmetic_en_in: Bool = Input(Bool())
    val OR_en_in                  : Bool = Input(Bool())
    val AND_en_in                 : Bool = Input(Bool())
    val subtraction_en_in         : Bool = Input(Bool())
    val equal_en_in               : Bool = Input(Bool())
    val notEqual_en_in            : Bool = Input(Bool())
    val greaterThanEqual_en_in    : Bool = Input(Bool())
    val greaterThanEqualU_en_in   : Bool = Input(Bool())
    val jalrAddition_en_in        : Bool = Input(Bool())
    val auipcAddition_en_in       : Bool = Input(Bool())
    val luiAddition_en_in         : Bool = Input(Bool())
    val jalAddition_en_in         : Bool = Input(Bool())
    val sb_en_in                  : Bool = Input(Bool())
    val sh_en_in                  : Bool = Input(Bool())
    val sw_en_in                  : Bool = Input(Bool())
    val lb_en_in                  : Bool = Input(Bool())
    val lh_en_in                  : Bool = Input(Bool())
    val lw_en_in                  : Bool = Input(Bool())
    val lbu_en_in                 : Bool = Input(Bool())
    val lhu_en_in                 : Bool = Input(Bool())

    // Output pins
    val PC_out                     : UInt = Output(UInt(32.W))
    val opcode_out                 : UInt = Output(UInt(7.W))
    val rd_addr_out                : UInt = Output(UInt(5.W))
    val func3_out                  : UInt = Output(UInt(3.W))
    val rs1_data_out               : SInt = Output(SInt(32.W))
    val rs2_data_out               : SInt = Output(SInt(32.W))
    val func7_out                  : UInt = Output(UInt(7.W))
    val i_s_b_imm_out              : SInt = Output(SInt(12.W))
    val u_j_imm_out                : SInt = Output(SInt(20.W))
    val wr_en_out                  : Bool = Output(Bool())
    val imm_en_out                 : Bool = Output(Bool())
    val str_en_out                 : Bool = Output(Bool())
    val ld_en_out                  : Bool = Output(Bool())
    val br_en_out                  : Bool = Output(Bool())
    val jal_en_out                 : Bool = Output(Bool())
    val jalr_en_out                : Bool = Output(Bool())
    val auipc_en_out               : Bool = Output(Bool())
    val lui_en_out                 : Bool = Output(Bool())
    val addition_en_out            : Bool = Output(Bool())
    val shiftLeftLogical_en_out    : Bool = Output(Bool())
    val lessThan_en_out            : Bool = Output(Bool())
    val lessThanU_en_out           : Bool = Output(Bool())
    val XOR_en_out                 : Bool = Output(Bool())
    val shiftRightLogical_en_out   : Bool = Output(Bool())
    val shiftRightArithmetic_en_out: Bool = Output(Bool())
    val OR_en_out                  : Bool = Output(Bool())
    val AND_en_out                 : Bool = Output(Bool())
    val subtraction_en_out         : Bool = Output(Bool())
    val equal_en_out               : Bool = Output(Bool())
    val notEqual_en_out            : Bool = Output(Bool())
    val greaterThanEqual_en_out    : Bool = Output(Bool())
    val greaterThanEqualU_en_out   : Bool = Output(Bool())
    val jalrAddition_en_out        : Bool = Output(Bool())
    val auipcAddition_en_out       : Bool = Output(Bool())
    val luiAddition_en_out         : Bool = Output(Bool())
    val jalAddition_en_out         : Bool = Output(Bool())
    val sb_en_out                  : Bool = Output(Bool())
    val sh_en_out                  : Bool = Output(Bool())
    val sw_en_out                  : Bool = Output(Bool())
    val lb_en_out                  : Bool = Output(Bool())
    val lh_en_out                  : Bool = Output(Bool())
    val lw_en_out                  : Bool = Output(Bool())
    val lbu_en_out                 : Bool = Output(Bool())
    val lhu_en_out                 : Bool = Output(Bool())
}
class RegDA extends Module
{
    // Initializing IO pins
    val io: RegDA_IO = IO(new RegDA_IO())

    // Input wires
    val PC_in                     : UInt = dontTouch(WireInit(io.PC_in))
    val opcode_in                 : UInt = dontTouch(WireInit(io.opcode_in))
    val rd_addr_in                : UInt = dontTouch(WireInit(io.rd_addr_in))
    val func3_in                  : UInt = dontTouch(WireInit(io.func3_in))
    val rs1_data_in               : SInt = dontTouch(WireInit(io.rs1_data_in))
    val rs2_data_in               : SInt = dontTouch(WireInit(io.rs2_data_in))
    val func7_in                  : UInt = dontTouch(WireInit(io.func7_in))
    val i_s_b_imm_in              : SInt = dontTouch(WireInit(io.i_s_b_imm_in))
    val u_j_imm_in                : SInt = dontTouch(WireInit(io.u_j_imm_in))
    val wr_en_in                  : Bool = dontTouch(WireInit(io.wr_en_in))
    val imm_en_in                 : Bool = dontTouch(WireInit(io.imm_en_in))
    val str_en_in                 : Bool = dontTouch(WireInit(io.str_en_in))
    val ld_en_in                  : Bool = dontTouch(WireInit(io.ld_en_in))
    val br_en_in                  : Bool = dontTouch(WireInit(io.br_en_in))
    val jal_en_in                 : Bool = dontTouch(WireInit(io.jal_en_in))
    val jalr_en_in                : Bool = dontTouch(WireInit(io.jalr_en_in))
    val auipc_en_in               : Bool = dontTouch(WireInit(io.auipc_en_in))
    val lui_en_in                 : Bool = dontTouch(WireInit(io.lui_en_in))
    val addition_en_in            : Bool = dontTouch(WireInit(io.addition_en_in))
    val shiftLeftLogical_en_in    : Bool = dontTouch(WireInit(io.shiftLeftLogical_en_in))
    val lessThan_en_in            : Bool = dontTouch(WireInit(io.lessThan_en_in))
    val lessThanU_en_in           : Bool = dontTouch(WireInit(io.lessThanU_en_in))
    val XOR_en_in                 : Bool = dontTouch(WireInit(io.XOR_en_in))
    val shiftRightLogical_en_in   : Bool = dontTouch(WireInit(io.shiftRightLogical_en_in))
    val shiftRightArithmetic_en_in: Bool = dontTouch(WireInit(io.shiftRightArithmetic_en_in))
    val OR_en_in                  : Bool = dontTouch(WireInit(io.OR_en_in))
    val AND_en_in                 : Bool = dontTouch(WireInit(io.AND_en_in))
    val subtraction_en_in         : Bool = dontTouch(WireInit(io.subtraction_en_in))
    val equal_en_in               : Bool = dontTouch(WireInit(io.equal_en_in))
    val notEqual_en_in            : Bool = dontTouch(WireInit(io.notEqual_en_in))
    val greaterThanEqual_en_in    : Bool = dontTouch(WireInit(io.greaterThanEqual_en_in))
    val greaterThanEqualU_en_in   : Bool = dontTouch(WireInit(io.greaterThanEqualU_en_in))
    val jalrAddition_en_in        : Bool = dontTouch(WireInit(io.jalrAddition_en_in))
    val auipcAddition_en_in       : Bool = dontTouch(WireInit(io.auipcAddition_en_in))
    val luiAddition_en_in         : Bool = dontTouch(WireInit(io.luiAddition_en_in))
    val jalAddition_en_in         : Bool = dontTouch(WireInit(io.jalAddition_en_in))
    val sb_en_in                  : Bool = dontTouch(WireInit(io.sb_en_in))
    val sh_en_in                  : Bool = dontTouch(WireInit(io.sh_en_in))
    val sw_en_in                  : Bool = dontTouch(WireInit(io.sw_en_in))
    val lb_en_in                  : Bool = dontTouch(WireInit(io.lb_en_in))
    val lh_en_in                  : Bool = dontTouch(WireInit(io.lh_en_in))
    val lw_en_in                  : Bool = dontTouch(WireInit(io.lw_en_in))
    val lbu_en_in                 : Bool = dontTouch(WireInit(io.lbu_en_in))
    val lhu_en_in                 : Bool = dontTouch(WireInit(io.lhu_en_in))

    // Initializing registers
    val PC                     : UInt = dontTouch(RegInit(0.U(32.W)))
    val opcode                 : UInt = dontTouch(RegInit(0.U(7.W)))
    val rd_addr                : UInt = dontTouch(RegInit(0.U(5.W)))
    val func3                  : UInt = dontTouch(RegInit(0.U(3.W)))
    val rs1_data               : SInt = dontTouch(RegInit(0.S(32.W)))
    val rs2_data               : SInt = dontTouch(RegInit(0.S(32.W)))
    val func7                  : UInt = dontTouch(RegInit(0.U(7.W)))
    val i_s_b_imm              : SInt = dontTouch(RegInit(0.S(12.W)))
    val u_j_imm                : SInt = dontTouch(RegInit(0.S(20.W)))
    val wr_en                  : Bool = dontTouch(RegInit(0.B))
    val imm_en                 : Bool = dontTouch(RegInit(0.B))
    val str_en                 : Bool = dontTouch(RegInit(0.B))
    val ld_en                  : Bool = dontTouch(RegInit(0.B))
    val br_en                  : Bool = dontTouch(RegInit(0.B))
    val jal_en                 : Bool = dontTouch(RegInit(0.B))
    val jalr_en                : Bool = dontTouch(RegInit(0.B))
    val auipc_en               : Bool = dontTouch(RegInit(0.B))
    val lui_en                 : Bool = dontTouch(RegInit(0.B))
    val addition_en            : Bool = dontTouch(RegInit(0.B))
    val shiftLeftLogical_en    : Bool = dontTouch(RegInit(0.B))
    val lessThan_en            : Bool = dontTouch(RegInit(0.B))
    val lessThanU_en           : Bool = dontTouch(RegInit(0.B))
    val XOR_en                 : Bool = dontTouch(RegInit(0.B))
    val shiftRightLogical_en   : Bool = dontTouch(RegInit(0.B))
    val shiftRightArithmetic_en: Bool = dontTouch(RegInit(0.B))
    val OR_en                  : Bool = dontTouch(RegInit(0.B))
    val AND_en                 : Bool = dontTouch(RegInit(0.B))
    val subtraction_en         : Bool = dontTouch(RegInit(0.B))
    val equal_en               : Bool = dontTouch(RegInit(0.B))
    val notEqual_en            : Bool = dontTouch(RegInit(0.B))
    val greaterThanEqual_en    : Bool = dontTouch(RegInit(0.B))
    val greaterThanEqualU_en   : Bool = dontTouch(RegInit(0.B))
    val jalrAddition_en        : Bool = dontTouch(RegInit(0.B))
    val auipcAddition_en       : Bool = dontTouch(RegInit(0.B))
    val luiAddition_en         : Bool = dontTouch(RegInit(0.B))
    val jalAddition_en         : Bool = dontTouch(RegInit(0.B))
    val sb_en                  : Bool = dontTouch(RegInit(0.B))
    val sh_en                  : Bool = dontTouch(RegInit(0.B))
    val sw_en                  : Bool = dontTouch(RegInit(0.B))
    val lb_en                  : Bool = dontTouch(RegInit(0.B))
    val lh_en                  : Bool = dontTouch(RegInit(0.B))
    val lw_en                  : Bool = dontTouch(RegInit(0.B))
    val lbu_en                 : Bool = dontTouch(RegInit(0.B))
    val lhu_en                 : Bool = dontTouch(RegInit(0.B))


    // Output wires
    val PC_out                     : UInt = dontTouch(WireInit(PC))
    val opcode_out                 : UInt = dontTouch(WireInit(opcode))
    val rd_addr_out                : UInt = dontTouch(WireInit(rd_addr))
    val func3_out                  : UInt = dontTouch(WireInit(func3))
    val rs1_data_out               : SInt = dontTouch(WireInit(rs1_data))
    val rs2_data_out               : SInt = dontTouch(WireInit(rs2_data))
    val func7_out                  : UInt = dontTouch(WireInit(func7))
    val i_s_b_imm_out              : SInt = dontTouch(WireInit(i_s_b_imm))
    val u_j_imm_out                : SInt = dontTouch(WireInit(u_j_imm))
    val wr_en_out                  : Bool = dontTouch(WireInit(wr_en))
    val imm_en_out                 : Bool = dontTouch(WireInit(imm_en))
    val str_en_out                 : Bool = dontTouch(WireInit(str_en))
    val ld_en_out                  : Bool = dontTouch(WireInit(ld_en))
    val br_en_out                  : Bool = dontTouch(WireInit(br_en))
    val jal_en_out                 : Bool = dontTouch(WireInit(jal_en))
    val jalr_en_out                : Bool = dontTouch(WireInit(jalr_en))
    val auipc_en_out               : Bool = dontTouch(WireInit(auipc_en))
    val lui_en_out                 : Bool = dontTouch(WireInit(lui_en))
    val addition_en_out            : Bool = dontTouch(WireInit(addition_en))
    val shiftLeftLogical_en_out    : Bool = dontTouch(WireInit(shiftLeftLogical_en))
    val lessThan_en_out            : Bool = dontTouch(WireInit(lessThan_en))
    val lessThanU_en_out           : Bool = dontTouch(WireInit(lessThanU_en))
    val XOR_en_out                 : Bool = dontTouch(WireInit(XOR_en))
    val shiftRightLogical_en_out   : Bool = dontTouch(WireInit(shiftRightLogical_en))
    val shiftRightArithmetic_en_out: Bool = dontTouch(WireInit(shiftRightArithmetic_en))
    val OR_en_out                  : Bool = dontTouch(WireInit(OR_en))
    val AND_en_out                 : Bool = dontTouch(WireInit(AND_en))
    val subtraction_en_out         : Bool = dontTouch(WireInit(subtraction_en))
    val equal_en_out               : Bool = dontTouch(WireInit(equal_en))
    val notEqual_en_out            : Bool = dontTouch(WireInit(notEqual_en))
    val greaterThanEqual_en_out    : Bool = dontTouch(WireInit(greaterThanEqual_en))
    val greaterThanEqualU_en_out   : Bool = dontTouch(WireInit(greaterThanEqualU_en))
    val jalrAddition_en_out        : Bool = dontTouch(WireInit(jalrAddition_en))
    val auipcAddition_en_out       : Bool = dontTouch(WireInit(auipcAddition_en))
    val luiAddition_en_out         : Bool = dontTouch(WireInit(luiAddition_en))
    val jalAddition_en_out         : Bool = dontTouch(WireInit(jalAddition_en))
    val sb_en_out                  : Bool = dontTouch(WireInit(sb_en))
    val sh_en_out                  : Bool = dontTouch(WireInit(sh_en))
    val sw_en_out                  : Bool = dontTouch(WireInit(sw_en))
    val lb_en_out                  : Bool = dontTouch(WireInit(lb_en))
    val lh_en_out                  : Bool = dontTouch(WireInit(lh_en))
    val lw_en_out                  : Bool = dontTouch(WireInit(lw_en))
    val lbu_en_out                 : Bool = dontTouch(WireInit(lbu_en))
    val lhu_en_out                 : Bool = dontTouch(WireInit(lhu_en))

    // Wiring to output pins
    Array(
        // Output pins
        io.PC_out,                  io.opcode_out,               io.rd_addr_out,         io.func3_out,                io.rs1_data_out,
        io.rs2_data_out,            io.func7_out,                io.i_s_b_imm_out,       io.u_j_imm_out,              io.wr_en_out,
        io.imm_en_out,              io.str_en_out,               io.ld_en_out,           io.br_en_out,                io.jal_en_out,
        io.jalr_en_out,             io.auipc_en_out,             io.lui_en_out,          io.addition_en_out,          io.shiftLeftLogical_en_out,
        io.lessThan_en_out,         io.lessThanU_en_out,         io.XOR_en_out,          io.shiftRightLogical_en_out, io.shiftRightArithmetic_en_out,
        io.OR_en_out,               io.AND_en_out,               io.subtraction_en_out,  io.equal_en_out,             io.notEqual_en_out,
        io.greaterThanEqual_en_out, io.greaterThanEqualU_en_out, io.jalrAddition_en_out, io.auipcAddition_en_out,     io.luiAddition_en_out,
        io.jalAddition_en_out,      io.sb_en_out,                io.sh_en_out,           io.sw_en_out,                io.lb_en_out,
        io.lh_en_out,               io.lw_en_out,                io.lbu_en_out,          io.lhu_en_out,

        // Registers
        PC,                         opcode,                      rd_addr,                func3,                       rs1_data,
        rs2_data,                   func7,                       i_s_b_imm,              u_j_imm,                     wr_en,
        imm_en,                     str_en,                      ld_en,                  br_en,                       jal_en,
        jalr_en,                    auipc_en,                    lui_en,                 addition_en,                 shiftLeftLogical_en,
        lessThan_en,                lessThanU_en,                XOR_en,                 shiftRightLogical_en,        shiftRightArithmetic_en,
        OR_en,                      AND_en,                      subtraction_en,         equal_en,                    notEqual_en,
        greaterThanEqual_en,        greaterThanEqualU_en,        jalrAddition_en,        auipcAddition_en,            luiAddition_en,
        jalAddition_en,             sb_en,                       sh_en,                  sw_en,                       lb_en,
        lh_en,                      lw_en,                       lbu_en,                 lhu_en
    ) zip Array(
        // Output pins
        PC_out,                     opcode_out,                  rd_addr_out,            func3_out,                   rs1_data_out,
        rs2_data_out,               func7_out,                   i_s_b_imm_out,          u_j_imm_out,                 wr_en_out,
        imm_en_out,                 str_en_out,                  ld_en_out,              br_en_out,                   jal_en_out,
        jalr_en_out,                auipc_en_out,                lui_en_out,             addition_en_out,             shiftLeftLogical_en_out,
        lessThan_en_out,            lessThanU_en_out,            XOR_en_out,             shiftRightLogical_en_out,    shiftRightArithmetic_en_out,
        OR_en_out,                  AND_en_out,                  subtraction_en_out,     equal_en_out,                notEqual_en_out,
        greaterThanEqual_en_out,    greaterThanEqualU_en_out,    jalrAddition_en_out,    auipcAddition_en_out,        luiAddition_en_out,
        jalAddition_en_out,         sb_en,                       sh_en,                  sw_en,                       lb_en,
        lh_en,                      lw_en,                       lbu_en,                 lhu_en,

        // Registers
        PC_in,                      opcode_in,                   rd_addr_in,             func3_in,                    rs1_data_in,
        rs2_data_in,                func7_in,                    i_s_b_imm_in,           u_j_imm_in,                  wr_en_in,
        imm_en_in,                  str_en_in,                   ld_en_in,               br_en_in,                    jal_en_in,
        jalr_en_in,                 auipc_en_in,                 lui_en_in,              addition_en_in,              shiftLeftLogical_en_in,
        lessThan_en_in,             lessThanU_en_in,             XOR_en_in,              shiftRightLogical_en_in,     shiftRightArithmetic_en_in,
        OR_en_in,                   AND_en_in,                   subtraction_en_in,      equal_en_in,                 notEqual_en_in,
        greaterThanEqual_en_in,     greaterThanEqualU_en_in,     jalrAddition_en_in,     auipc_en_in,                 luiAddition_en_in,
        jalAddition_en_in,          sb_en_in,                    sh_en_in,               sw_en_in,                    lb_en_in,
        lh_en_in,                   lw_en_in,                    lbu_en_in,              lhu_en_in
    ) foreach
    {
        x => x._1 := x._2
    }
}

