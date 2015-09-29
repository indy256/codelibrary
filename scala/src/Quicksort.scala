object Quicksort {

  // functional implementation. slow
  def quicksort(xs: Array[Int]): Array[Int] =
    if (xs.length <= 1) xs
    else quicksort(xs filter (xs(0) >)) ++ (xs filter (xs(0) ==)) ++ quicksort(xs filter (xs(0) <))

  def quicksort_(xs: Array[Int]): Array[Int] = xs match {
    case Array() => xs
    case Array(x, s@_*) => quicksort(Array(s: _*) filter (x >=)) ++ Array(x) ++ quicksort(Array(s: _*) filter (x <))
  }

  // imperative implementation. fast
  def quicksort2(a: Array[Int]) {
    def swap(i: Int, j: Int) {
      val t = a(i)
      a(i) = a(j)
      a(j) = t
    }
    def sort(l: Int, r: Int) {
      val pivot = a((l + r) / 2)
      var i = l
      var j = r
      while (i <= j) {
        while (a(i) < pivot) i += 1
        while (a(j) > pivot) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (l < j) sort(l, j)
      if (j < r) sort(i, r)
    }
    sort(0, a.length - 1)
  }

  // test
  def main(args: Array[String]) {
    val N = 100000
    var a = scala.util.Random.shuffle(0 until N: Seq[Int]).toArray

    val start = System.currentTimeMillis
    a = quicksort(a)
    // quicksort2(a)
    println(System.currentTimeMillis - start)

    (0 until N).foreach(i => assert(i == a(i)))
  }
}
