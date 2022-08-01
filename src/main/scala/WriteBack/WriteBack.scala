package WriteBack

import chisel3._
import chisel3.util._

class WriteBack_IO extends Bundle
{
    // Input pins
    val PC       : UInt = Input(UInt(32.W))
    val alu      : SInt = Input(SInt(32.W))
    val mem_data : SInt = Input(SInt(32.W))
    val i_s_b_imm: SInt = Input(SInt(12.W))
    val u_j_imm  : SInt = Input(SInt(20.W))
    val wr_en    : Bool = Input(Bool())
    val br_en    : Bool = Input(Bool())
    val jalr_en  : Bool = Input(Bool())
    val jal_en   : Bool = Input(Bool())
    val auipc_en : Bool = Input(Bool())
    val lui_en   : Bool = Input(Bool())

    // Output pins
    val nPC    : UInt = Output(UInt(32.W))
    val nPC_en : Bool = Output(Bool())
    val rd_data: SInt = Output(SInt(32.W))
}
class WriteBack extends Module
{
    // Initializing IO pins
    val io: WriteBack_IO = IO(new WriteBack_IO())

    // Input wires
    val PC       : UInt = dontTouch(WireInit(io.PC))
    val alu      : SInt = dontTouch(WireInit(io.alu))
    val mem_data : SInt = dontTouch(WireInit(io.mem_data))
    val i_s_b_imm: SInt = dontTouch(WireInit(io.i_s_b_imm))
    val u_j_imm  : SInt = dontTouch(WireInit(io.u_j_imm))
    val wr_en    : Bool = dontTouch(WireInit(io.wr_en))
    val br_en    : Bool = dontTouch(WireInit(io.br_en))
    val jalr_en  : Bool = dontTouch(WireInit(io.jalr_en))
    val jal_en   : Bool = dontTouch(WireInit(io.jal_en))
    val auipc_en : Bool = dontTouch(WireInit(io.auipc_en))
    val lui_en   : Bool = dontTouch(WireInit(io.lui_en))

    // Intermediate wires
    val auipc    : SInt = dontTouch(WireInit((PC + Cat(u_j_imm, Fill(12, "b0".U))).asSInt))
    val lui      : SInt = dontTouch(WireInit(Cat(u_j_imm, Fill(12, "b0".U)).asSInt))
    val b_inst   : SInt = dontTouch(WireInit(alu(0).asSInt))
    val PC4_rd   : SInt = dontTouch(WireInit((PC + 4.U).asSInt))
    val jal_PC   : UInt = dontTouch(WireInit(PC + u_j_imm.asUInt))
    val b_inst_PC: UInt = dontTouch(WireInit(PC + i_s_b_imm.asUInt))

    // Output wires
    val nPC    : UInt = dontTouch(WireInit(MuxCase(0.U, Array(
        br_en               -> b_inst_PC,
        jal_en              -> jal_PC,
        jalr_en             -> alu.asUInt
    ))))
    val rd_data: SInt = dontTouch(WireInit(MuxCase(alu, Array(
        auipc_en            -> auipc,
        lui_en              -> lui,
        (jal_en || jalr_en) -> PC4_rd
    ))))
    val nPC_en : Bool = dontTouch(WireInit(br_en || jal_en || jalr_en))

    // Wiring to output pins
    Array(
        io.nPC, io.nPC_en, io.rd_data
    ) zip Array(
        nPC,    nPC_en,    rd_data
    ) foreach
    {
        x => x._1 := x._2
    }
}

