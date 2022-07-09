// https://en.wikipedia.org/wiki/Dinic%27s_algorithm in O(V^2 * E)
class MaxFlowDinic(nodes: Int) {

    data class Edge(val t: Int, val rev: Int, val cap: Int, var f: Int = 0)

    val graph = (1..nodes).map { arrayListOf<Edge>() }.toTypedArray()
    val dist = IntArray(nodes)

    fun addBidiEdge(s: Int, t: Int, cap: Int) {
        graph[s].add(Edge(t, graph[t].size, cap))
        graph[t].add(Edge(s, graph[s].size - 1, 0))
    }

    fun dinicBfs(src: Int, dest: Int, dist: IntArray): Boolean {
        dist.fill(-1)
        dist[src] = 0
        val q = IntArray(graph.size)
        var sizeQ = 0
        q[sizeQ++] = src
        var i = 0
        while (i < sizeQ) {
            val u = q[i++]
            for (e in graph[u]) {
                if (dist[e.t] < 0 && e.f < e.cap) {
                    dist[e.t] = dist[u] + 1
                    q[sizeQ++] = e.t
                }
            }
        }
        return dist[dest] >= 0
    }

    fun dinicDfs(ptr: IntArray, dest: Int, u: Int, f: Int): Int {
        if (u == dest)
            return f
        while (ptr[u] < graph[u].size) {
            val e = graph[u][ptr[u]]
            if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
                val df = dinicDfs(ptr, dest, e.t, Math.min(f, e.cap - e.f))
                if (df > 0) {
                    e.f += df
                    graph[e.t][e.rev].f -= df
                    return df
                }
            }
            ++ptr[u]
        }
        return 0
    }

    fun maxFlow(src: Int, dest: Int): Int {
        var flow = 0
        while (dinicBfs(src, dest, dist)) {
            val ptr = IntArray(graph.size)
            while (true) {
                val df = dinicDfs(ptr, dest, src, Int.MAX_VALUE)
                if (df == 0)
                    break
                flow += df
            }
        }
        return flow
    }

    // invoke after maxFlow()
    fun minCut() = dist.map { it != -1 }.toBooleanArray()

    fun clearFlow() {
        for (edges in graph)
            for (edge in edges)
                edge.f = 0
    }
}

// Usage example
fun main(args: Array<String>) {
    val flow = MaxFlowDinic(3)
    flow.addBidiEdge(0, 1, 3)
    flow.addBidiEdge(0, 2, 2)
    flow.addBidiEdge(1, 2, 2)
    println(4 == flow.maxFlow(0, 2))
}
