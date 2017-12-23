object MaxFlowFordFulkersonSimple {

    fun maxFlow(cap: Array<IntArray>, s: Int, t: Int): Int {

        fun findPath(vis: BooleanArray, i: Int): Boolean {
            if (i == t)
                return true
            vis[i] = true
            for (j in vis.indices)
                if (!vis[j] && cap[i][j] > 0 && findPath(vis, j)) {
                    --cap[i][j]
                    ++cap[j][i]
                    return true
                }
            return false
        }

        return generateSequence(0) { it + 1 }.find { !findPath(BooleanArray(cap.size), s) }!!
    }

    // Usage example
    @JvmStatic
    fun main(args: Array<String>) {
        val capacity = arrayOf(
                intArrayOf(0, 1, 1, 0),
                intArrayOf(1, 0, 1, 1),
                intArrayOf(1, 1, 0, 1),
                intArrayOf(0, 1, 1, 0))
        println(2 == maxFlow(capacity, 0, 3))
    }
}
