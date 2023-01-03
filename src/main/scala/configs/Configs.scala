package configs


trait Configs {
  // Core Parameters
  val ext  : String = "rv32i"
  val debug: Boolean = false

  val xlen      : Int = ConfigMaps.params(ext)("XLEN")
  val regAddrLen: Int = ConfigMaps.params(ext)("REG_ADDR_LEN")
  val opcodeLen : Int = ConfigMaps.params(ext)("OPCODE_LEN")
  val f3len     : Int = ConfigMaps.params(ext)("F3LEN")
  val f7len     : Int = ConfigMaps.params(ext)("F7LEN")

  // ISA Constants
  val opcodes : Map[String, Map[String, Int]]              = ConfigMaps.opcodes
  val opID    : Map[String, Map[String, Map[String, Int]]] = ConfigMaps.opID
  val immConf : Seq[String]                                = ConfigMaps.conf("imm")(ext)
  val aluConf : Seq[String]                                = ConfigMaps.conf("alu")(ext)
  val dmemConf: Seq[String]                                = ConfigMaps.conf("dmem")(ext)

  // RVFI Parameters
  val NRET: Int = 1
  val ILEN: Int = 32

  // SRAM Parameters
  val DEPTH: Int = 16
}
