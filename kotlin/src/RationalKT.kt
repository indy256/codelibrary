import java.math.BigInteger

class RationalKT(n: BigInteger, d: BigInteger = BigInteger.ONE) : Comparable<RationalKT> {
    val num: BigInteger
    val den: BigInteger

    init {
        val gcd = n.gcd(d)
        val g = if (d.signum() > 0) gcd else if (d.signum() < 0) gcd.negate() else BigInteger.ONE
        num = n.divide(g)
        den = d.divide(g)
    }

    constructor(num: Long, den: Long) : this(BigInteger.valueOf(num), BigInteger.valueOf(den))

    constructor(num: Long) : this(BigInteger.valueOf(num))

    operator fun plus(r: RationalKT): RationalKT = RationalKT(num.multiply(r.den).add(r.num.multiply(den)), den.multiply(r.den))

    operator fun minus(r: RationalKT): RationalKT = RationalKT(num.multiply(r.den).subtract(r.num.multiply(den)), den.multiply(r.den))

    operator fun times(r: RationalKT): RationalKT = RationalKT(num.multiply(r.num), den.multiply(r.den))

    operator fun div(r: RationalKT): RationalKT = RationalKT(num.multiply(r.den), den.multiply(r.num))

    operator fun unaryMinus(): RationalKT = RationalKT(num.negate(), den)

    fun inverse(): RationalKT = RationalKT(den, num)

    fun abs(): RationalKT = RationalKT(num.abs(), den)

    fun signum(): Int = num.signum()

    fun doubleValue(): Double = num.toDouble() / den.toDouble()

    fun longValue(): Long = num.toLong() / den.toLong()

    override fun compareTo(other: RationalKT): Int = num.multiply(other.den).compareTo(other.num.multiply(den))

    override fun equals(other: Any?): Boolean = num == (other as RationalKT).num && den == other.den

    override fun hashCode(): Int = num.hashCode() * 31 + den.hashCode()

    override fun toString(): String = "$num/$den"

    companion object {
        val ZERO = RationalKT(0)
        val ONE = RationalKT(1)
        val POSITIVE_INFINITY = RationalKT(1, 0)
        val NEGATIVE_INFINITY = RationalKT(-1, 0)

        // Usage example
        @JvmStatic fun main(args: Array<String>) {
            val a = RationalKT(1, 3)
            val b = RationalKT(1, 6)
            val s1 = RationalKT(1, 2)
            val s2 = a + b
            println(true == (s1 == s2))
            println(a * b / b - a)
        }
    }
}
