package xodus.core

import chisel3._
import fetch_stage._,
       xodus.core.decode_stage._,
       xodus.core.execute_stage._/*,
       core.memory_stage._,
       core.write_back_stage._*/,
       pipeline_regs._,
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

  val decoder  : DecoderIO      = Module(new Decoder).io
  val reg_file : RegisterFileIO = Module(new RegisterFile).io
  val ctrl_unit: ControlUnitIO  = Module(new ControlUnit).io

  val reg_de = Module(new RegDE).io

  val alu: ALUIO = Module(new ALU).io

  val reg_em = Module(new RegEM).io


  /*** Interconnections ***/

  // Fetch Stage
  imem_interface := pc.pc
  io.imem        <> imem_interface.imem
  reg_fd.in.pc   := pc.pc
  reg_fd.in.inst := imem_interface.inst

  // Decode Stage
  decoder.inst            := reg_fd.out.inst
  ctrl_unit.opcode        := decoder.opcode
  ctrl_unit.funct3        := decoder.funct3
  ctrl_unit.funct7        := decoder.funct7
  decoder.ctrl            <> ctrl_unit.ctrl.decoder
  reg_de.in.pc            := reg_fd.out.pc
  reg_de.in.rd_addr       := decoder.r_addr(0)
  reg_de.in.int_data(2)   := decoder.imm
  reg_de.in.reg_file_ctrl <> ctrl_unit.ctrl.reg_file
  reg_de.in.alu_ctrl      <> ctrl_unit.ctrl.alu
  for (i <- 0 until 2) {
    reg_file.r_addr(i + 1) := decoder.r_addr(i + 1)
    reg_de.in.int_data(i)  := reg_file.read(i)
  }

  // Execute Stage
  alu.in                  <> reg_de.out.int_data
  alu.pc                  := reg_de.out.pc
  alu.ctrl                <> reg_de.out.alu_ctrl
  reg_em.in.rd_addr       := reg_de.out.rd_addr
  reg_em.in.store_data    := reg_de.out.int_data(1)
  reg_em.in.reg_file_ctrl <> reg_de.out.reg_file_ctrl
  reg_em.in.alu           := alu.out
  reg_em.in.dmem_ctrl     <> reg_de.out.dmem_ctrl

  // Memory Stage

  // Write Back Stage
}
//class CoreIO extends Bundle with Configs {
//  val dMem: SRAMTopIO = Flipped(new SRAMTopIO)
//}
//
//
//class Core extends Module with Configs {
//  val dMemAligner: DMemAlignerIO = Module(new DMemAligner).io
//
//  val regEM = Module(new RegEM).io
//
//  val dMemInterface: DMemInterfaceIO = Module(new DMemInterface).io
//
//  val regMW = Module(new RegMW).io
//
//  val wb: WriteBackIO = Module(new WriteBack).io
//  /*****************
//   * Execute Stage *
//   *****************/
//
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
