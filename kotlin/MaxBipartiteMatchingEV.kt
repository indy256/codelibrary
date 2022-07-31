// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V * E)
fun maxMatching(graph: Array<out List<Int>>, n2: Int): Int {
    val n1 = graph.size
    val vis = BooleanArray(n1)
    val matching = IntArray(n2) { -1 }

    val findPath = DeepRecursiveFunction { u1 ->
        vis[u1] = true
        for (v in graph[u1]) {
            val u2 = matching[v]
            if (u2 == -1 || !vis[u2] && callRecursive(u2) == 1) {
                matching[v] = u1
                return@DeepRecursiveFunction 1
            }
        }
        0
    }

    return (0 until n1).sumOf { vis.fill(false); findPath(it) }
}

// Usage example
fun main() {
    val g = (1..2).map { arrayListOf<Int>() }.toTypedArray()
    g[0].add(0)
    g[0].add(1)
    g[1].add(1)
    println(2 == maxMatching(g, 2))
}
