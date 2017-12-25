import java.util.Random

// https://en.wikipedia.org/wiki/Gauss–Jordan_elimination
// returns X such that A * X = B
fun gauss(A: Array<DoubleArray>, B: DoubleArray): DoubleArray {
    val a = A.map { it.copyOf() }.toTypedArray()
    val b = B.copyOf()
    val n = a.size
    for (row in 0 until n) {
        val best = (row until n).maxBy { Math.abs(a[it][row]) }!!
        a[row] = a[best].also { a[best] = a[row] }
        b[row] = b[best].also { b[best] = b[row] }
        for (j in row + 1 until n)
            a[row][j] /= a[row][row]
        b[row] /= a[row][row]
        // a[row][row] = 1;
        for (j in 0 until n) {
            val z = a[j][row]
            if (j != row && z != 0.0) {
                // row + 1 instead of row is an optimization
                for (k in row + 1 until n)
                    a[j][k] -= a[row][k] * z
                b[j] -= b[row] * z
            }
        }
    }
    return b
}

// random test
fun main(args: Array<String>) {
    val rnd = Random(1)
    for (step in 0..9999) {
        val n = rnd.nextInt(5) + 1
        val a = (0 until n).map { (0 until n).map { (rnd.nextInt(10) - 5).toDouble() }.toDoubleArray() }.toTypedArray()
        if (Math.abs(det(a)) > 1e-6) {
            val b = (0 until n).map { (rnd.nextInt(10) - 5).toDouble() }.toDoubleArray()
            val x = gauss(a, b)
            for (i in a.indices) {
                val y = a[i].zip(x).map { it -> it.first * it.second }.sum()
                if (Math.abs(b[i] - y) > 1e-9)
                    throw RuntimeException()
            }
        }
    }
}
