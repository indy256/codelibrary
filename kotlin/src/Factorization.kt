fun factorize(N: Long): Map<Long, Int> {
    val factors = mutableListOf<Long>()
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
    return factors.groupBy { it }.mapValues { it.value.size }
}

// Usage example
fun main(args: Array<String>) {
    println(factorize(36))
    println(factorize(150))
}
