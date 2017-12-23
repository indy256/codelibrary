object TopologicalSort {

    fun topologicalSort(graph: Array<out List<Int>>): List<Int> {
        val n = graph.size
        val used = BooleanArray(n)
        val order = mutableListOf<Int>()

        fun dfs(u: Int) {
            used[u] = true
            graph[u].filter { !used[it] }.forEach { dfs(it) }
            order.add(u)
        }

        (0 until n).filter { !used[it] }.forEach { dfs(it) }
        order.reverse()
        return order
    }
}

// Usage example
fun main(args: Array<String>) {
    val g = (1..3).map({ mutableListOf<Int>() }).toTypedArray()
    g[2].add(0)
    g[2].add(1)
    g[0].add(1)

    val order = TopologicalSort.topologicalSort(g)
    println(order)
}
