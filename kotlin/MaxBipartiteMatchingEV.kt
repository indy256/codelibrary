// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V * E)
fun maxMatching(graph: Array<out List<Int>>): Int {
    val n1 = graph.size
    val n2 = (graph.flatMap { it }.maxOrNull() ?: -1) + 1
    val matching = IntArray(n2) { -1 }
    return (0 until n1).sumOf { findPath(graph, it, matching, BooleanArray(n1)) }
}

fun findPath(graph: Array<out List<Int>>, u1: Int, matching: IntArray, vis: BooleanArray): Int {
    vis[u1] = true
    for (v in graph[u1]) {
        val u2 = matching[v]
        if (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis) == 1) {
            matching[v] = u1
            return 1
        }
    }
    return 0
}

// Usage example
fun main(args: Array<String>) {
    val g = (1..2).map { arrayListOf<Int>() }.toTypedArray()
    g[0].add(0)
    g[0].add(1)
    g[1].add(1)
    println(2 == maxMatching(g))
}
