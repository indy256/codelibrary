import kotlin.math.abs

// https://en.wikipedia.org/wiki/Determinant
fun det(matrix: Array<DoubleArray>): Double {
    val EPS = 1e-10
    val a = matrix.map { it.copyOf() }.toTypedArray()
    val n = a.size
    var res = 1.0
    for (i in 0 until n) {
        val p = (i until n).maxByOrNull { abs(a[it][i]) }!!
        if (abs(a[p][i]) < EPS)
            return 0.0
        if (i != p) {
            res = -res
            a[i] = a[p].also { a[p] = a[i] }
        }
        res *= a[i][i]
        for (j in i + 1 until n)
            a[i][j] /= a[i][i]
        for (j in 0 until n)
            if (j != i && abs(a[j][i]) > EPS /*optimizes overall complexity to O(n^2) for sparse matrices*/)
                for (k in i + 1 until n)
                    a[j][k] -= a[i][k] * a[j][i]
    }
    return res
}

// Usage example
fun main() {
    val d = det(arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(-1.0, 0.0)))
    println(abs(d - 1) < 1e-10)
}
