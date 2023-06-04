package core

import chisel3._
import configs.Configs,
       core.fetch_stage._,
       core.decode_stage._,
       core.execute_stage._,
       core.memory_stage._,
       core.write_back_stage._,
       core.pipeline_regs._,
       memory.MemoryIO,
       debug_io.DebugCore


class CoreIO extends Bundle with Configs {
  val iMem: MemoryIO = Flipped(new MemoryIO)
  val dMem: MemoryIO = Flipped(new MemoryIO)


  val debug: Option[DebugCore] = if (Debug) Some(new DebugCore) else None
}


class Core extends Module with Configs {
  val io: CoreIO = IO(new CoreIO)

  // Modules
  val pc: PCIO = Module(new PC).io

  val regFD = Module(new RegFD).io

  val decoder: DecoderIO     = Module(new Decoder).io
  val regFile: RegFileIO     = Module(new RegFile).io
  val cu     : ControlUnitIO = Module(new ControlUnit).io

  val regDE = Module(new RegDE).io

  val alu: ALUIO = Module(new ALU).io

  val regEM = Module(new RegEM).io

  val dMemAligner: DMemAlignerIO = Module(new DMemAligner).io

  val regMW = Module(new RegMW).io

  val wb: WriteBackIO = Module(new WriteBack).io


  /***************
   * Fetch Stage *
   ***************/

  io.iMem.req.addr.bits  := pc.addr
  io.iMem.req.addr.valid := 1.B
  io.iMem.req.data.bits  := 0.U
  io.iMem.req.data.valid := 0.B
  regFD.in.pc            := pc.pc
  regFD.in.inst          := io.iMem.resp.data


  /****************
   * Decode Stage *
   ****************/

  decoder.inst       := regFD.out.inst
  decoder.en         <> cu.en.decoder
  cu.opcode          := decoder.opcode
  cu.funct3          := decoder.funct3
  cu.funct7_imm7     := decoder.funct7_imm7
  regDE.in.pc        := regFD.out.pc
  regDE.in.data(2)   := decoder.imm
  regDE.in.regFileEN <> cu.en.regFile
  regDE.in.aluEN     <> cu.en.alu
  regDE.in.dMemEN    <> cu.en.dMem
  regDE.in.rAddr     <> decoder.rAddr
  for (i <- 0 to 1) {
    regFile.rAddr(i + 1) := decoder.rAddr(i + 1)
    regDE.in.data(i)     := regFile.read(i)
  }


  /*****************
   * Execute Stage *
   *****************/

  alu.pc             := regDE.out.pc
  alu.in             <> regDE.out.data
  alu.en             <> regDE.out.aluEN
  regEM.in.rAddr     <> regDE.out.rAddr
  regEM.in.regFileEN <> regDE.out.regFileEN
  regEM.in.alu       := alu.out
  regEM.in.dMemEN    <> regDE.out.dMemEN
  regEM.in.storeData := regDE.out.data(1)


  /****************
   * Memory Stage *
   ****************/

  dMemAligner.en        <> regEM.out.dMemEN
  dMemAligner.addr      := regEM.out.alu
  dMemAligner.storeData := regEM.out.storeData
  io.dMem               <> dMemAligner.dMemReqResp
  regMW.in.rAddr        <> regEM.out.rAddr
  regMW.in.regFileEN    <> regEM.out.regFileEN
  regMW.in.alu          := regEM.out.alu
  regMW.in.load         <> dMemAligner.load


  /********************
   * Write Back Stage *
   ********************/

  wb.alu              := regMW.out.alu
  wb.load             <> regMW.out.load
  regFile.rAddr(0)    := regMW.out.rAddr(0)
  regFile.write.valid := regMW.out.regFileEN.write
  regFile.write.bits  := wb.out



  // Debug
  if (Debug) {
    io.debug.get.pc               <> pc

    io.debug.get.regFD.out <> regFD.out

    io.debug.get.decoder.opcode      := decoder.opcode
    io.debug.get.decoder.rAddr       <> decoder.rAddr
    io.debug.get.decoder.funct3      := decoder.funct3
    io.debug.get.decoder.funct7_imm7 := decoder.funct7_imm7
    io.debug.get.decoder.imm         := decoder.imm

    io.debug.get.regFile.read <> regFile.read

    io.debug.get.cu.en <> cu.en

    io.debug.get.regDE.out <> regDE.out

    io.debug.get.alu.out := alu.out

    io.debug.get.regEM.out <> regEM.out

    io.debug.get.dMemAligner.load    <> dMemAligner.load
    io.debug.get.dMemAligner.dMemReq <> dMemAligner.dMemReqResp.req

    io.debug.get.regMW.out <> regMW.out

    io.debug.get.wb.out := wb.out
  }
}
