package Tracer

import chisel3._
import Top._
import java.io._

class TracerTop extends Module
{
    // Initializing modules
    val Tracer: Tracer = Module(new Tracer)
    val Core  : Top    = Module(new Top)

    var num: Int = 4

    // Intermediate wires
    val clkCycle: UInt = dontTouch(RegInit(0.U(32.W)))
    clkCycle := clkCycle + 1.U

    // Wiring to Tracer
    Seq(
        Tracer.io.RegFD_inst,    Tracer.io.RegDA_rs1_addr,    Tracer.io.RegDA_rs2_addr, Tracer.io.RegDA_rs1_data, Tracer.io.RegAM_rs2_data,
        Tracer.io.RegMW_rd_addr, Tracer.io.WriteBack_rd_data, Tracer.io.RegDA_PC,       Tracer.io.Fetch_nPC,      Tracer.io.RegAM_load_en,
        Tracer.io.RegAM_str_en,  Tracer.io.RegAM_alu,         Tracer.io.RegMW_wr_en
    ) zip Seq(
        Core.io.RegFD_inst,    Core.io.RegDA_rs1_addr,    Core.io.RegDA_rs2_addr, Core.io.RegDA_rs1_data, Core.io.RegAM_rs2_data,
        Core.io.RegMW_rd_addr, Core.io.WriteBack_rd_data, Core.io.RegDA_PC,       Core.io.Fetch_nPC,      Core.io.RegAM_load_en,
        Core.io.RegAM_str_en,  Core.io.RegAM_alu,         Core.io.RegMW_wr_en
    ) foreach
    {
        x => x._1 := x._2
    }

    // Writing Trace to file  (TODO: Need to implement file creation directly from CHISEL3)
    // val traceFile: File = new File("trace/trace.log")
    // val traceWriter: PrintWriter = new PrintWriter(traceFile)
    // traceWriter.printf("Clock_Cycle: %d\n", num)
    // traceWriter.close()
    
    // Writing trace to console  (Temporary)
    printf(
        "ClkCycle: %d, PC: %x, Inst: %x, Mode: %d, rs1_addr: %d, rs1_rdata: %x, rs2_addr: %d, rs2_rdata: %x, rd_addr: %d, rd_wdata: %x, mem_addr: %d, mem_rdata: %x, mem_wdata: %x\n",
        clkCycle,                 Tracer.io.rvfi_pc_rdata,  Tracer.io.rvfi_insn,      Tracer.io.rvfi_mode,    Tracer.io.rvfi_rs1_addr,
        Tracer.io.rvfi_rs1_rdata, Tracer.io.rvfi_rs2_addr,  Tracer.io.rvfi_rs2_rdata, Tracer.io.rvfi_rd_addr, Tracer.io.rvfi_rd_wdata,
        Tracer.io.rvfi_mem_addr,  Tracer.io.rvfi_mem_rdata, Tracer.io.rvfi_mem_wdata
    )
}

