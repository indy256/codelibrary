object TopologicalSort {

    fun topologicalSort(graph: Array<out List<Int>>): List<Int> {

        fun dfs(graph: Array<out List<Int>>, used: BooleanArray, order: MutableList<Int>, u: Int) {
            used[u] = true
            for (v in graph[u])
                if (!used[v])
                    dfs(graph, used, order, v)
            order.add(u)
        }

        val n = graph.size
        val used = BooleanArray(n)
        val order = mutableListOf<Int>()
        for (i in 0..n - 1)
            if (!used[i])
                dfs(graph, used, order, i)

        order.reverse()
        return order
    }

    // Usage example
    @JvmStatic fun main(args: Array<String>) {
        val g = (1..3).map({ mutableListOf<Int>() }).toTypedArray()
        g[2].add(0)
        g[2].add(1)
        g[0].add(1)

        val order = topologicalSort(g)
        println(order)
    }
}