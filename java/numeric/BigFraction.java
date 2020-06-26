package numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BigFraction extends Number implements Comparable<BigFraction> {
    public static final BigFraction ZERO = new BigFraction(BigInteger.ZERO);
    public static final BigFraction ONE = new BigFraction(BigInteger.ONE);

    private BigInteger top;
    private BigInteger bot;

    public BigFraction(BigInteger top) {
        this.top = top;
        this.bot = BigInteger.ONE;
    }

    public BigFraction(BigInteger top, BigInteger bot) {
        this(top, bot, false);
    }

    private BigFraction(BigInteger top, BigInteger bot, boolean skipGCD) {
        if (top == null || bot == null || bot.signum() == 0) {
            throw new IllegalArgumentException();
        }
        if (bot.signum() < 0) {
            top = top.negate();
            bot = bot.negate();
        }
        if (!skipGCD) {
            BigInteger g = top.gcd(bot);
            this.top = top.divide(g);
            this.bot = bot.divide(g);
        } else {
            this.top = top;
            this.bot = bot;
        }
    }

    public static BigFraction plus(BigFraction a, BigFraction b) {
        return new BigFraction(a.top.multiply(b.bot).add(b.top.multiply(a.bot)),
                a.bot.multiply(b.bot));
    }

    public static BigFraction minus(BigFraction a, BigFraction b) {
        return new BigFraction(a.top.multiply(b.bot).subtract(b.top.multiply(a.bot)),
                a.bot.multiply(b.bot));
    }

    public static BigFraction mul(BigFraction a, BigFraction b) {
        return new BigFraction(a.top.multiply(b.top), a.bot.multiply(b.bot));
    }

    public static BigFraction div(BigFraction a, BigFraction b) {
        return new BigFraction(a.top.multiply(b.bot), a.bot.multiply(b.top));
    }

    public BigFraction negate() {
        return new BigFraction(top.negate(), bot, true);
    }


    public BigFraction abs() {
        if (top.signum() >= 0) {
            return this;
        }
        return negate();
    }

    public int sign() {
        return top.signum();
    }

    public BigFraction pow(int exp) {
        return new BigFraction(top.pow(exp), bot.pow(exp), true);
    }

    public BigFraction inverse() {
        return new BigFraction(bot, top, true);
    }

    @Override
    public String toString() {
        if (!bot.equals(BigInteger.ONE)) {
            return String.format("%s/%s", top.toString(), bot.toString());
        } else {
            return top.toString();
        }
    }

    @Override
    public int compareTo(BigFraction o) {
        return top.multiply(o.bot).compareTo(o.top.multiply(bot));
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return top.divide(bot).longValue();
    }

    public BigInteger integer() {
        return top.divide(bot);
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return new BigDecimal(top).divide(new BigDecimal(bot),
                20,
                RoundingMode.HALF_EVEN).doubleValue();
    }


    /**
     * Parse from period fractional number: 12.99(087)
     */
    public static BigFraction parsePeriodFractionalStyleString(String s) {
        boolean negate = false;
        if (s.startsWith("-")) {
            negate = true;
            s = s.substring(1);
        }

        String[] split = s.split("\\.");
        BigFraction integerPart = new BigFraction(new BigInteger(split[0]));
        if (split.length == 1) {
            if (negate) {
                integerPart = integerPart.negate();
            }
            return integerPart;
        }

        String[] split2 = split[1].split("[()]");
        BigFraction t1;
        BigFraction b1;
        if (split2[0].isEmpty()) {
            t1 = ZERO;
            b1 = ONE;
        } else {
            t1 = new BigFraction(new BigInteger(split2[0]));
            b1 = new BigFraction(BigInteger.valueOf(10).pow(split2[0].length()));
        }

        BigFraction period = ZERO;
        if (split2.length > 1) {
            period = new BigFraction(new BigInteger(split2[1]), BigInteger.TEN.pow(split2[1].length()).subtract(BigInteger.ONE));
        }

        BigFraction ans = plus(integerPart, div(plus(t1, period), b1));
        if (negate) {
            ans = ans.negate();
        }
        return ans;
    }

    /**
     * parse from division style: 12/35
     */
    public static BigFraction parse(String s, int base) {
        String[] split = s.split("/");
        if (split.length == 1) {
            return new BigFraction(new BigInteger(split[0], base));
        } else {
            return new BigFraction(new BigInteger(split[0], base), new BigInteger(split[1], base));
        }
    }

    /**
     * Format to period fractional number: 12.99(087), 0.(0)
     *
     * @return
     */
    public static String formatPeriodFractionalStyleString(BigFraction fraction) {
        StringBuilder ans = new StringBuilder();
        if (fraction.sign() < 0) {
            ans.append('-');
            fraction = fraction.negate();
        }
        BigFraction integerPart = new BigFraction(fraction.integer());
        BigFraction fractionalPart = minus(fraction, integerPart);
        ans.append(integerPart);
        ans.append('.');
        Map<BigInteger, Integer> indexMap = new HashMap<>();

        BigInteger top = fractionalPart.top;
        BigInteger bot = fractionalPart.bot;
        while (top.signum() > 0) {
            Integer index = indexMap.get(top);
            if (index != null) {
                ans.insert(index.intValue(), '(');
                ans.append(')');
                break;
            }
            indexMap.put(top, ans.length());
            BigInteger[] divisorAndRemainder = top.multiply(BigInteger.TEN).divideAndRemainder(bot);
            ans.append(divisorAndRemainder[0]);
            top = divisorAndRemainder[1];
        }

        if (ans.charAt(ans.length() - 1) != ')') {
            ans.append("(0)");
        }
        return ans.toString();
    }

    @Override
    public int hashCode() {
        return top.hashCode() * 31 + bot.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BigFraction)) {
            return false;
        }
        BigFraction other = (BigFraction) obj;
        return top.equals(other.top) && bot.equals(other.bot);
    }

    // Usage example
    public static void main(String[] args) {
        BigFraction a = new BigFraction(BigInteger.valueOf(1), BigInteger.valueOf(2));
        BigFraction b = new BigFraction(BigInteger.valueOf(3));
        BigFraction c = BigFraction.parse("2/4", 10);
        BigFraction d = BigFraction.parsePeriodFractionalStyleString("12.00(1)");
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(BigFraction.plus(a, b));
        System.out.println(BigFraction.minus(a, b));
        System.out.println(BigFraction.mul(a, b));
        System.out.println(BigFraction.div(a, b));
    }
}
