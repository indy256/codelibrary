import java.math.BigInteger

class Rational(n: BigInteger, d: BigInteger = BigInteger.ONE) : Comparable<Rational> {
    val num: BigInteger
    val den: BigInteger

    init {
        val gcd = n.gcd(d)
        val g = if (d.signum() > 0) gcd else if (d.signum() < 0) gcd.negate() else BigInteger.ONE
        num = n.divide(g)
        den = d.divide(g)
    }

    constructor(num: Long, den: Long = 1) : this(BigInteger.valueOf(num), BigInteger.valueOf(den))

    operator fun plus(r: Rational): Rational = Rational(num.multiply(r.den).add(r.num.multiply(den)), den.multiply(r.den))

    operator fun minus(r: Rational): Rational = Rational(num.multiply(r.den).subtract(r.num.multiply(den)), den.multiply(r.den))

    operator fun times(r: Rational): Rational = Rational(num.multiply(r.num), den.multiply(r.den))

    operator fun div(r: Rational): Rational = Rational(num.multiply(r.den), den.multiply(r.num))

    operator fun unaryMinus(): Rational = Rational(num.negate(), den)

    fun inverse(): Rational = Rational(den, num)

    fun abs(): Rational = Rational(num.abs(), den)

    fun signum(): Int = num.signum()

    fun doubleValue(): Double = num.toDouble() / den.toDouble()

    fun longValue(): Long = num.toLong() / den.toLong()

    override fun compareTo(other: Rational): Int = num.multiply(other.den).compareTo(other.num.multiply(den))

    override fun equals(other: Any?): Boolean = num == (other as Rational).num && den == other.den

    override fun hashCode(): Int = num.hashCode() * 31 + den.hashCode()

    override fun toString(): String = "$num/$den"

    companion object {
        val ZERO = Rational(0)
        val ONE = Rational(1)
        val POSITIVE_INFINITY = Rational(1, 0)
        val NEGATIVE_INFINITY = Rational(-1, 0)
    }
}

// Usage example
fun main() {
    val a = Rational(1, 3)
    val b = Rational(1, 6)
    val s1 = Rational(1, 2)
    val s2 = a + b
    println(true == (s1 == s2))
    println(a * b / b - a)
    println(a * Rational.ZERO)
}
