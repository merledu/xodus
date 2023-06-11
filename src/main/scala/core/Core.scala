package core

import chisel3._
import configs.Configs,
       core.fetch_stage._,
       core.decode_stage._,
       core.execute_stage._,
       core.memory_stage._,
       core.write_back_stage._,
       core.pipeline_regs._,
       sram.SRAMTopIO


class CoreIO extends Bundle with Configs {
  val iMem: SRAMTopIO = Flipped(new SRAMTopIO)
  val dMem: SRAMTopIO = Flipped(new SRAMTopIO)
}


class Core extends Module with Configs {
  val io: CoreIO = IO(new CoreIO)

  // Modules
  val pc           : PCIO            = Module(new PC).io
  val iMemInterface: IMemInterfaceIO = Module(new IMemInterface).io

  val regFD = Module(new RegFD).io

  val decoder: DecoderIO     = Module(new Decoder).io
  val regFile: IntRegFileIO  = Module(new IntRegFile).io
  val cu     : ControlUnitIO = Module(new ControlUnit).io

  val regDE = Module(new RegDE).io

  val alu: ALUIO = Module(new ALU).io

  val regEM = Module(new RegEM).io

  val dMemInterface: DMemInterfaceIO = Module(new DMemInterface).io

  val regMW = Module(new RegMW).io

  val wb: WriteBackIO = Module(new WriteBack).io


  /***************
   * Fetch Stage *
   ***************/

  iMemInterface.pc := pc.pc
  io.iMem          <> iMemInterface.iMemInterface
  regFD.in.pc      := pc.pc
  regFD.in.inst    := iMemInterface.inst


  /****************
   * Decode Stage *
   ****************/

  decoder.inst         := regFD.out.inst
  decoder.ctrl         <> cu.ctrl.decoder
  cu.opcode            := decoder.opcode
  cu.funct3            := decoder.funct3
  cu.funct7_imm7       := decoder.funct7_imm7
  regDE.in.pc          := regFD.out.pc
  regDE.in.rAddr       <> decoder.rAddr
  regDE.in.intData(2)  := decoder.imm
  regDE.in.regFileCtrl <> cu.ctrl.regFile
  regDE.in.aluCtrl     <> cu.ctrl.alu
  regDE.in.dMemCtrl    <> cu.ctrl.dMem
  for (i <- 0 to 1) {
    regFile.rAddr(i + 1) := decoder.rAddr(i + 1)
    regDE.in.intData(i)  := regFile.read(i)
  }

  regFile.rAddr(0) := 0.U
  regFile.write.valid := 0.B
  regFile.write.bits := 0.S


  /*****************
   * Execute Stage *
   *****************/

  alu.pc               := regDE.out.pc
  alu.in               <> regDE.out.intData
  alu.ctrl             <> regDE.out.aluCtrl
  regEM.in.rAddr       <> regDE.out.rAddr
  regEM.in.regFileCtrl <> regDE.out.regFileCtrl
  regEM.in.alu         := alu.out
  regEM.in.dMemCtrl    <> regDE.out.dMemCtrl
  regEM.in.storeData   := regDE.out.intData(1)


  /****************
   * Memory Stage *
   ****************/

  dMemInterface.ctrl      <> regEM.out.dMemCtrl
  dMemInterface.alu       := regEM.out.alu
  dMemInterface.storeData := regEM.out.storeData
  io.dMem                 <> dMemInterface.dMemInterface
  regMW.in.rAddr          <> regEM.out.rAddr
  regMW.in.regFileCtrl    <> regEM.out.regFileCtrl
  regMW.in.alu            := regEM.out.alu
  regMW.in.load           <> dMemInterface.load


  /********************
   * Write Back Stage *
   ********************/

  wb.alu              := regMW.out.alu
  wb.load             <> regMW.out.load
  regFile.rAddr(0)    := regMW.out.rAddr(0)
  regFile.write.valid := regMW.out.regFileCtrl.intWrite
  regFile.write.bits  := wb.out
}
