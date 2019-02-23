fun maxFlow(cap: Array<IntArray>, s: Int, t: Int): Int {

    fun findPath(i: Int, vis: BooleanArray = BooleanArray(cap.size)): Boolean {
        if (i == t)
            return true
        vis[i] = true
        for (j in vis.indices)
            if (!vis[j] && cap[i][j] > 0 && findPath(j, vis)) {
                --cap[i][j]
                ++cap[j][i]
                return true
            }
        return false
    }

    return generateSequence(0) { it + 1 }.find { !findPath(s) }!!
}

// Usage example
fun main() {
    val capacity = arrayOf(
            intArrayOf(0, 1, 1, 0),
            intArrayOf(1, 0, 1, 1),
            intArrayOf(1, 1, 0, 1),
            intArrayOf(0, 1, 1, 0))
    println(2 == maxFlow(capacity, 0, 3))
}
