import scala.language.implicitConversions
import scala.math.BigInt

class Rational(num: BigInt, den: BigInt = 1) extends Ordered[Rational] {
  private def normalize(num: BigInt, den: BigInt) = {
    val g = if (den.signum > 0) num.gcd(den) else if (den.signum < 0) -num.gcd(den) else BigInt(1)
    (num / g, den / g)
  }

  val (n, d) = normalize(num, den)

  def +(that: Rational) = new Rational(n * that.d + that.n * d, d * that.d)

  def -(that: Rational) = new Rational(n * that.d - that.n * d, d * that.d)

  def *(that: Rational) = new Rational(n * that.n, d * that.d)

  def /(that: Rational) = new Rational(n * that.d, d * that.n)

  def unary_- = new Rational(-n, d)

  def abs = new Rational(n.abs, d)

  def signum = n.signum

  def doubleValue = n.doubleValue / d.doubleValue

  def longValue = n.longValue / d.longValue

  override def compare(that: Rational) = (n * that.d).compare(that.n * d)

  def max(that: Rational) = if (this > that) this else that

  def min(that: Rational) = if (this < that) this else that

  override def equals(that: Any) = that match {
    case that: Rational => n == that.n && d == that.d
    case _ => false
  }

  override def hashCode = n.hashCode() * 31 + d.hashCode()

  override def toString = n + "/" + d
}

object Rational {
  final val Zero = new Rational(0)
  final val One = new Rational(1)
  final val PositiveInfinity = new Rational(1, 0)
  final val NegativeInfinity = new Rational(-1, 0)

  implicit def int2Rational(i: Int): Rational = new Rational(i)

  implicit def long2Rational(l: Long): Rational = new Rational(l)

  // Usage example
  def main(args: Array[String]) {
    val a = 1 / new Rational(3)
    val b = new Rational(1, 6)
    val c = new Rational(1, 2)
    assert(c == a + b)
  }
}
