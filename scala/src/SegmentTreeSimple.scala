object SegmentTreeSimple {

  def get(t: Array[Int], i: Int): Int = t(i + t.length / 2)

  def add(t: Array[Int], i: Int, value: Int) {
    var j = i + t.length / 2
    t(j) += value

    while (j > 1) {
      t(j >> 1) = math.max(t(j), t(j ^ 1))
      j >>= 1
    }
  }

  def max(t: Array[Int], a: Int, b: Int): Int = {
    var res = Int.MinValue
    var i = a + t.length / 2
    var j = b + t.length / 2
    while (i <= j) {
      if ((i & 1) != 0) res = math.max(res, t(i))
      if ((j & 1) == 0) res = math.max(res, t(j))
      i = (i + 1) >> 1
      j = (j - 1) >> 1
    }
    res
  }

  // Usage example
  def main(args: Array[String]) {
    val n = 10
    val t = new Array[Int](n + n)
    add(t, 0, 1)
    add(t, 9, 2)
    println(2 == max(t, 0, 9))
  }
}
