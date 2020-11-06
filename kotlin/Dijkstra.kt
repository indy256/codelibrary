object Dijkstra {
    data class Edge(val target: Int, val cost: Int)

    data class ShortestPaths(val dist: IntArray, val pred: IntArray)

    fun shortestPaths(graph: Array<out List<Edge>>, s: Int): ShortestPaths {
        val n = graph.size
        val dist = IntArray(n) { Int.MAX_VALUE }
        dist[s] = 0
        val pred = IntArray(n) { -1 }
        val visited = BooleanArray(n)
        for (i in 0 until n) {
            var u = -1
            for (j in 0 until n) {
                if (!visited[j] && (u == -1 || dist[u] > dist[j]))
                    u = j
            }
            if (dist[u] == Int.MAX_VALUE)
                break
            visited[u] = true

            for (e in graph[u]) {
                val v = e.target
                val nprio = dist[u] + e.cost
                if (dist[v] > nprio) {
                    dist[v] = nprio
                    pred[v] = u
                }
            }
        }
        return ShortestPaths(dist, pred)
    }

    // Usage example
    @JvmStatic
    fun main(args: Array<String>) {
        val cost = arrayOf(intArrayOf(0, 3, 2), intArrayOf(0, 0, -2), intArrayOf(0, 0, 0))
        val n = cost.size
        val graph = (1..n).map { arrayListOf<Edge>() }.toTypedArray()
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (cost[i][j] != 0) {
                    graph[i].add(Edge(j, cost[i][j]))
                }
            }
        }
        println(graph.contentToString())
        val (dist, pred) = shortestPaths(graph, 0)
        println(0 == dist[0])
        println(3 == dist[1])
        println(1 == dist[2])
        println(-1 == pred[0])
        println(0 == pred[1])
        println(1 == pred[2])
    }
}
