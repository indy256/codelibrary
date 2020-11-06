fun nextPermutation(p: IntArray): Boolean {
    val a = (0 until p.size - 1).lastOrNull { p[it] < p[it + 1] } ?: return false
    val b = (a + 1 until p.size).last { p[a] < p[it] }
    p[a] = p[b].also { p[b] = p[a] }
    p.reverse(a + 1, p.size)
    return true
}

// Usage example
fun main() {
    val p = intArrayOf(0, 1, 2)
    do {
        println(p.contentToString())
    } while (nextPermutation(p))
}
