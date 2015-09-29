object Euclid {
  def gcd(a: Long, b: Long): Long = if (b == 0) a else gcd(b, a % b)

  // Usage example
  def main(args: Array[String]) {
    println(gcd(6, 9))
  }
}
