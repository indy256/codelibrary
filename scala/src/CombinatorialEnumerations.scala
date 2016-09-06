object CombinatorialEnumerations {

  // subclass and implement count() method
  abstract class AbstractEnumeration protected(// range of definition of sequence elements
                                               val range: Int, // length of generated sequences
                                               val length: Int) {
    // returns number of combinatorial sequences starting with prefix
    // by contract only the last element of prefix can be invalid and in this case 0 must be returned
    protected def count(prefix: Array[Int]): Long

    def next(sequence: Array[Int]): Array[Int] = fromNumber(toNumber(sequence) + 1)

    def totalCount: Long = count(new Array(0))

    def toNumber(sequence: Array[Int]): Long = sequence.indices.map(i => (0 until sequence(i)).map(d => count(sequence.slice(0, i) ++ Array(d))).sum).sum

    def fromNumber(number: Long): Array[Int] = {
      var cur = number
      val sequence = new Array[Int](length)
      for (i <- sequence.indices) {
        val prefix: Array[Int] = sequence.slice(0, i)

        def f(d: Int, cur: Long): (Int, Long) = {
          val cnt = count(prefix ++ Array(d))
          if (d == range || cur < cnt) (d, cur) else f(d + 1, cur - cnt)
        }

        val (d, cnt) = f(0, cur)
        sequence(i) = d
        cur = cnt
      }
      sequence
    }

    def enumerate() {
      println(getClass.getName)
      for (i <- 0 until totalCount.toInt) {
        val p = fromNumber(i)
        println(i + " " + p.mkString(","))
        val j = toNumber(p)
        if (i != j) throw new RuntimeException
      }
    }
  }

  class Arrangements(val n: Int, val k: Int) extends AbstractEnumeration(n, k) {
    protected def count(prefix: Array[Int]): Long = {
      val size = prefix.length

      // if the last element appears twice, then prefix is invalid and 0 must be returned
      if ((0 until size - 1).exists(i => prefix(i) == prefix(size - 1))) {
        return 0
      }

      (0 until length - size).map(i => (range - size - i).toLong).product
    }
  }

  // Usage example
  def main(args: Array[String]) {
    val arrangements = new Arrangements(3, 2)
    arrangements.enumerate()
  }
}
