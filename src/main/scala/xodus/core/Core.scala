package xodus.core

import chisel3._
import xodus.core.fetch_stage._/*,
       xodus.core.decode_stage._,
       core.execute_stage._,
       core.memory_stage._,
       core.write_back_stage._*/,
       xodus.core.pipeline_regs._,
       xodus.sram.IMemTopIO


class CoreIO extends Bundle {
  val imem: IMemTopIO = Flipped(new IMemTopIO)
}


class Core extends Module {
  val io: CoreIO = IO(new CoreIO)

  // Modules
  val pc            : PCIO            = Module(new PC).io
  val imem_interface: IMemInterfaceIO = Module(new IMemInterface).io

  val reg_fd = Module(new RegFD).io


  /*** Interconnections ***/

  // Fetch Stage
  imem_interface := pc.pc
  io.imem        <> imem_interface.imem
  reg_fd.in.pc   := pc.pc
  reg_fd.in.inst := imem_interface.inst
}
//class CoreIO extends Bundle with Configs {
//  val iMem: SRAMTopIO = Flipped(new SRAMTopIO)
//  val dMem: SRAMTopIO = Flipped(new SRAMTopIO)
//}
//
//
//class Core extends Module with Configs {
//  val io: CoreIO = IO(new CoreIO)
//
//  // Modules
//  val pc           : PCIO            = Module(new PC).io
//  val iMemInterface: IMemInterfaceIO = Module(new IMemInterface).io
//
//  val regFD = Module(new RegFD).io
//
//  val decoder: DecoderIO      = Module(new Decoder).io
//  val regFile: RegisterFileIO = Module(new RegisterFile).io
//  val cu     : ControlUnitIO  = Module(new ControlUnit).io
//
//  val regDE = Module(new RegDE).io
//
//  val alu: ALUIO = Module(new ALU).io
//  val dMemAligner: DMemAlignerIO = Module(new DMemAligner).io
//
//  val regEM = Module(new RegEM).io
//
//  val dMemInterface: DMemInterfaceIO = Module(new DMemInterface).io
//
//  val regMW = Module(new RegMW).io
//
//  val wb: WriteBackIO = Module(new WriteBack).io
//
//
//  /***************
//   * Fetch Stage *
//   ***************/
//
//  pc.stall         := cu.ctrl.pc.stall
//  iMemInterface.pc := pc.pc
//  io.iMem          <> iMemInterface.iMemInterface
//  regFD.in.pc      := pc.pc
//  regFD.in.inst    := iMemInterface.inst
//  regFD.stall      := cu.ctrl.regFD.stall
//
//
//  /****************
//   * Decode Stage *
//   ****************/
//
//  decoder.inst             := regFD.out.inst
//  decoder.ctrl             <> cu.ctrl.decoder
//  cu.opcode                := decoder.opcode
//  cu.funct3                := decoder.funct3
//  cu.funct7                := decoder.funct7
//  cu.dMemOffset            <> dMemAligner.offset
//  cu.dMemAlign             <> dMemAligner.align
//  regDE.in.pc              := regFD.out.pc
//  regDE.in.rAddr           <> decoder.rAddr
//  regDE.in.intData(2)      := decoder.imm
//  regDE.in.regFileCtrl     <> cu.ctrl.regFile
//  regDE.in.aluCtrl         <> cu.ctrl.alu
//  regDE.in.dMemAlignerCtrl <> cu.ctrl.dMemAligner
//  regDE.in.dMemCtrl        <> cu.ctrl.dMem
//  regDE.stall              := cu.ctrl.regDE.stall
//  for (i <- 0 to 1) {
//    regFile.rAddr(i + 1) := decoder.rAddr(i + 1)
//    regDE.in.intData(i)  := regFile.read(i)
//  }
//
//
//  /*****************
//   * Execute Stage *
//   *****************/
//
//  alu.pc               := regDE.out.pc
//  alu.in               <> regDE.out.intData
//  alu.ctrl             <> regDE.out.aluCtrl
//  dMemAligner.alu      := alu.out
//  dMemAligner.ctrl     <> regDE.out.dMemAlignerCtrl
//  dMemAligner.dMemCtrl <> regDE.out.dMemCtrl
//  dMemAligner.store    := regDE.out.intData(1)
//  regEM.in.rAddr       <> regDE.out.rAddr
//  regEM.in.regFileCtrl <> regDE.out.regFileCtrl
//  regEM.in.alu         := alu.out
//  regEM.in.dMemCtrl    <> regDE.out.dMemCtrl
//  regEM.in.wmask       := dMemAligner.wmask
//  regEM.in.dMemAddr    := dMemAligner.addr
//  regEM.in.store       := dMemAligner.alignedStore
//
//
//  /****************
//   * Memory Stage *
//   ****************/
//
//  dMemInterface.ctrl   <> regEM.out.dMemCtrl
//  dMemInterface.addr   := regEM.out.dMemAddr
//  dMemInterface.wmask  := regEM.out.wmask
//  dMemInterface.store  := regEM.out.store
//  io.dMem              <> dMemInterface.dMemInterface
//  regMW.in.rAddr       <> regEM.out.rAddr
//  regMW.in.regFileCtrl <> regEM.out.regFileCtrl
//  regMW.in.alu         := regEM.out.alu
//  regMW.in.load        <> dMemInterface.load
//
//
//  /********************
//   * Write Back Stage *
//   ********************/
//
//  wb.alu              := regMW.out.alu
//  wb.load             <> regMW.out.load
//  regFile.rAddr(0)    := regMW.out.rAddr(0)
//  regFile.write.valid := regMW.out.regFileCtrl.intWrite
//  regFile.write.bits  := wb.out
//}
