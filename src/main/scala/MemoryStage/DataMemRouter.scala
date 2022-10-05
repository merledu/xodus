package MemoryStage

import chisel3._
import chisel3.util._
import scala.math.pow

class DataMemRouterIO extends Bundle
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

    val memDataIn = Input(SInt(32.W))

    // Output pins
    val memDataOut = Output(SInt(32.W))
    val rs2DataOut = Output(SInt(32.W))
    val loadEn     = Output(Bool())
    val storeEn    = Output(Bool())
    val addr       = Output(UInt(16.W))
}
class DataMemRouter extends Module
{
    // Initializing IO pins
    val io      : DataMemRouterIO = IO(new DataMemRouterIO)
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
    //val data_mem: Mem[SInt] = Mem(pow(2, 16).toInt, SInt(32.W))

    // Intermediate wires
    val address : UInt = dontTouch(WireInit(alu_in.asUInt))
    val mem_data: SInt = dontTouch(WireInit(io.memDataIn))
    val offset         = dontTouch(WireInit(alu_in(1, 0)))
    val rs2Data        = dontTouch(Wireinit(Vecinit(4, 0.S(8.W))))

    // Store wires
    val sb: SInt = dontTouch(WireInit(rs2_data(7, 0).asSInt))
    val sh: SInt = dontTouch(WireInit(rs2_data(15, 0).asSInt))
    val sw: SInt = dontTouch(WireInit(rs2_data))

    // Storing to data memory
    //when (str_en)
    //{
    //    io.rs2DataOut := dontTouch(MuxCase(0.S, Seq(
    //        sb_en -> sb,
    //        sh_en -> sh,
    //        sw_en -> sw
    //    )))
    //}
    Seq(
            (io.loadEn,       load_en),
            (io.storeEn,      str_en),
            (io.addr,         address),
    ) map (x => x._1 := x._2)

    when (str_en) {
            when (sb_en) {
                    when (offset === 1.U) {
                            rs2Data(1) := rs2_data(7, 0)
                    }.elsewhen (offset === 2.U) {
                            rs2Data(2) := rs2_data(7, 0)
                    }.elsewhen (offset === 3.U) {
                            rs2Data(3) := rs2_data(7, 0)
                    }.otherwise {
                            rs2Data(0) := rs2_data(7, 0)
                    }
            }.elsewhen (sh_en) {
                    when (offset === 1.U) {
                            rs2Data(1) := rs2_data(7, 0)
                            rs2Data(2) := rs2_data(15, 8)
                    }.elsewhen (offset === 2.U) {
                            rs2Data(2) := rs2_data(7, 0)
                            rs2Data(3) := rs2_data(15, 8)
                    }.otherwise {
                            rs2Data(0) := rs2_data(7, 0)
                            rs2Data(1) := rs2_data(15, 8)
                    }
            }.elsewhen (sw_en) {
                    Seq(
                            (rs2Data(0), rs2_data(7, 0)),
                            (rs2Data(1), rs2_data(15, 8)),
                            (rs2Data(2), rs2_data(23, 16)),
                            (rs2Data(3), rs2_data(31, 24))
                    ) map (x => x._1 := x._2)
            }

            io.rs2DataOut := rs2Data.asSInt
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
        io.memDataOut := MuxCase(0.S, Seq(
            lb_en  -> lb,
            lh_en  -> lh,
            lw_en  -> lw,
            lbu_en -> lbu.asSInt,
            lhu_en -> lhu.asSInt
        ))
    }.otherwise
    {
        io.memDataOut := 0.S
    }
}

