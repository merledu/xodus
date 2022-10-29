package Top

import chisel3._
import ParamsAndConsts._
import Core._
import Memories._
import Temp._
//import Tracer._

class Top(TRACE:Boolean=false) extends Module {
        // Initializing modules
        val core   : Core    = Module(new Core(Params.PARAMS, Consts.OPCODES))
        val instMem: MemoryO = Module(new MemoryO(Params.PARAMS, true))
        val dataMem: DataMem = Module(new DataMem)
        //val tracer = if (TRACE) Some(Module(new Tracer(Params.PARAMS, TRACE)))
        //             else None

        // Intermediate wires
        val clkCycle: UInt = dontTouch(RegInit(0.U(32.W)))
        clkCycle := clkCycle + 1.U

        // Wiring the modules
        Seq(
                // Instruction Memory
                instMem.io.addr,

                // Data Memory
                dataMem.io.addr, dataMem.io.dataIn, dataMem.io.storeEn, dataMem.io.loadEn,

                // Core
                core.io.inst, core.io.dataIn,
        ) zip Seq(
                // Instruction Memory
                core.io.instAddr,

                // Data Memory
                core.io.dataAddr, core.io.dataOut, core.io.storeEn, core.io.loadEn,

                // Core
                instMem.io.out, dataMem.io.out
        ) foreach {
                x => x._1 := x._2
        }

        //if (TRACE) Seq(
        //        tracer.get.io.RegFD_inst,    tracer.get.io.RegDA_rs1_addr,    tracer.get.io.RegDA_rs2_addr, tracer.get.io.RegDA_rs1_data,    tracer.get.io.RegAM_rs2_data,
        //        tracer.get.io.RegMW_rd_addr, tracer.get.io.WriteBack_rd_data, tracer.get.io.RegDA_PC,       tracer.get.io.Fetch_nPC,         tracer.get.io.RegAM_load_en,
        //        tracer.get.io.RegAM_str_en,  tracer.get.io.RegAM_alu,         tracer.get.io.RegMW_wr_en,    tracer.get.io.RegDA_stallControl
        //) zip Seq(
        //        core.io.RegFD_inst,    core.io.RegDA_rs1_addr,    core.io.RegDA_rs2_addr, core.io.RegDA_rs1_data,    core.io.RegAM_rs2_data,
        //        core.io.RegMW_rd_addr, core.io.WriteBack_rd_data, core.io.RegDA_PC,       core.io.Fetch_nPC,         core.io.RegAM_load_en,
        //        core.io.RegAM_str_en,  core.io.RegAM_alu,         core.io.RegMW_wr_en,    core.io.RegDA_stallControl
        //) foreach {
        //        x => x._1.get := x._2
        //} else None

        //// Writing Trace to file  (TODO: Need to implement file creation directly from CHISEL3)
        //// var num: Int = 4
        //// val traceFile: File = new File("trace/trace.log")
        //// val traceWriter: PrintWriter = new PrintWriter(traceFile)
        //// traceWriter.printf("Clock_Cycle: %d\n", num)
        //// traceWriter.close()
        //
        //// Printing RVFI values to console
        //val valid: Bool = (!tracer.get.io.stallControl.get) && (tracer.get.io.rvfi_insn.get =/= 0.U)
        //when (valid) {
        //        printf(
        //                "ClkCycle: %d, pc_rdata: %x, pc_wdata: %x, insn: %x, mode: %d, rs1_addr: %d, rs1_rdata: %x, rs2_addr: %d, rs2_rdata: %x, rd_addr: %d, rd_wdata: %x, mem_addr: %x, mem_rdata: %x, mem_wdata: %x\n",
        //                clkCycle,                        tracer.get.io.rvfi_pc_rdata.get,  tracer.get.io.rvfi_pc_wdata.get,  tracer.get.io.rvfi_insn.get,      tracer.get.io.rvfi_mode.get,
        //                tracer.get.io.rvfi_rs1_addr.get, tracer.get.io.rvfi_rs1_rdata.get, tracer.get.io.rvfi_rs2_addr.get,  tracer.get.io.rvfi_rs2_rdata.get, tracer.get.io.rvfi_rd_addr.get,
        //                tracer.get.io.rvfi_rd_wdata.get, tracer.get.io.rvfi_mem_addr.get,  tracer.get.io.rvfi_mem_rdata.get, tracer.get.io.rvfi_mem_wdata.get
        //        )
        //}
}
