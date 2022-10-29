//package Tracer
//
//import chisel3._
//
//class TracerIO(PARAMS:Map[String, Int], TRACE:Boolean) extends Bundle {
//        // Input ports
//        
//        // - RVFI ports
//        val regFDinst       = if (TRACE) Some(Input(UInt(PARAMS("XLEN").W)))
//                              else None
//        val regDErsAddr     = if (TRACE) Some(Input(Vec(2, UInt(PARAMS("REGADDRLEN").W))))
//                              else None
//        val regDErs1Data    = if (TRACE) Some(Input(SInt(PARAMS("XLEN").W)))
//                              else None
//        val regEMrs2Data    = if (TRACE) Some(Input(SInt(PARAMS("XLEN").W)))
//                              else None
//        val regMWwrEn       = if (TRACE) Some(Input(Bool()))
//                              else None
//        val regMWrdAddr     = if (TRACE) Some(Input(UInt(PARAMS("REGADDRLEN").W)))
//                              else None
//        val writeBackRdData = if (TRACE) Some(Input(SInt(PARAMS("XLEN").W)))
//                              else None
//        val regDEpc         = if (TRACE) Some(Input(UInt(PARAMS("XLEN").W)))
//                              else None
//        val fetchNPC        = if (TRACE) Some(Input(UInt(PARAMS("XLEN").W)))
//                              else None
//        val regEMloadEn     = if (TRACE) Some(Input(Bool()))
//                              else None
//        val regEMstrEn      = if (TRACE) Some(Input(Bool()))
//                              else None
//        val regEMalu        = if (TRACE) Some(Input(SInt(PARAMS("XLEN").W)))
//                              else None
//        
//        // - Hazard ports
//        val regDEstallControl = if (TRACE) Some(Input(Bool()))
//                                else None
//        
//        // Output ports  (TODO: Commented ports are not used by this tracer, need to be added in the future)
//        
//        // - Instruction metadata
//        val rvfi_valid = if (TRACE) Some(Output(UInt(PARAMS("NRET").W)))
//                         else None
//        // val rvfi_order: UInt = Output(UInt((PARAMS("NRET") * 64).W))
//        val rvfi_insn = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("ILEN")).W)))
//                        else None
//        // val rvfi_trap : UInt = Output(UInt(PARAMS("NRET").W))
//        // val rvfi_halt : UInt = Output(UInt(PARAMS("NRET").W))
//        // val rvfi_intr : UInt = Output(UInt(PARAMS("NRET").W))
//        val rvfi_mode = if (TRACE) Some(Output(UInt((PARAMS("NRET") * 2).W)))
//                        else None
//        // val rvfi_ixl  : UInt = Output(UInt((PARAMS("NRET") * 2).W))
//        
//        // - Register read/write
//        val rvfi_rs1_addr  = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("REGADDRLEN")).W)))
//                             else None
//        val rvfi_rs2_addr  = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("REGADDRLEN")).W)))
//                             else None
//        val rvfi_rs1_rdata = if (TRACE) Some(Output(SInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                             else None
//        val rvfi_rs2_rdata = if (TRACE) Some(Output(SInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                             else None
//        val rvfi_rd_addr   = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("REGADDRLEN")).W)))
//                             else None
//        val rvfi_rd_wdata  = if (TRACE) Some(Output(SInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                             else None
//        
//        // - Program Counter
//        val rvfi_pc_rdata = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                            else None
//        val rvfi_pc_wdata = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                            else None
//        
//        // - Memory Access
//        val rvfi_mem_addr = if (TRACE) Some(Output(UInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                            else None
//        // val rvfi_mem_rmask: UInt = Output(UInt((PARAMS("NRET") * PARAMS("XLEN") / 8).W))
//        // val rvfi_mem_wmask: UInt = Output(UInt((PARAMS("NRET") * PARAMS("XLEN") / 8).W))
//        val rvfi_mem_rdata = if (TRACE) Some(Output(SInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                             else None
//        val rvfi_mem_wdata = if (TRACE) Some(Output(SInt((PARAMS("NRET") * PARAMS("XLEN")).W)))
//                             else None
//}
//
//class Tracer(PARAMS:Map[String, Int], TRACE:Boolean=false) extends Module {
//        // Initializing IO ports
//        val io                = if (TRACE) Some(IO(new TracerIO(PARAMS, TRACE)))
//                                else None
//        val regFDinst         = if (TRACE) Some(dontTouch(WireInit(io.regFDinst)))
//                                else None
//        val regDErsAddr       = if (TRACE) Some(dontTouch(VecInit(io.regDErsAddr)))
//                                else None
//        val regDErs1Data      = if (TRACE) Some(dontTouch(WireInit(io.regDErs1Data)))
//                                else None
//        val regEMrs2Data      = if (TRACE) Some(dontTouch(WireInit(io.regEMrs2Data)))
//                                else None
//        val regMWwrEn         = if (TRACE) Some(dontTouch(WireInit(io.regMWwrEn)))
//                                else None
//        val regMWrdAddr       = if (TRACE) Some(dontTouch(WireInit(io.regMWrdAddr)))
//                                else None
//        val writeBackRdData   = if (TRACE) Some(dontTouch(WireInit(io.writeBackRdData)))
//                                else None
//        val regDEpc           = if (TRACE) Some(dontTouch(WireInit(io.regDEpc)))
//                                else None
//        val fetchNPC          = if (TRACE) Some(dontTouch(WireInit(io.fetchNPC)))
//                                else None
//        val regEMloadEn       = if (TRACE) Some(dontTouch(WireInit(io.regEMloadEn)))
//                                else None
//        val regEMstrEn        = if (TRACE) Some(dontTouch(WireInit(io.regEMstrEn)))
//                                else None
//        val regEMmemAddr      = if (TRACE) Some(dontTouch(WireInit(io.regEMalu.asUInt)))
//                                else None
//        val regDEstallControl = if (TRACE) Some(dontTouch(WireInit(io.regDEstallControl)))
//                                else None
//        
//        // Delay Registers
//        
//        // - inst
//        //val regDAinst = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W))))
//        //                else None
//        //val regAMinst = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W))))
//        //                else None
//        //val regMWinst = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W))))
//        //                else None
//        val reginst = if (TRACE) Some(dontTouch(Vec(3, RegInit(0.U(PARAMS("XLEN".W))))))
//                      else None
//        
//        // - rs1Addr/rs2Addr
//        //val regAMrsAddr = if (TRACE) Some(dontTouch(Vec(2, RegInit(0.U(PARAMS("REGADDRLEN").W)))))
//        //                  else None
//        //val regMWrsAddr = if (TRACE) Some(dontTouch(Vec(2, RegInit(0.U(PARAMS("REGADDRLEN").W)))))
//        //                  else None
//        val regRsAddr = if (TRACE) Some(dontTouch(Vec(2, Vec(2, RegInit(0.U(PARAMS("REGADDRLEN".W)))))))
//                        else None
//        
//        // - rs1Data/rs2Data
//        val regAMrs1Data = if (TRACE) Some(dontTouch(RegInit(0.S(PARAMS("XLEN").W))))
//                           else None
//        val regMWrsData  = if (TRACE) Some(dontTouch(Vec(2, RegInit(0.S(PARAMS("XLEN").W)))))
//                           else None
//        
//        // - PC/nPC
//        //val regEM_PC  = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        //val regMW_PC  = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        //val regFD_nPC = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        //val regDE_nPC = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        //val regEM_nPC = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        //val regMW_nPC = if (TRACE) Some(dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        val regPC  = if (TRACE) Some(dontTouch(Vec(2, RegInit(0.U(PARAMS("XLEN".W))))))
//                     else None
//        val regNPC = if (TRACE) Some(dontTouch(Vec(4, RegInit(0.U(PARAMS("XLEN".W))))))
//                     else None
//        
//        // - load_en/str_en/mem_addr
//        val RegMW_load_en : Bool = dontTouch(RegInit(0.B))) else None
//        val RegMW_str_en  : Bool = dontTouch(RegInit(0.B))) else None
//        val RegMW_mem_addr: UInt = dontTouch(RegInit(0.U(PARAMS("XLEN").W)))) else None
//        
//        // - Hazard controls
//        val RegAM_stallControl: Bool = dontTouch(RegInit(0.B))) else None
//        val RegMW_stallControl: Bool = dontTouch(RegInit(0.B))) else None
//        
//        // RVFI wires
//        
//        // - Instruction metadata
//        val rvfi_insn = if (TRACE) Some(dontTouch(WireInit()))
//                        else None
//        val rvfi_mode = if (TRACE) Some(dontTouch(WireInit()))
//                        else None
//        
//        // - Register read/write
//        val rvfi_rs1_addr  = if (TRACE) Some(dontTouch(WireInit()))
//                             else None
//        val rvfi_rs2_addr  = if (TRACE) Some(dontTouch(WireInit()
//                             else None
//        val rvfi_rs1_rdata = if (TRACE) Some(dontTouch(WireInit(regMWrs1data)))
//                             else None
//        val rvfi_rs2_rdata = if (TRACE) Some(dontTouch(WireInit(regMWrs2data)))
//                             else None
//        val rvfi_rd_addr   = if (TRACE) Some(dontTouch(WireInit(Mux(regMW_wr_en, RegMW_rd_addr, 0.U))))
//                             else None
//        val rvfi_rd_wdata  = if (TRACE) Some(dontTouch(WireInit(Mux(regMW_wr_en, WriteBack_rd_data, 0.S))))
//                             else None
//        
//        // - Program Counter
//        val rvfi_pc_rdata = if (TRACE) Some(dontTouch(WireInit(RegMW_PC)))
//                            else None
//        val rvfi_pc_wdata = if (TRACE) Some(dontTouch(WireInit(RegMW_nPC)))
//                            else None
//        
//        // - Memory Access
//        val rvfi_mem_addr  = if (TRACE) Some(dontTouch(WireInit(Mux(RegMW_load_en || RegMW_str_en, RegMW_mem_addr, 0.U))))
//                             else None
//        val rvfi_mem_rdata = if (TRACE) Some(dontTouch(WireInit(Mux(RegMW_load_en, WriteBack_rd_data, 0.S))))
//                             else None
//        val rvfi_mem_wdata = if (TRACE) Some(dontTouch(WireInit(Mux(RegMW_str_en, RegMW_rs2_data, 0.S))))
//                             else None
//        
//        // - Hazard controls
//        val stallControl = if (TRACE) Some(dontTouch(WireInit(RegMW_stallControl)))
//                           else None
//        
//        // Wiring to output ports
//        if (TRACE) Seq(
//                // Output ports
//                
//                // - RVFI ports
//                io.rvfi_insn,      io.rvfi_mode,      io.rvfi_rs1_addr,  io.rvfi_rs2_addr, io.rvfi_rs1_rdata,
//                io.rvfi_rs2_rdata, io.rvfi_rd_addr,   io.rvfi_rd_wdata,  io.rvfi_pc_rdata, io.rvfi_pc_wdata,
//                io.rvfi_mem_addr,  io.rvfi_mem_rdata, io.rvfi_mem_wdata,
//                
//                // - Hazard controls
//                io.stallControl,
//                
//                // Delay Registers
//                
//                // - inst
//                regDAinst, regAMinst, regMWinst,
//                
//                // - rs1Addr/rs2Addr
//                RegAM_rs1_addr, RegAM_rs2_addr, RegMW_rs1_addr, RegMW_rs2_addr,
//                
//                // - rs1_data/rs2_data
//                RegAM_rs1_data, RegMW_rs1_data, RegMW_rs2_data,
//                
//                // - PC/nPC
//                RegAM_PC,  RegMW_PC, RegFD_nPC, RegDA_nPC, RegAM_nPC,
//                RegMW_nPC,
//                
//                // - load_en/str_en/mem_addr
//                RegMW_load_en, RegMW_str_en, RegMW_mem_addr,
//                
//                // - Hazard controls
//                RegAM_stallControl, RegMW_stallControl
//                
//        ) zip Seq(
//                // Output ports
//                
//                // - RVFI ports
//                regMWinst,      3.U,      regMWrsAddr(0),  regMWrsAddr(1), rvfi_rs1_rdata,
//                rvfi_rs2_rdata, rvfi_rd_addr,   rvfi_rd_wdata,  rvfi_pc_rdata, rvfi_pc_wdata,
//                rvfi_mem_addr,  rvfi_mem_rdata, rvfi_mem_wdata,
//                
//                // - Hazard controls
//                stallControl,
//                
//                // Delay Registers
//                
//                // - inst
//                regFDinst, regDAinst, regAMinst,
//                
//                // - rs1_addr/rs2_addr
//                regDArs1Addr, regDArs2Addr, RegAM_rs1_addr, RegAM_rs2_addr,
//                
//                // - rs1_data/rs2_data
//                RegDA_rs1_data, RegAM_rs1_data, RegAM_rs2_data,
//                
//                // - PC/nPC
//                RegDA_PC, RegAM_PC, Fetch_nPC, RegFD_nPC, RegDA_nPC,
//                RegAM_nPC,
//                
//                // - load_en/str_en/mem_addr
//                RegAM_load_en, RegAM_str_en, RegAM_mem_addr,
//                
//                // - Hazard controls
//                RegDA_stallControl, RegAM_stallControl
//                
//        ) foreach {
//                x => x._1 := x._2
//        } else None
//}
