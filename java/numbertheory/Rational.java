package numbertheory;

import java.math.BigInteger;

public class Rational implements Comparable<Rational> {
    public static final Rational ZERO = new Rational(0);
    public static final Rational ONE = new Rational(1);
    public static final Rational POSITIVE_INFINITY = new Rational(1, 0);
    public static final Rational NEGATIVE_INFINITY = new Rational(-1, 0);

    final BigInteger num;
    final BigInteger den;

    public Rational(BigInteger num, BigInteger den) {
        BigInteger gcd = num.gcd(den);
        BigInteger g = den.signum() > 0 ? gcd : den.signum() < 0 ? gcd.negate() : BigInteger.ONE;
        this.num = num.divide(g);
        this.den = den.divide(g);
    }

    public Rational(long num, long den) {
        this(BigInteger.valueOf(num), BigInteger.valueOf(den));
    }

    public Rational(long num) {
        this.num = BigInteger.valueOf(num);
        this.den = BigInteger.ONE;
    }

    public Rational add(Rational r) {
        return new Rational(num.multiply(r.den).add(r.num.multiply(den)), den.multiply(r.den));
    }

    public Rational sub(Rational r) {
        return new Rational(num.multiply(r.den).subtract(r.num.multiply(den)), den.multiply(r.den));
    }

    public Rational mul(Rational r) {
        return new Rational(num.multiply(r.num), den.multiply(r.den));
    }

    public Rational div(Rational r) {
        return new Rational(num.multiply(r.den), den.multiply(r.num));
    }

    public Rational negate() {
        return new Rational(num.negate(), den);
    }

    public Rational inverse() {
        return new Rational(den, num);
    }

    public Rational abs() {
        return new Rational(num.abs(), den);
    }

    public int signum() {
        return num.signum();
    }

    public double doubleValue() {
        return num.doubleValue() / den.doubleValue();
    }

    public long longValue() {
        return num.longValue() / den.longValue();
    }

    public int compareTo(Rational other) {
        return (num.multiply(other.den).compareTo(other.num.multiply(den)));
    }

    public boolean equals(Object obj) {
        return num.equals(((Rational) obj).num) && den.equals(((Rational) obj).den);
    }

    public int hashCode() {
        return num.hashCode() * 31 + den.hashCode();
    }

    public String toString() {
        return num + "/" + den;
    }

    // Usage example
    public static void main(String[] args) {
        Rational a = new Rational(1, 3);
        Rational b = new Rational(1, 6);
        Rational c = new Rational(1, 2);
        System.out.println(true == c.equals(a.add(b)));
        Rational d = new Rational(1, -2);
        System.out.println(d);
    }
}
