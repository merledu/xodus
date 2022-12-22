//package Memories
//
//import chisel3._
//import chisel3.util.experimental.loadMemoryFromFile
//import scala.math.pow
//
//class MemoryIO(PARAMS:Map[String, Int], INST:Boolean, DATA:Boolean) extends Bundle {
//        // Input ports
//        val addr: UInt = Input(UInt(PARAMS("MDEPTH").W))
//
//        // - Data Memory specific ports
//        val en     = if (DATA) Some(Input(Vec(2, Bool())))
//                     else None
//        val dataIn = if (DATA) Some(Input(SInt(PARAMS("XLEN").W)))
//                     else None
//
//        // Output ports
//        val out: UInt = Output(UInt(PARAMS("XLEN").W))
//}
//
//class MemoryO(PARAMS:Map[String, Int], INST:Boolean=false, DATA:Boolean=false) extends Module {
//        // Initializing IO ports
//        val io      : MemoryIO = IO(new MemoryIO(PARAMS, INST, DATA))
//        //val addr    : UInt     = dontTouch(WireInit(io.addr))
//        val memDepth: Int      = pow(2, PARAMS("MDEPTH")).toInt
//
//        // - Data Memory specific ports
//        val loadEn  = if (DATA) Some(dontTouch(WireInit(io.en.get(0))))
//                      else None
//        val storeEn = if (DATA) Some(dontTouch(WireInit(io.en.get(1))))
//                      else None
//        val dataIn  = if (DATA) Some(dontTouch(WireInit(io.dataIn.get)))
//                      else None
//
//        // Initializing instruction memory
//        val mem = if (INST) Some(Mem(memDepth, UInt(PARAMS("XLEN").W)))
//                  else if (DATA) Some(Mem(memDepth, SInt(PARAMS("XLEN").W)))
//                  else None
//
//        // Instruction Memory: Saving instruction hex from file to memory
//        if (INST) Some(loadMemoryFromFile(mem.get, "asm/assembly.hex"))
//                else None
//
//        // Data Memory: Saving data to memory
//        //if (DATA) when (storeEn.get) {
//        //        mem.get.write(addr, dataIn.get)
//        //} else None
//
//        // Wiring ports
//        // 
//        // - Instruction Memory
//        if (INST) io.out := mem.get.read(io.addr)
//
//        // - Data Memory
//        else if (DATA) when (loadEn.get) {
//                io.out := mem.get.read(io.addr)
//        } else None
//}
