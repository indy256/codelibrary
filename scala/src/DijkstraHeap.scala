import scala.collection.mutable

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm in O(E*log(V))
object DijkstraHeap {

  case class Edge(var t: Int, var cost: Int)

  def shortestPaths(graph: Array[Array[Edge]], s: Int): (Array[Int], Array[Int]) = {
    val n = graph.length
    val pred = Array.fill(n)(-1)
    val prio = Array.fill(n)(Int.MaxValue)
    prio(s) = 0
    val q = new mutable.PriorityQueue[Long]
    q += s
    while (!q.isEmpty) {
      val cur: Long = q.dequeue()
      val curu = cur.toInt
      if ((cur >>> 32) == prio(curu)) {
        for (e <- graph(curu)) {
          val v = e.t
          val nprio = prio(curu) + e.cost
          if (prio(v) > nprio) {
            prio(v) = nprio
            pred(v) = curu
            q += (nprio.toLong << 32) + v
          }
        }
      }
    }
    (prio, pred)
  }

  // Usage example
  def main(args: Array[String]) {
    val cost = Array.ofDim[Int](4, 4)
    cost(0)(1) = 1
    cost(0)(2) = 2
    cost(0)(3) = 4
    cost(3)(2) = -2
    cost(2)(1) = -2
    val graph = (for (i <- cost.indices) yield (for (j <- cost.indices; if cost(i)(j) != 0) yield Edge(j, cost(i)(j))).toArray).toArray

    val (dist, pred) = shortestPaths(graph, 0)

    println(graph.map(_.mkString("(", " ", ")")).mkString(" "))
    println(dist.mkString(" "))
    println(pred.mkString(" "))
    println(Array(0, 0, 2, 4) sameElements dist)
    println(Array(-1, 2, 0, 0) sameElements pred)
  }
}
