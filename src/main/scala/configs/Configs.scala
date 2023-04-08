package xodus.configs


trait Configs {
  val variant: String = "32"
  val ext    : String = "i"

  val Debug: Boolean = false

  val XLEN        : Int = ConfigMaps.variables(ConfigMaps.extensions.indexOf(ext))("XLEN")(variant)
  val RegAddrWidth: Int = ConfigMaps.variables(ConfigMaps.extensions.indexOf(ext))("RegAddrWidth")(variant)
}
