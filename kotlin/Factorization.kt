fun factorize(N: Long): Map<Long, Int> {
    val factors = arrayListOf<Long>()
    var n = N
    var d: Long = 2
    while (d * d <= n) {
        while (n % d == 0L) {
            factors += d
            n /= d
        }
        ++d
    }
    if (n > 1) factors += n
    return factors.groupingBy { it }.eachCount()
}

// Usage example
fun main() {
    println(factorize(36))
    println(factorize(150))
}
