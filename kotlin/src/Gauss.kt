import java.util.Random

// https://en.wikipedia.org/wiki/Gauss–Jordan_elimination
// returns x such that A * x = b
fun gauss2(A: Array<DoubleArray>, b: DoubleArray): DoubleArray {
    val a = A.mapIndexed { i, Ai -> Ai + b[i] }.toTypedArray()
    val n = a.size
    for (i in 0 until n) {
        val best = (i until n).maxBy { Math.abs(a[it][i]) }!!
        a[i] = a[best].also { a[best] = a[i] }
        for (j in i + 1..n)
            a[i][j] /= a[i][i]
        for (j in 0 until n) {
            val z = a[j][i]
            if (j != i && z != 0.0) {
                for (k in i + 1..n)
                    a[j][k] -= a[i][k] * z
            }
        }
    }
    return a.map { it[n] }.toDoubleArray()
}

// random test
fun main(args: Array<String>) {
    val rnd = Random(1)
    for (step in 0..9999) {
        val n = rnd.nextInt(5) + 1
        val a = (0 until n).map { (0 until n).map { (rnd.nextInt(10) - 5).toDouble() }.toDoubleArray() }.toTypedArray()
        if (Math.abs(det(a)) > 1e-6) {
            val b = (0 until n).map { (rnd.nextInt(10) - 5).toDouble() }.toDoubleArray()
            val x = gauss2(a, b)
            for (i in a.indices) {
                val y = a[i].zip(x).map { it -> it.first * it.second }.sum()
                if (Math.abs(b[i] - y) > 1e-9)
                    throw RuntimeException()
            }
        }
    }
}
