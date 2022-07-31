fun maxFlow(cap: Array<IntArray>, s: Int, t: Int): Int {
    val vis = BooleanArray(cap.size)

    val findPath = DeepRecursiveFunction { i ->
        if (i == t)
            return@DeepRecursiveFunction true
        vis[i] = true
        for (j in vis.indices)
            if (!vis[j] && cap[i][j] > 0 && callRecursive(j)) {
                --cap[i][j]
                ++cap[j][i]
                return@DeepRecursiveFunction true
            }
        false
    }

    return generateSequence(0) { it + 1 }.find { vis.fill(false); !findPath(s) }!!
}

// Usage example
fun main() {
    val capacity = arrayOf(
        intArrayOf(0, 1, 1, 0),
        intArrayOf(1, 0, 1, 1),
        intArrayOf(1, 1, 0, 1),
        intArrayOf(0, 1, 1, 0)
    )
    println(2 == maxFlow(capacity, 0, 3))
}
