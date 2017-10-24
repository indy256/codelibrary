// https://en.wikipedia.org/wiki/Dijkstra's_algorithm in O(V^2)
object Dijkstra {

  case class Edge(t: Int, cost: Int)

  def shortestPaths(graph: Array[Array[Edge]], s: Int): (Array[Int], Array[Int]) = {
    val n = graph.length
    val pred = Array.fill(n)(-1)
    val prio = Array.fill(n)(Int.MaxValue)
    prio(s) = 0
    val visited = new Array[Boolean](n)
    for (i <- 0 until n) {
      val u = prio.zipWithIndex.filter(x => !visited(x._2)).minBy(_._1)._2
      if (prio(u) != Int.MaxValue) {
        visited(u) = true
        for (e <- graph(u)) {
          val v = e.t
          val nprio = prio(u) + e.cost
          if (prio(v) > nprio) {
            prio(v) = nprio
            pred(v) = u
          }
        }
      }
    }
    (prio, pred)
  }

  // Usage example
  def main(args: Array[String]) {
    val cost = Array.ofDim[Int](3, 3)
    cost(0)(1) = 1
    cost(0)(2) = 5
    cost(1)(2) = 2
    val graph = (for (i <- cost.indices) yield (for (j <- cost.indices; if cost(i)(j) != 0) yield Edge(j, cost(i)(j))).toArray).toArray

    val (dist, pred) = shortestPaths(graph, 0)

    println(graph.map(_.mkString("(", " ", ")")).mkString(" "))
    println(dist.mkString(" "))
    println(pred.mkString(" "))
    println(Array(0, 1, 3) sameElements dist)
    println(Array(-1, 0, 1) sameElements pred)
  }
}
