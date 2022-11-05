package ParamsAndConsts

import chisel3._
import chisel3.util._

object Consts {
    // Opcodes
    val OPCODES: Map[String, Map[String, UInt]] = Map(
        "R" -> Map(
            "math" -> "b0110011".U/*,
            "mathw" -> "b0".U*/
        ),
        "S" -> Map("S" -> "b0100011".U),
        "B" -> Map("B" -> "b1100011".U),
        "J" -> Map("J" -> "b1101111".U),
        "I" -> Map(
            "math"  -> "b0010011".U,
            "load"  -> "b0000011".U,
            "fence" -> "b0001111".U,
            "mathw" -> "b0011011".U,
            "jalr"  -> "b1100111".U,
            "csr"   -> "b1110011".U
        ),
        "U" -> Map(
            "auipc" -> "b0010111".U,
            "lui"   -> "b0110111".U
        )
    )

    val FUNC3: Map[String, UInt] = Map()

    //val OP: Map[String, Map[String, UInt]] = Map(
    //    "R" -> Map(
    //        "math" -> Map(
    //            "add" -> Cat()
    //          )
    //    )
    //)
}
