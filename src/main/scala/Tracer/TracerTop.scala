package Tracer

import chisel3._
import Core._
import Memory._
import java.io._

class TracerIO extends Bundle {
    val out = Output(UInt(32.W))
}

class TracerTop(progFile: Option[String], dataFile: Option[String]) extends Module {
    val io = IO(new TracerIO)
    // Initializing modules
    val Tracer : Tracer  = Module(new Tracer)
    val Core   : Core    = Module(new Core)
    val instMem: SRAMTop = Module(new SRAMTop(progFile))
    val dataMem: SRAMTop = Module(new SRAMTop(dataFile))

    var num: Int = 4

    // Intermediate wires
    val clkCycle: UInt = dontTouch(RegInit(0.U(32.W)))
    clkCycle := clkCycle + 1.U

    Seq(
            (instMem.io.addr,    Core.io.instAddr),
            (instMem.io.storeEn, 0.B),
            (instMem.io.loadEn,  1.B),
            (instMem.io.dataIn,  0.S),

            (dataMem.io.addr,    Core.io.dataAddr),
            (dataMem.io.storeEn, Core.io.storeEn),
            (dataMem.io.loadEn,  Core.io.loadEn),
            (dataMem.io.dataIn,  Core.io.rs2DataOut),

            (Core.io.memInstIn, instMem.io.out/*0.U*/),
            (Core.io.memDataIn, dataMem.io.out.asSInt/*0.S*/),
            
            (io.out, 0.U)
    ) map (x => x._1 := x._2)

    // Wiring to Tracer
    Seq(
        Tracer.io.RegFD_inst,    Tracer.io.RegDA_rs1_addr,    Tracer.io.RegDA_rs2_addr, Tracer.io.RegDA_rs1_data,    Tracer.io.RegAM_rs2_data,
        Tracer.io.RegMW_rd_addr, Tracer.io.WriteBack_rd_data, Tracer.io.RegDA_PC,       Tracer.io.Fetch_nPC,         Tracer.io.RegAM_load_en,
        Tracer.io.RegAM_str_en,  Tracer.io.RegAM_alu,         Tracer.io.RegMW_wr_en,    Tracer.io.RegDA_stallControl
    ) zip Seq(
        Core.io.RegFD_inst,    Core.io.RegDA_rs1_addr,    Core.io.RegDA_rs2_addr, Core.io.RegDA_rs1_data,    Core.io.RegAM_rs2_data,
        Core.io.RegMW_rd_addr, Core.io.WriteBack_rd_data, Core.io.RegDA_PC,       Core.io.Fetch_nPC,         Core.io.RegAM_load_en,
        Core.io.RegAM_str_en,  Core.io.RegAM_alu,         Core.io.RegMW_wr_en,    Core.io.RegDA_stallControl
    ) foreach {
        x => x._1 := x._2
    }

    // Writing Trace to file  (TODO: Need to implement file creation directly from CHISEL3)
    // val traceFile: File = new File("trace/trace.log")
    // val traceWriter: PrintWriter = new PrintWriter(traceFile)
    // traceWriter.printf("Clock_Cycle: %d\n", num)
    // traceWriter.close()
    
    // Printing RVFI values to console
    val valid: Bool = (!Tracer.io.stallControl) && (Tracer.io.rvfi_insn =/= 0.U)
    when (valid) {
        printf(
            "ClkCycle: %d, pc_rdata: %x, pc_wdata: %x, insn: %x, mode: %d, rs1_addr: %d, rs1_rdata: %x, rs2_addr: %d, rs2_rdata: %x, rd_addr: %d, rd_wdata: %x, mem_addr: %x, mem_rdata: %x, mem_wdata: %x\n",
            clkCycle,                Tracer.io.rvfi_pc_rdata,  Tracer.io.rvfi_pc_wdata,  Tracer.io.rvfi_insn,      Tracer.io.rvfi_mode,
            Tracer.io.rvfi_rs1_addr, Tracer.io.rvfi_rs1_rdata, Tracer.io.rvfi_rs2_addr,  Tracer.io.rvfi_rs2_rdata, Tracer.io.rvfi_rd_addr,
            Tracer.io.rvfi_rd_wdata, Tracer.io.rvfi_mem_addr,  Tracer.io.rvfi_mem_rdata, Tracer.io.rvfi_mem_wdata
        )
    }
}
