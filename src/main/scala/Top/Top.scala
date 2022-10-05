package Top

import chisel3._
import Memory._
import Tracer._
import java.io._

class Top extends Module {
    // Initializing modules
    val Tracer: Tracer = Module(new Tracer)
    val Core           = Module(new Core)
    val instMem        = Module(new InstMem)
    val dataMem        = Module(new DataMem)

    var num: Int = 4

    // Intermediate wires
    val clkCycle: UInt = dontTouch(RegInit(0.U(32.W)))
    clkCycle := clkCycle + 1.U

    Seq(
            (instMem.io.addr, Core.io.instAddr),

            (dataMem.io.addr,    Core.io.dataAddr),
            (dataMem.io.storeEn, Core.io.storeEn),
            (dataMem.io.loadEn,  Core.io.loadEn),
            (dataMem.io.dataIn,  Core.io.rs2DataOut),

            (Core.io.memInstIn, instMem.io.inst),
            (Core.io.memDataIn, dataMem.io.dataOut)
    ) map (x => x._1 := x._2)

    // Wiring to Tracer
    Seq(
        Tracer.io.RegFD_inst,    Tracer.io.RegDA_rs1_addr,    Tracer.io.RegDA_rs2_addr, Tracer.io.RegDA_rs1_data, Tracer.io.RegAM_rs2_data,
        Tracer.io.RegMW_rd_addr, Tracer.io.WriteBack_rd_data, Tracer.io.RegDA_PC,       Tracer.io.Fetch_nPC,      Tracer.io.RegAM_load_en,
        Tracer.io.RegAM_str_en,  Tracer.io.RegAM_alu,         Tracer.io.RegMW_wr_en
    ) zip Seq(
        Core.io.RegFD_inst,    Core.io.RegDA_rs1_addr,    Core.io.RegDA_rs2_addr, Core.io.RegDA_rs1_data, Core.io.RegAM_rs2_data,
        Core.io.RegMW_rd_addr, Core.io.WriteBack_rd_data, Core.io.RegDA_PC,       Core.io.Fetch_nPC,      Core.io.RegAM_load_en,
        Core.io.RegAM_str_en,  Core.io.RegAM_alu,         Core.io.RegMW_wr_en
    ) foreach {
        x => x._1 := x._2
    }

    // Writing Trace to file  (TODO: Need to implement file creation directly from CHISEL3)
    // val traceFile: File = new File("trace/trace.log")
    // val traceWriter: PrintWriter = new PrintWriter(traceFile)
    // traceWriter.printf("Clock_Cycle: %d\n", num)
    // traceWriter.close()
    
    // Writing trace to console  (Temporary)
    printf(
        "ClkCycle: %d, pc_rdata: %x, pc_wdata: %x, insn: %x, mode: %d, rs1_addr: %d, rs1_rdata: %x, rs2_addr: %d, rs2_rdata: %x, rd_addr: %d, rd_wdata: %x, mem_addr: %x, mem_rdata: %x, mem_wdata: %x\n",
        clkCycle,                Tracer.io.rvfi_pc_rdata,  Tracer.io.rvfi_pc_wdata,  Tracer.io.rvfi_insn,      Tracer.io.rvfi_mode,
        Tracer.io.rvfi_rs1_addr, Tracer.io.rvfi_rs1_rdata, Tracer.io.rvfi_rs2_addr,  Tracer.io.rvfi_rs2_rdata, Tracer.io.rvfi_rd_addr,
        Tracer.io.rvfi_rd_wdata, Tracer.io.rvfi_mem_addr,  Tracer.io.rvfi_mem_rdata, Tracer.io.rvfi_mem_wdata
    )
}

