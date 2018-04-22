package numbertheory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.summingInt;

public class Factorization {

    // returns prime_divisor -> power
    // O(sqrt(n)) complexity
    public static Map<Long, Integer> factorize(long n) {
        List<Long> factors = new ArrayList<>();
        for (long d = 2; d * d <= n; d++) {
            while (n % d == 0) {
                factors.add(d);
                n /= d;
            }
        }
        if (n > 1) {
            factors.add(n);
        }
        return factors.stream().collect(Collectors.groupingBy(Function.identity(), summingInt(v -> 1)));
    }

    public static int[] getAllDivisors(int n) {
        List<Integer> divisors = new ArrayList<>();
        for (int d = 1; d * d <= n; d++)
            if (n % d == 0) {
                divisors.add(d);
                if (d * d != n)
                    divisors.add(n / d);
            }
        return divisors.stream().sorted().mapToInt(Integer::valueOf).toArray();
    }

    // returns divisor of n or -1 if failed: https://en.wikipedia.org/wiki/Pollard%27s_rho_algorithm#Algorithm
    // O(n^(1/4)) complexity
    public static long pollard(long n) {
        Random rnd = new Random(1);
        long x = Math.abs(rnd.nextLong()) % n;
        long y = x;
        while (true) {
            x = g(x, n);
            y = g(g(y, n), n);
            if (x == y)
                return -1;
            long d = gcd(Math.abs(x - y), n);
            if (d != 1)
                return d;
        }
    }

    static long gcd(long a, long b) {
        return a == 0 ? b : gcd(b % a, a);
    }

    static long g(long x, long n) {
        return (41 * x + 1) % n;
    }

    // returns divisor of n: https://en.wikipedia.org/wiki/Fermat%27s_factorization_method
    public static long ferma(long n) {
        long x = (long) Math.sqrt(n);
        long y = 0;
        long r = x * x - y * y - n;
        while (true) {
            if (r == 0)
                return x != y ? x - y : x + y;
            else if (r > 0) {
                r -= y + y + 1;
                ++y;
            } else {
                r += x + x + 1;
                ++x;
            }
        }
    }

    // Usage example
    public static void main(String[] args) {
        System.out.println(factorize(24));
        System.out.println(Arrays.toString(getAllDivisors(16)));

        long n = 1000_003L * 100_000_037;
        System.out.println(pollard(n));
        System.out.println(ferma(n));
    }
}
