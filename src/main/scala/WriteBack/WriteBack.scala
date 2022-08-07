package WriteBack

import chisel3._
import chisel3.util._

class WriteBack_IO extends Bundle
{
    // Input pins
    val PC      : UInt = Input(UInt(32.W))
    val PC4     : UInt = Input(UInt(32.W))
    val alu     : SInt = Input(SInt(32.W))
    val mem_data: SInt = Input(SInt(32.W))
    val imm     : SInt = Input(SInt(32.W))
    val br_en   : Bool = Input(Bool())
    val jalr_en : Bool = Input(Bool())
    val jal_en  : Bool = Input(Bool())
    val auipc_en: Bool = Input(Bool())
    val load_en : Bool = Input(Bool())
    val lui_en  : Bool = Input(Bool())

    // Output pins
    val nPC    : UInt = Output(UInt(32.W))
    val nPC_en : Bool = Output(Bool())
    val rd_data: SInt = Output(SInt(32.W))
}
class WriteBack extends Module
{
    // Initializing IO pins
    val io      : WriteBack_IO = IO(new WriteBack_IO)
    val PC      : UInt = dontTouch(WireInit(io.PC))
    val PC4     : UInt = dontTouch(WireInit(io.PC4))
    val alu     : SInt = dontTouch(WireInit(io.alu))
    val mem_data: SInt = dontTouch(WireInit(io.mem_data))
    val imm     : SInt = dontTouch(WireInit(io.imm))
    val br_en   : Bool = dontTouch(WireInit(io.br_en))
    val jalr_en : Bool = dontTouch(WireInit(io.jalr_en))
    val jal_en  : Bool = dontTouch(WireInit(io.jal_en))
    val auipc_en: Bool = dontTouch(WireInit(io.auipc_en))
    val lui_en  : Bool = dontTouch(WireInit(io.lui_en))
    val load_en : Bool = dontTouch(WireInit(io.load_en))

    // Intermediate wires
    val u_imm  : SInt = dontTouch(WireInit(imm << 12.U))
    val auipc  : SInt = dontTouch(WireInit((PC + u_imm.asUInt).asSInt))
    val lui    : SInt = dontTouch(WireInit(u_imm.asSInt))
    val br     : Bool = dontTouch(WireInit(alu(0).asBool))
    val PC4_rd : SInt = dontTouch(WireInit(PC4.asSInt))
    val PC_imm : UInt = dontTouch(WireInit(PC + imm.asUInt))
    val nPC    : UInt = dontTouch(WireInit(Mux((br_en && br) || jal_en, PC_imm, alu.asUInt)))  // default: jalr_PC -> rs1 + imm
    val rd_data: SInt = dontTouch(WireInit(MuxCase(alu, Array(
        auipc_en            -> auipc,
        lui_en              -> lui,
        (jal_en || jalr_en) -> PC4_rd,
        load_en             -> mem_data
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

