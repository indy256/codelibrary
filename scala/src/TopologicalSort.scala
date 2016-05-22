import java.util.Collections

object TopologicalSort {

  def dfs(graph: Array[util.List[Integer]], used: Array[Boolean], order: util.List[Integer], u: Int) {
    used(u) = true
    import scala.collection.JavaConversions._
    for (v <- graph(u)) if (!used(v)) dfs(graph, used, order, v)
    order.add(u)
  }

  def topologicalSort(graph: Array[util.List[Integer]]): util.List[Integer] = {
    val n: Int = graph.length
    val used: Array[Boolean] = new Array[Boolean](n)
    val order: util.List[Integer] = new util.ArrayList[Integer]
    {
      var i: Int = 0
      while (i < n) {
        if (!used(i)) dfs(graph, used, order, i)
        ({
          i += 1; i - 1
        })
      }
    }
    Collections.reverse(order)
    return order
  }

  // Usage example
  def main(args: Array[String]) {
  }
}

