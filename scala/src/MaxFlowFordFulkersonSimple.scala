// https://en.wikipedia.org/wiki/Ford–Fulkerson_algorithm in O(V^2 * flow)
object MaxFlowFordFulkersonSimple {

  def maxFlow(cap: Array[Array[Int]], s: Int, t: Int): Int = {
    // use streams
    var flow = 0
    while (augmentPath(cap, new Array[Boolean](cap.length), s, t)) {
      flow += 1
    }
    flow
  }

  def augmentPath(cap: Array[Array[Int]], vis: Array[Boolean], i: Int, t: Int): Boolean = {
    if (i == t) return true
    vis(i) = true
    for (j <- vis.indices) {
      if (!vis(j) && cap(i)(j) > 0 && augmentPath(cap, vis, j, t)) {
        cap(i)(j) -= 1
        cap(j)(i) += 1
        return true
      }
    }
    false
  }

  // Usage example
  def main(args: Array[String]) {
    val capacity = Array(Array(0, 1, 1, 0), Array(1, 0, 1, 1), Array(1, 1, 0, 1), Array(0, 1, 1, 0))
    println(2 == maxFlow(capacity, 0, 3))
  }
}
