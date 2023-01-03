//package execute_stage
//
//import chisel3._, chisel3.util._
//
//
//class ALUIO(
//  params :Map[String, Int],
//  enNum  :Int
//) extends Bundle {
//  // Input pins
//  val operands: Vec[SInt] = Input(Vec(3, SInt(params("XLEN").W)))
//  val pc      : UInt      = Input(UInt(params("XLEN").W))
//  val en      : Vec[Bool] = Input(Vec(enNum, Bool()))
//
//  // Output pins
//  val out: SInt = Output(SInt(32.W))
//}
//
//
//class ALU(
//  params  :Map[String, Int],
//  confAlu :Seq[String],
//  debug   :Boolean
//) extends Module {
//  val enNum: Int = confAlu.length
//  val io: ALUIO = IO(new ALUIO(
//    params = params,
//    enNum  = enNum
//  ))
//
//  // Wires
//  val sintWires: Map[String, SInt] = Map(
//    "imm"     -> io.operands(0),
//    "rs1Data" -> io.operands(1),
//    "rs2Data" -> io.operands(2)
//  )
//
//  val enWires: Map[String, Bool] = (
//    for (i <- 0 until enNum)
//      yield confAlu(i) -> io.en(i)
//  ).toMap
//
//  val operands: Map[String, SInt] = Map(
//    "immU" -> (sintWires("imm") << 12.U),
//    "1"    -> sintWires("rs1Data"),
//    "2"    -> Mux(
//      enWires(confAlu(enNum - 2)),
//      sintWires("imm"),
//      sintWires("rs2Data")
//    )
//  )
//
//  val op: Map[String, SInt] = Map(
//    confAlu(0)  -> (operands("1") + operands("2")),
//    confAlu(1)  -> (operands("1") - operands("2")),
//    confAlu(2)  -> (operands("1") << operands("2")(4, 0)),
//    confAlu(3)  -> (operands("1") < operands("2")).asSInt,
//    confAlu(4)  -> (operands("1").asUInt < operands("2").asUInt).asSInt,
//    confAlu(5)  -> (operands("1") ^ operands("2")),
//    confAlu(6)  -> (operands("1").asUInt >> operands("2")(4, 0)).asSInt,
//    confAlu(7)  -> (operands("1") >> operands("2")(4, 0)).asSInt,
//    confAlu(8)  -> (operands("1") | operands("2")),
//    confAlu(9)  -> (operands("1") & operands("2")),
//    confAlu(10) -> (io.pc + operands("immU").asUInt).asSInt,
//    confAlu(11) -> operands("immU"),
//    confAlu(13) -> (io.pc.asSInt + 4.S)
//  )
//
//  // Connections
//  val opConn: Seq[(Bool, SInt)] = (
//    for (i <- 0 until enNum - 2)
//      yield enWires(confAlu(i)) -> op(confAlu(i))
//  ) ++ Seq(
//    enWires(confAlu(enNum - 1)) -> op(confAlu(enNum - 1))
//  )
//  io.out := MuxCase(0.S, opConn)
//
//    // Intermediate wires
//    //val addition            : SInt = dontTouch(WireInit(operand1 + operand2))
//    //val lessThan            : SInt = dontTouch(WireInit((operand1 < operand2).asSInt))
//    //val lessThanU           : SInt = dontTouch(WireInit((operand1.asUInt < operand2.asUInt).asSInt))
//    //val XOR                 : SInt = dontTouch(WireInit(operand1 ^ operand2))
//    //val OR                  : SInt = dontTouch(WireInit(operand1 | operand2))
//    //val AND                 : SInt = dontTouch(WireInit(operand1 & operand2))
//    //val shiftLeftLogical    : SInt = dontTouch(WireInit((operand1 << operand2(4, 0)).asSInt))
//    //val shiftRightLogical   : SInt = dontTouch(WireInit((operand1.asUInt >> operand2(4, 0)).asSInt))
//    //val shiftRightArithmetic: SInt = dontTouch(WireInit((operand1 >> operand2(4, 0)).asSInt))
//    //val subtraction         : SInt = dontTouch(WireInit(operand1 - operand2))
//    //val pc4                 : SInt = dontTouch(WireInit((pc + 4.U).asSInt))
//    //val pc4_en              : Bool = dontTouch(WireInit(jalr_en || jal_en))
//    //val u_imm               : SInt = dontTouch(WireInit(imm << 12.U))
//    //val auipc               : SInt = dontTouch(WireInit((pc + u_imm.asUInt).asSInt))
//    //val lui                 : SInt = dontTouch(WireInit(u_imm))
//
//    //// Wiring to output pins
//    //io.out := MuxCase(0.S, Seq(
//    //    addition_en             -> addition,
//    //    shiftLeftLogical_en     -> shiftLeftLogical,
//    //    lessThan_en             -> lessThan,
//    //    lessThanU_en            -> lessThanU,
//    //    XOR_en                  -> XOR,
//    //    shiftRightLogical_en    -> shiftRightLogical,
//    //    shiftRightArithmetic_en -> shiftRightArithmetic,
//    //    OR_en                   -> OR,
//    //    AND_en                  -> AND,
//    //    subtraction_en          -> subtraction,
//    //    pc4_en                  -> pc4,
//    //    auipc_en                -> auipc,
//    //    lui_en                  -> lui
//    //))
//}
