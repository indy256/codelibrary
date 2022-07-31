// https://en.wikipedia.org/wiki/Topological_sorting
fun topologicalSort(graph: Array<out List<Int>>): List<Int> {
    val n = graph.size
    val used = BooleanArray(n)
    val order = arrayListOf<Int>()

    val dfs = DeepRecursiveFunction { u ->
        used[u] = true
        graph[u].forEach { if (!used[it]) callRecursive(it) }
        order.add(u)
    }

    (0 until n).forEach { if (!used[it]) dfs(it) }
    order.reverse()
    return order
}

// Usage example
fun main() {
    val graph = (1..3).map { arrayListOf<Int>() }.toTypedArray()
    graph[2].add(0)
    graph[2].add(1)
    graph[0].add(1)

    val order = topologicalSort(graph)
    println(order)
}
