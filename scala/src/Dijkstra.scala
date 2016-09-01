import java.util

import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm in O(V^2)
object Dijkstra {

  case class Edge(t: Int, cost: Int)

  def shortestPaths(graph: Array[ArrayBuffer[Edge]], s: Int, prio: Array[Int], pred: Array[Int]) {
    val n = graph.length
    util.Arrays.fill(pred, -1)
    util.Arrays.fill(prio, Int.MaxValue)
    prio(s) = 0
    val visited = new Array[Boolean](n)
    Breaks.breakable {
      for (i <- 0 until n) {
        var u = -1
        for (j <- 0 until n)
          if (!visited(j) && (u == -1 || prio(u) > prio(j)))
            u = j
        if (prio(u) == Int.MaxValue)
          Breaks.break
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
  }

  // Usage example
  def main(args: Array[String]) {
    val cost = Array(
      Array(0, 3, 2),
      Array(0, 0, -2),
      Array(0, 0, 0))
    val n = cost.length
    val graph = Array.fill(n)(ArrayBuffer[Edge]())
    for (i <- 0 until n) {
      for (j <- 0 until n) {
        if (cost(i)(j) != 0) graph(i) += Edge(j, cost(i)(j))
      }
    }
    print(graph mkString " ")
    val dist = new Array[Int](n)
    val pred = new Array[Int](n)
    shortestPaths(graph, 0, dist, pred)
    println(0 == dist(0))
    println(3 == dist(1))
    println(1 == dist(2))
    println(-1 == pred(0))
    println(0 == pred(1))
    println(1 == pred(2))
  }
}
