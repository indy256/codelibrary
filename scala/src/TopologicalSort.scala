import scala.collection.mutable.ListBuffer

object TopologicalSort {
  def dfs(graph: Array[ListBuffer[Int]], used: Array[Boolean], order: ListBuffer[Int], u: Int) {
    used(u) = true
    for (v <- graph(u)) {
      if (!used(v)) {
        dfs(graph, used, order, v)
      }
    }
    u +=: order
  }

  def topologicalSort(graph: Array[ListBuffer[Int]]): List[Int] = {
    val n: Int = graph.length
    val used = new Array[Boolean](n)
    val order = ListBuffer[Int]()

    for (i <- 0 until n) {
      if (!used(i)) {
        dfs(graph, used, order, i)
      }
    }

    order.toList
  }

  // Usage example
  def main(args: Array[String]) {
    val g = new Array[ListBuffer[Int]](3)
    for (i <- g.indices)
      g(i) = ListBuffer()

    g(2) += 0
    g(2) += 1
    g(0) += 1

    val order: List[Int] = topologicalSort(g)
    print(order)
  }
}
