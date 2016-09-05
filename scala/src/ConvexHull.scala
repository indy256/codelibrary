object ConvexHull {

  type Point = (Int, Int)

  def convexHull(points: Array[Point]): Array[Point] = {
    val n = points.length
    val sortedPoints = points.sorted
    val hull = new Array[Point](n + 1)
    var cnt = 0
    for (i <- 0 until 2 * n) {
      val j = if (i < n) i else 2 * n - 1 - i
      while (cnt >= 2 && isNotRightTurn(hull(cnt - 2), hull(cnt - 1), sortedPoints(j))) {
        cnt -= 1
      }
      hull(cnt) = sortedPoints(j)
      cnt += 1
    }
    hull.slice(0, cnt - 1)
  }

  def isNotRightTurn(a: Point, b: Point, c: Point): Boolean = {
    val cross = (a._1 - b._1).toLong * (c._2 - b._2) - (a._2 - b._2).toLong * (c._1 - b._1)
    val dot = (a._1 - b._1).toLong * (c._1 - b._1) + (a._2 - b._2).toLong * (c._2 - b._2)
    cross < 0 || cross == 0 && dot <= 0
  }

  // Usage example
  def main(args: Array[String]) {
    val points = Array((0,0), (2,2), (1,1), (0, 1), (1, 0), (0,0))
    val hull = convexHull(points)
    println(hull.mkString(" "))
  }
}
