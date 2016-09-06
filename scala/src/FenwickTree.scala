object FenwickTree {

  // T[i] += value
  def add(t: Array[Int], i: Int, value: Int) {
    var j = i
    while (j < t.length) {
      t(j) += value
      j |= j + 1
    }
  }

  // sum[0..i]
  def sum(t: Array[Int], i: Int): Int = {
    var res: Int = 0
    var j = i
    while (j >= 0) {
      res += t(j)
      j = (j & (j + 1)) - 1
    }
    res
  }

  // Usage example
  def main(args: Array[String]) {
    val t = new Array[Int](10)
    add(t, 0, 1)
    add(t, 9, -2)
    println(-1 == sum(t, 9))
  }
}
