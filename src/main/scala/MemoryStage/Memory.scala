package MemoryStage

import chisel3._
import chisel3.util._
import scala.math.pow

class Memory_IO extends Bundle
{
    // Input pins
    val alu_in  : SInt = Input(SInt(32.W))
    val rs2_data: SInt = Input(SInt(32.W))
    val str_en  : Bool = Input(Bool())
    val load_en : Bool = Input(Bool())
    val sb_en   : Bool = Input(Bool())
    val sh_en   : Bool = Input(Bool())
    val sw_en   : Bool = Input(Bool())
    val lb_en   : Bool = Input(Bool())
    val lh_en   : Bool = Input(Bool())
    val lw_en   : Bool = Input(Bool())
    val lbu_en  : Bool = Input(Bool())
    val lhu_en  : Bool = Input(Bool())

    // Output pins
    val out: SInt = Output(SInt(32.W))
}
class Memory extends Module
{
    // Initializing IO pins
    val io      : Memory_IO = IO(new Memory_IO)
    val alu_in  : SInt      = dontTouch(WireInit(io.alu_in))
    val rs2_data: SInt      = dontTouch(WireInit(io.rs2_data))
    val str_en  : Bool      = dontTouch(WireInit(io.str_en))
    val load_en : Bool      = dontTouch(WireInit(io.load_en))
    val sb_en   : Bool      = dontTouch(WireInit(io.sb_en))
    val sh_en   : Bool      = dontTouch(WireInit(io.sh_en))
    val sw_en   : Bool      = dontTouch(WireInit(io.sw_en))
    val lb_en   : Bool      = dontTouch(WireInit(io.lb_en))
    val lh_en   : Bool      = dontTouch(WireInit(io.lh_en))
    val lw_en   : Bool      = dontTouch(WireInit(io.lw_en))
    val lbu_en  : Bool      = dontTouch(WireInit(io.lbu_en))
    val lhu_en  : Bool      = dontTouch(WireInit(io.lhu_en))

    // Data memory
    val data_mem: Mem[SInt] = Mem(pow(2, 16).toInt, SInt(32.W))

    // Intermediate wires
    val address : UInt = dontTouch(WireInit(alu_in.asUInt))
    val mem_data: SInt = dontTouch(WireInit(data_mem.read(address)))

    // Store wires
    val sb      : SInt = dontTouch(WireInit(rs2_data(7, 0).asSInt))
    val sh      : SInt = dontTouch(WireInit(rs2_data(15, 0).asSInt))
    val sw      : SInt = dontTouch(WireInit(rs2_data))

    // Storing to data memory
    when (str_en)
    {
        data_mem.write(address, MuxCase(0.S, Seq(
            sb_en -> sb,
            sh_en -> sh,
            sw_en -> sw
        )))
    }
    
    // Load wires
    val lb : SInt = dontTouch(WireInit(mem_data(7, 0).asSInt))
    val lh : SInt = dontTouch(WireInit(mem_data(15, 0).asSInt))
    val lw : SInt = dontTouch(WireInit(mem_data))
    val lbu: UInt = dontTouch(WireInit(mem_data(7, 0)))
    val lhu: UInt = dontTouch(WireInit(mem_data(15, 0)))

    // Loading data from data memory
    when (load_en)
    {
        io.out := MuxCase(0.S, Seq(
            lb_en  -> lb,
            lh_en  -> lh,
            lw_en  -> lw,
            lbu_en -> lbu.asSInt,
            lhu_en -> lhu.asSInt
        ))
    }.otherwise
    {
        io.out := 0.S
    }
}

