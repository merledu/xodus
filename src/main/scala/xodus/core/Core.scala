package xodus.core

import chisel3._
import fetch_stage._,
       xodus.core.decode_stage._,
       xodus.core.execute_stage._,
       xodus.core.memory_stage._,
       xodus.core.write_back_stage._,
       pipeline_regs._,
       xodus.sram.{IMemTopIO, DMemTopIO}


class CoreIO extends Bundle {
  val imem: IMemTopIO = Flipped(new IMemTopIO)
  val dmem: DMemTopIO = Flipped(new DMemTopIO)
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

  val dmem_interface: DMemInterfaceIO = Module(new DMemInterface).io

  val reg_mw = Module(new RegMW).io

  val write_back: WriteBackIO = Module(new WriteBack).io


  /*** Interconnections ***/

  // Fetch Stage
  imem_interface.pc := pc.pc
  io.imem           <> imem_interface.imem
  reg_fd.in.pc      := pc.pc
  reg_fd.in.inst    := imem_interface.inst

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
  reg_de.in.dmem_ctrl     <> ctrl_unit.ctrl.dmem
  for (i <- 0 until 2) {
    reg_file.r_addr(i + 1) := decoder.r_addr(i + 1)
    reg_de.in.int_data(i)  := reg_file.int_read(i)
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
  dmem_interface.ctrl       <> reg_em.out.dmem_ctrl
  dmem_interface.addr       := reg_em.out.alu.asUInt
  dmem_interface.store_data := reg_em.out.store_data
  io.dmem                   <> dmem_interface.dmem
  reg_mw.in.rd_addr         := reg_em.out.rd_addr
  reg_mw.in.reg_file_ctrl   <> reg_em.out.reg_file_ctrl
  reg_mw.in.alu             := reg_em.out.alu
  reg_mw.in.load            <> dmem_interface.load

  // Write Back Stage
  write_back.alu      := reg_mw.out.alu
  write_back.load     <> reg_mw.out.load
  reg_file.r_addr(0)  := reg_mw.out.rd_addr
  reg_file.write_data := write_back.out
  reg_file.ctrl       <> reg_mw.out.reg_file_ctrl
}