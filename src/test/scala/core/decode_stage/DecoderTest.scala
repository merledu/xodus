package decode_stage

import chisel3._
import chisel3.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import scala.util.Random

import configs._


class DecoderTest extends AnyFreeSpec with ChiselScalatestTester {
  "Decoder" in {
    test(new Decoder()) {
      decoder =>
        val debug: Boolean = false

        decoder.io.inst.poke(0x00500293.U)

        // TODO: Write an automated testbench
        //val typeArray: Seq[String] = Seq("R", "I", "S", "B", "U", "J")
        //val opcodeMap: Map[String, Seq[Int]] = Map(
        //  "R" -> Seq(
        //    0x33,  // arithmetic 32bit
        //    0x3B  // arithmetic 64bit
        //  ),
        //  "I" -> Seq(
        //    0x03,  // load
        //    0x13,  // arithmetic 32bit
        //    0x67,  // jalr
        //    0x0F,  // fence
        //    0x73,  // csr
        //    0x1B  // arithmetic 64bit
        //  ),
        //  "S" -> Seq(0x23),  // store
        //  "B" -> Seq(0x63),  // branch
        //  "U" -> Seq(
        //    0x17,  // auipc
        //    0x37  // lui
        //  ),
        //  "J" -> Seq(0x6F)  // jal
        //)

        //val f7Array: Seq[Int] = Seq(
        //  0x00,
        //  0x20
        //)
        //
        //for (i <- 0 until 100) {
        //  val typeNdx: Int = Random.nextInt(6)
        //  val instType: String = typeArray(typeNdx)
        //  if (debug) {
        //    println(s"instType: ${instType}")
        //  }
        //
        //  val opcodeNdx: Int = Random.nextInt(
        //    if ((instType == "R") ||
        //      (instType == "S") ||
        //      (instType == "B") ||
        //      (instType == "J")
        //    ) {
        //      1
        //    } else if (instType == "I") {
        //      3
        //    } else if (instType == "U") {
        //      2
        //    } else {
        //      0
        //    }
        //  )
        //  val opcode: Int = opcodeMap(instType)(opcodeNdx)
        //  if (debug) {
        //    println(s"[Expected] opcode: ${opcode}")
        //  }
        //
        //  val rAddr: Seq[Int] = Seq(
        //    if ((instType == "R") || (instType == "I") || (instType == "U") || (instType == "J")) {
        //      Random.nextInt(32)  // rdAddr
        //    } else {
        //      0
        //    },
        //    if ((instType == "R") || (instType == "I") || (instType == "S") || (instType == "B")) {
        //      Random.nextInt(32)  // rs1Addr
        //    } else {
        //      0
        //    },
        //    if ((instType == "R") || (instType == "S") || (instType == "B")) {
        //      Random.nextInt(32)  // rs2Addr
        //    } else {
        //      0
        //    }
        //  )
        //  if (debug) {
        //    println(s"[Expected] rdAddr: ${rAddr(0)}")
        //    for (i <- 1 until 3) {
        //      println(s"[Expected] rs${i}Addr: ${rAddr(i)}")
        //    }
        //  }
        //
        //  val func3: Int = if ((opcode == opcodeMap("R")(0)) || (opcode == opcodeMap("I")(1))) {
        //    Random.nextInt(8)
        //  } else if (opcode == opcodeMap("I")(0)) {
        //    Random.nextInt(7)
        //  } else if (opcode == opcodeMap("S")(0)) {
        //    Random.nextInt(4)
        //  } else if (opcode == opcodeMap("B")(0)) {
        //    var f3: Int = Random.nextInt(8)
        //    while ((f3 == 2) || (f3 == 3)) {
        //      f3 = Random.nextInt(8)
        //    }
        //    f3
        //  } else {
        //    0
        //  }
        //  if (debug) {
        //    println(s"[Expected] func3: ${func3}")
        //  }
        //
        //  val f7Ndx: Int = Random.nextInt(2)
        //  val func7: Int = if (instType == "R") {
        //    f7Array(f7Ndx)
        //  } else {
        //    0
        //  }
        //  if (debug) {
        //    println(s"[Expected] func7: ${func7}")
        //  }
        //
        //  val immParts: Map[String, Seq[Int]] = Map(
        //    "S" -> Seq(
        //      Random.nextInt(),
        //      Random.nextInt()
        //    ),
        //    "B" -> Seq(
        //      Random.nextInt(),
        //      Random.nextInt(),
        //      Random.nextInt(),
        //      Random.nextInt(),
        //      0
        //    ),
        //    "J" -> Seq(
        //      Random.nextInt(),
        //      Random.nextInt(),
        //      Random.nextInt(),
        //      Random.nextInt(),
        //      0
        //    )
        //  )
        //  val immMap: Map[String, Int] = Map(
        //    "R" -> 0,
        //    "I" -> Random.nextInt(4096),
        //    "S" -> (
        //      ((immParts("S")(0) << 5) & (
        //        if ((immParts("S")(0) & 0x00000800) == 0x00000800) {
        //          -0xFFF
        //        } else {
        //          0xFFF
        //        }
        //      )) | (immParts("S")(1))
        //    ),
        //    "B" -> (
        //      ((immParts("B")(0) << 12) & (
        //        if ((immParts("B")(0) & 0x00001000) == 0x00001000) {
        //          -0x1FFF
        //        } else {
        //          0x1FFF
        //        }
        //      )) |
        //      ((immParts("B")(1) << 6)  & 0x1FFF) |
        //      ((immParts("B")(2) << 2)  & 0x1FFF) |
        //      ((immParts("B")(3) << 1)  & 0x1FFF) |
        //      immParts("B")(4)
        //    ),
        //    "U" -> Random.nextInt(20),
        //    "J" -> (
        //      ((immParts("J")(0) << 20) & (
        //        if ((immParts("J")(0) & 0x00100000) == 0x00100000) {
        //          -0x1FFFFF
        //        } else {
        //          0x1FFFFF
        //        }
        //      )) |
        //      ((immParts("J")(1) << 10) & 0x1FFFFF) |
        //      ((immParts("J")(2) << 9)  & 0x1FFFFF) |
        //      ((immParts("J")(3) << 1)  & 0x1FFFFF) |
        //      immParts("J")(4)
        //    )
        //  )
        //  val imm: Int = immMap(instType)
        //  if (debug) {
        //    println(s"[Expected] imm: ${imm}")
        //  }
        //
        //  val inst: Long = if (instType == "R") {
        //    ((func7 << 25)    & 0xFFFFFFFFL) |
        //    ((rAddr(2) << 20) & 0xFFFFFFFFL) |
        //    ((rAddr(1) << 15) & 0xFFFFFFFFL) |
        //    ((func3 << 12)    & 0xFFFFFFFFL) |
        //    ((rAddr(0) << 7)  & 0xFFFFFFFFL) |
        //    opcode
        //  } else if (instType == "I") {
        //    ((imm << 20)      & 0xFFFFFFFFL) |
        //    ((rAddr(1) << 15) & 0xFFFFFFFFL) |
        //    ((func3 << 12)    & 0xFFFFFFFFL) |
        //    ((rAddr(0) << 7)  & 0xFFFFFFFFL) |
        //    opcode
        //  } else if (instType == "S") {
        //    ((immParts("S")(0) << 25) & 0xFFFFFFFFL) |
        //    ((rAddr(2) << 20)         & 0xFFFFFFFFL) |
        //    ((rAddr(1) << 15)         & 0xFFFFFFFFL) |
        //    ((func3 << 12)            & 0xFFFFFFFFL) |
        //    ((immParts("S")(1) << 7)  & 0xFFFFFFFFL) |
        //    opcode
        //  } else if (instType == "B") {
        //    ((immParts("B")(0) << 31) & 0xFFFFFFFFL) |
        //    ((immParts("B")(1) << 25) & 0xFFFFFFFFL) |
        //    ((rAddr(2) << 20)         & 0xFFFFFFFFL) |
        //    ((rAddr(1) << 15)         & 0xFFFFFFFFL) |
        //    ((func3 << 12)            & 0xFFFFFFFFL) |
        //    ((immParts("B")(2) << 8)  & 0xFFFFFFFFL) |
        //    ((immParts("B")(3) << 7)  & 0xFFFFFFFFL) |
        //    opcode
        //  } else if (instType == "U") {
        //    ((imm << 12)     & 0xFFFFFFFFL) |
        //    ((rAddr(0) << 7) & 0xFFFFFFFFL) |
        //    opcode
        //  } else if (instType == "J") {
        //    ((immParts("J")(0) << 31) & 0xFFFFFFFFL) |
        //    ((immParts("J")(1) << 21) & 0xFFFFFFFFL) |
        //    ((immParts("J")(2) << 20) & 0xFFFFFFFFL) |
        //    ((immParts("J")(3) << 12) & 0xFFFFFFFFL) |
        //    ((rAddr(0) << 7)          & 0xFFFFFFFFL) |
        //    opcode
        //  } else {
        //    0L
        //  }
        //  if (debug) {
        //    println(s"[Poked] inst: ${inst}\n")
        //  }
        //
        //  decoder.io.inst.poke(inst.U)
        //
        //  decoder.clock.step(1)
        //
        //  //decoder.io.opcode.expect(opcode.U)
        //  //for (i <- 0 until 3) {
        //  //  decoder.io.rAddr(i).expect(rAddr(i).U)
        //  //}
        //  //decoder.io.func3.expect(func3.U)
        //  //decoder.io.func7.expect(func7.U)
        //  //decoder.io.imm.expect(imm.S)
        //}
    }
  }
}
