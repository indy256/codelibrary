// https://en.wikipedia.org/wiki/Gauss–Jordan_elimination
fun gauss(A: Array<DoubleArray>, B: DoubleArray): DoubleArray {
    val a = A.map { it.copyOf() }.toTypedArray()
    val b = B.copyOf()
    val n = a.size
    for (row in 0 until n) {
        val best = (row until n).maxBy { Math.abs(a[it][row]) }!!
        a[row] = a[best].also { a[best] = a[row] }
        b[row] = b[best].also { b[best] = b[row] }
        for (i in row + 1 until n)
            a[row][i] /= a[row][row]
        b[row] /= a[row][row]
        // a[row][row] = 1;
        for (i in 0 until n) {
            val z = a[i][row]
            if (i != row && z != 0.0) {
                // row + 1 instead of row is an optimization
                for (j in row + 1 until n)
                    a[i][j] -= a[row][j] * z
                b[i] -= b[row] * z
            }
        }
    }
    return b
}

// Usage example
fun main(args: Array<String>) {
    val a = arrayOf(doubleArrayOf(4.0, 2.0, -1.0), doubleArrayOf(2.0, 4.0, 3.0), doubleArrayOf(-1.0, 3.0, 5.0))
    val b = doubleArrayOf(1.0, 0.0, 0.0)
    val x = gauss(a, b)
    for (i in a.indices) {
        val y = a[i].zip(x).map { it -> it.first * it.second }.sum()
        if (Math.abs(b[i] - y) > 1e-9)
            throw RuntimeException()
    }
}
