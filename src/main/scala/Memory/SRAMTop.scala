package Memory

import chisel3._
import chisel3.util._

class SRAMIO extends Bundle {
        // Input ports
        val clk_i  : Bool = Input(Bool())
        val rst_i  : Bool = Input(Bool())
        val csb_i  : Bool = Input(Bool())
        val we_i   : Bool = Input(Bool())
        val wmask_i: UInt = Input(UInt(4.W))
        val addr_i : UInt = Input(UInt(13.W))
        val wdata_i: UInt = Input(UInt(32.W))

        // Output ports
        val rdata_o: UInt = Output(UInt(32.W))
}

class sram_top(hexFile: Option[String]) extends BlackBox (
        Map("IFILE_IN" -> {if (hexFile.isDefined) hexFile.get else ""})
) with HasBlackBoxResource {
        // Initializing IO ports
        val io: SRAMIO = IO(new SRAMIO)

        // Adding SRAM code
        addResource("/sram_top.v")
        addResource("/sram.v")
}

class SRAMTopIO extends Bundle {
        // Input ports
        val addr   : UInt = Input(UInt(16.W))
        val storeEn: Bool = Input(Bool())
        val loadEn : Bool = Input(Bool())
        val dataIn : SInt = Input(SInt(32.W))

        // Output ports
        val out: UInt = Output(UInt(32.W))
}

class SRAMTop(hexFile: Option[String]) extends Module {
        // Initializing IO ports
        val io: SRAMTopIO = IO(new SRAMTopIO)

        // Initializing SRAM
        val SRAM: sram_top = Module(new sram_top(hexFile))

        val clk: UInt     = dontTouch(WireInit(clock.asUInt()(0)))
        val rst: Bool     = dontTouch(WireInit(reset.asBool))

        // Connections
        Seq(
                (SRAM.io.csb_i,   1.B, 0.B,             0.B),
                (SRAM.io.we_i,    0.B, 1.B,             0.B),
                (SRAM.io.wmask_i, 0.U, 0.U,             0.U),
                (SRAM.io.addr_i,  0.U, io.addr,         io.addr),
                (SRAM.io.wdata_i, 0.U, 0.U,             io.dataIn.asUInt),
        ).map(x => x._1 := MuxCase(x._2, Seq(
                io.loadEn  -> x._3,
                io.storeEn -> x._4
        )))

        //Seq(
        //        (SRAM.io.clk_i, clk),
        //        (SRAM.io.rst_i, rst),
        //        (io.out,        SRAM.io.rdata_o)
        //).map(x => x._1 := x._2)
        SRAM.io.clk_i := clk
        SRAM.io.rst_i := rst
        io.out        := SRAM.io.rdata_o

}
