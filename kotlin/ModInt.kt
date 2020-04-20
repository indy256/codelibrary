inline class ModInt(val value: Int) {
    companion object {
        const val mod: Int = 1000_000_007
    }

    operator fun unaryMinus() = ModInt(norm(-value.toLong()))
    operator fun plus(b: ModInt) = ModInt(norm(value.toLong() + b.value))
    operator fun minus(b: ModInt) = ModInt(norm(value.toLong() - b.value))
    operator fun times(b: ModInt) = ModInt(norm(value.toLong() * b.value))
    operator fun div(b: ModInt) = ModInt(norm(inverse(b.value, mod) * value.toLong()))

    fun norm(x: Long): Int {
        var x = x
        if (x < -mod || x >= mod)
            x %= mod
        if (x < 0) x += mod;
        return x.toInt()
    }

    fun inverse(a: Int, m: Int): Int {
        var a = a
        var m = m
        var u = 0
        var v = 1
        while (a != 0) {
            val t = m / a
            m -= t * a
            a = m.also { m = a }
            u -= t * v
            u = v.also { v = u }
        }
        assert(m == 1)
        return u
    }
}

// Usage example
fun main() {
    val a = ModInt(1)
    var b = ModInt(1)
    b += ModInt(1)
    val c = ModInt(1000_000_000)
    val d = a / b * c / c
    print(d)
}
