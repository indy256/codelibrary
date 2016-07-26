import scala.collection.mutable.ArrayBuffer

object TopologicalSort {
  def dfs(graph: Array[ArrayBuffer[Int]], used: Array[Boolean], order: ArrayBuffer[Int], u: Int) {
    used(u) = true
    for (v <- graph(u); if !used(v))
      dfs(graph, used, order, v)
    order += u
  }

  def topologicalSort(graph: Array[ArrayBuffer[Int]]): Array[Int] = {
    val n: Int = graph.length
    val used = new Array[Boolean](n)
    val order = ArrayBuffer[Int]()

    for (i <- 0 until n; if !used(i))
      dfs(graph, used, order, i)

    order.reverse.toArray
  }

  // Usage example
  def main(args: Array[String]) {
    val g: Array[ArrayBuffer[Int]] = Array.fill(3)(ArrayBuffer())

    g(2) += 0
    g(2) += 1
    g(0) += 1

    val order: Array[Int] = topologicalSort(g)
    print(order.mkString(" "))
  }
}
