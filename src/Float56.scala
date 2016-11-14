class Float56(val float64: Double) {

  val long64 = java.lang.Double.doubleToRawLongBits(float64).toBinaryString
  val raw64 = "0" * (64 - long64.length) + long64
  val sign = raw64(0)
  val rawExponent64 = raw64.substring(1, 12)
  val rawMantissa64 = raw64.substring(12)
  val rawExponent56 = Float56.rawExponent(rawExponent64)
  val rawMantissa56 = Float56.rawMantissa(rawMantissa64)

  override def toString = s"$sign $rawExponent56 $rawMantissa56 [$float64]"
}

object Float56 {
  def apply(float64: Double) = new Float56(float64)

  def rawExponent(rawExponent64: String): String = {
    val exponent64 = Integer.parseInt(rawExponent64, 2)
    if (exponent64 == 0) return "0" * 7
    val bias64 = 1023
    val exponent = exponent64 - bias64
    val bias56 = (1 << 6) - 1
    var exponent56 = exponent + bias56
    if (exponent == 1024) exponent56 = (1 << 7) - 1
    val rawExponent56 = Integer.toBinaryString(exponent56)
    "0" * (7 - rawExponent56.length) + rawExponent56
  }

  def rawMantissa(rawMantissa64: String) = rawMantissa64.substring(0, 48)
}