package WriteBackStage

import chisel3._
import chisel3.util._

class WriteBack_IO extends Bundle
{
    // Input pins
    val PC      : UInt = Input(UInt(32.W))
    val alu     : SInt = Input(SInt(32.W))
    val mem_data: SInt = Input(SInt(32.W))
    val imm     : SInt = Input(SInt(32.W))
    val jalr_en : Bool = Input(Bool())
    val jal_en  : Bool = Input(Bool())
    val auipc_en: Bool = Input(Bool())
    val load_en : Bool = Input(Bool())
    val lui_en  : Bool = Input(Bool())

    // Output pins
    val rd_data: SInt = Output(SInt(32.W))
}
class WriteBack extends Module
{
    // Initializing IO pins
    val io      : WriteBack_IO = IO(new WriteBack_IO)
    val PC      : UInt = dontTouch(WireInit(io.PC))
    val alu     : SInt = dontTouch(WireInit(io.alu))
    val mem_data: SInt = dontTouch(WireInit(io.mem_data))
    val imm     : SInt = dontTouch(WireInit(io.imm))
    val jalr_en : Bool = dontTouch(WireInit(io.jalr_en))
    val jal_en  : Bool = dontTouch(WireInit(io.jal_en))
    val auipc_en: Bool = dontTouch(WireInit(io.auipc_en))
    val lui_en  : Bool = dontTouch(WireInit(io.lui_en))
    val load_en : Bool = dontTouch(WireInit(io.load_en))

    // Intermediate wires
    val u_imm  : SInt = dontTouch(WireInit(imm << 12.U))
    val auipc  : SInt = dontTouch(WireInit((PC + u_imm.asUInt).asSInt))
    val lui    : SInt = dontTouch(WireInit(u_imm.asSInt))
    val PC4_rd : SInt = dontTouch(WireInit((PC + 4.U).asSInt))
    val PC_imm : UInt = dontTouch(WireInit(PC + imm.asUInt))
    val rd_data: SInt = dontTouch(WireInit(MuxCase(alu, Seq(
        auipc_en            -> auipc,
        lui_en              -> lui,
        (jal_en || jalr_en) -> PC4_rd,
        load_en             -> mem_data
    ))))

    // Wiring to output pins
    io.rd_data := rd_data
}

