import java.util.Random
import kotlin.system.measureTimeMillis

fun quickSort(a: IntArray, rnd: Random = Random(), low: Int = 0, high: Int = a.size - 1) {
    if (low >= high)
        return
    val separator = a[low + rnd.nextInt(high - low + 1)]
    var i = low
    var j = high
    while (i <= j) {
        while (a[i] < separator)
            ++i
        while (a[j] > separator)
            --j
        if (i <= j) {
            a[i] = a[j].also { a[j] = a[i] }
            ++i
            --j
        }
    }
    quickSort(a, rnd, low, j)
    quickSort(a, rnd, i, high)
}

// test
fun main() {
    val n = 10_000_000L
    val rnd = Random()
    val a = rnd.ints(n).toArray()
    val b = a.sortedArray()

    println(measureTimeMillis { quickSort(a, rnd) })

    if (!a.contentEquals(b))
        throw RuntimeException()
}
