package numbertheory;

import java.util.Arrays;
import java.util.stream.IntStream;

// f(a*b) = f(a)*f(b) | gcd(a,b)=1
public interface MultiplicativeFunction {
    long apply(long prime, int exponent, long power);

    MultiplicativeFunction PHI = (p, exp, power) -> power - power / p;

    MultiplicativeFunction MOBIUS = (p, exp, power) -> exp == 1 ? -1 : 0;

    MultiplicativeFunction DIVISOR_COUNT = (p, exp, power) -> exp + 1;

    MultiplicativeFunction DIVISOR_SUM = (p, exp, power) -> (power * p - 1) / (p - 1);

    default long get(long x) {
        long res = 1;
        for (long d = 2; d * d <= x; d++) {
            if (x % d == 0) {
                int exp = 0;
                long power = 1;
                do {
                    ++exp;
                    power *= d;
                    x /= d;
                } while (x % d == 0);
                res *= apply(d, exp, power);
            }
        }
        if (x != 1)
            res *= apply(x, 1, x);
        return res;
    }

    default long[] generateValues(int n) {
        int[] divisor = generateDivisors(n);
        long[] res = new long[n + 1];
        res[1] = 1;
        for (int i = 2; i <= n; i++) {
            int j = i;
            int exp = 0;
            do {
                j /= divisor[i];
                ++exp;
            } while (j % divisor[i] == 0);
            res[i] = res[j] * apply(divisor[i], exp, i / j);
        }
        return res;
    }

    static int[] generateDivisors(int n) {
        int[] divisors = IntStream.range(0, n + 1).toArray();
        for (int i = 2; i * i <= n; i++)
            if (divisors[i] == i)
                for (int j = i * i; j <= n; j += i) divisors[j] = i;
        return divisors;
    }

    // Usage example
    public static void main(String[] args) {
        System.out.println(1 == MOBIUS.get(1));
        System.out.println(-1 == MOBIUS.get(2));
        System.out.println(Arrays.toString(MOBIUS.generateValues(10)));
    }
}
