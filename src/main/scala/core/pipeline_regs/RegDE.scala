//package xodus.core.pipeline_regs
//
//import chisel3._,
//       chisel3.util._
//import xodus.configs.Configs,
//       xodus.core.decode_stage.DecoderIO
//
//
//class RegDE_IO extends Bundle with Configs {
//  // Input ports
//  val opcodeIn     : UInt      = Flipped(new DecoderIO().opcode)
//  val rAddrIn      : Vec[UInt] = Flipped(new DecoderIO().rAddr)
//  val funct3In     : UInt      = Flipped(new DecoderIO().funct3)
//  val funct7_imm7In: UInt      = Flipped(new DecoderIO().funct7_imm7)
//  val dataIn       : Vec[SInt] = Input(Vec(3, SInt(XLEN.W)))
//  //val pcIn         : UInt      = Flipped(new RegFD_IO().pcOut)
//
//  // Output ports
//  val opcodeOut     : UInt      = Flipped(opcodeIn)
//  val rAddrOut      : Vec[UInt] = Flipped(rAddrIn)
//  val funct3Out     : UInt      = Flipped(funct3In)
//  val funct7_imm7Out: UInt      = Flipped(funct7_imm7In)
//  val dataOut       : Vec[SInt] = Flipped(dataIn)
//  //val pcOut         : UInt      = Flipped(pcIn)
//}
//
//
//class RegDE extends Module with Configs {
//  val io: RegDE_IO = IO(new RegDE_IO)
//
//  // Pipeline
//  genPipeline(Seq(
//    io.opcodeIn      -> io.opcodeOut,
//    io.funct3In      -> io.funct3Out,
//    io.funct7_imm7In -> io.funct7_imm7Out,
//    //io.pcIn          -> io.pcOut
//  ) ++ (
//    for (i <- 0 until io.rAddrIn.length)
//      yield io.rAddrIn(i) -> io.rAddrOut(i)
//  ) ++ (
//    for (i <- 0 until io.dataOut.length)
//      yield io.dataIn(i) -> io.dataOut(i)
//  )).map {
//    x =>
//      x._2 := x._3
//      x._1 := x._2
//  }
//}
