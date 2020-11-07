import kotlin.math.abs
import java.util.Random

// https://en.wikipedia.org/wiki/Gauss–Jordan_elimination
// returns x such that A * x = b. requires |A| > 0
fun gauss(A: Array<DoubleArray>, b: DoubleArray): DoubleArray {
    val a = A.mapIndexed { i, Ai -> Ai + b[i] }.toTypedArray()
    val n = a.size
    for (i in 0 until n) {
        val pivot = (i until n).maxByOrNull { abs(a[it][i]) }!!
        a[i] = a[pivot].also { a[pivot] = a[i] }
        for (j in i + 1..n)
            a[i][j] /= a[i][i]
        for (j in 0 until n)
            if (j != i && a[j][i] != 0.0)
                for (k in i + 1..n)
                    a[j][k] -= a[i][k] * a[j][i]
    }
    return a.map { it[n] }.toDoubleArray()
}

// random test
fun main() {
    val rnd = Random(1)
    for (step in 0..9999) {
        val n = rnd.nextInt(5) + 1
        val a = (0 until n).map { (0 until n).map { (rnd.nextInt(10) - 5).toDouble() }.toDoubleArray() }.toTypedArray()
        if (abs(det(a)) > 1e-6) {
            val b = (0 until n).map { (rnd.nextInt(10) - 5).toDouble() }.toDoubleArray()
            val x = gauss(a, b)
            for (i in a.indices) {
                val y = a[i].zip(x).sumOf { it.first * it.second }
                if (abs(b[i] - y) > 1e-9)
                    throw RuntimeException()
            }
        }
    }
}
