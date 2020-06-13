package numeric;

public class Fraction implements Comparable<Fraction> {
    public long a, b;

    public Fraction(long a, long b) {
        if (b < 0) {
            a = -a;
            b = -b;
        }
        long g = gcd(Math.abs(a), b);
        if (g != 0) {
            a /= g;
            b /= g;
        }
        this.a = a;
        this.b = b;
    }

    static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public Fraction plus(Fraction o) {
        return new Fraction(a * o.b + o.a * b, b * o.b);
    }

    public Fraction minus(Fraction o) {
        return new Fraction(a * o.b - o.a * b, b * o.b);
    }

    public Fraction mul(Fraction o) {
        return new Fraction(a * o.a, b * o.b);
    }

    public Fraction div(Fraction o) {
        return new Fraction(a * o.b, b * o.a);
    }

    @Override
    public int compareTo(Fraction o) {
        return Long.compare(this.a * o.b, this.b * o.a);
    }

    @Override
    public String toString() {
        return a + "/" + b;
    }

    // Usage example
    public static void main(String[] args) {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 5);
        System.out.println(a.plus(b));
        System.out.println(a.minus(b));
        System.out.println(a.mul(b));
        System.out.println(a.div(b));
    }
}
