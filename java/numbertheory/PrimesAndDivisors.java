package numbertheory;

import java.util.Arrays;
import java.util.stream.IntStream;

public class PrimesAndDivisors {
    // Generates prime numbers up to n in O(n*log(log(n))) time
    public static int[] generatePrimes(int n) {
        boolean[] prime = new boolean[n + 1];
        Arrays.fill(prime, 2, n + 1, true);

        for (int i = 2; i * i <= n; i++)
            if (prime[i])
                for (int j = i * i; j <= n; j += i) prime[j] = false;

        int[] primes = new int[n + 1];
        int cnt = 0;
        for (int i = 0; i < prime.length; i++)
            if (prime[i])
                primes[cnt++] = i;
        return Arrays.copyOf(primes, cnt);
    }

    // Generates prime numbers up to n in O(n) time
    public static int[] generatePrimesLinearTime(int n) {
        int[] lp = new int[n + 1];
        int[] primes = new int[n + 1];
        int cnt = 0;
        for (int i = 2; i <= n; ++i) {
            if (lp[i] == 0) {
                lp[i] = i;
                primes[cnt++] = i;
            }
            for (int j = 0; j < cnt && primes[j] <= lp[i] && i * primes[j] <= n; ++j) lp[i * primes[j]] = primes[j];
        }
        return Arrays.copyOf(primes, cnt);
    }

    // Generates prime numbers in the range n to m in O(n*log(log(n))) time
    public static int[] generatePrimesRange(int n, int m) {
        int[] primes = generatePrimes((int)Math.sqrt(m));
        boolean[] sieve = new boolean[(m - n + 1)];
        Arrays.fill(sieve, true);

        for (int i = 0; i < primes.length; i++) {
            int d = n / primes[i];
            d *= primes[i];
            while (d <= m) {
                if(d >= n && primes[i] != d)
                    sieve[d - n] = false;
                d += primes[i];
            }
        }

        primes = new int[sieve.length];
        int cnt = 0;
        for (int i = 0; i < sieve.length; i++)
            if (sieve[i] && (i + n) != 1)
                primes[cnt++] = (i + n);
        return Arrays.copyOf(primes, cnt);
    }

    public static boolean isPrime(long n) {
        if (n <= 1)
            return false;
        for (long i = 2; i * i <= n; i++)
            if (n % i == 0)
                return false;
        return true;
    }

    public static int[] numberOfPrimeDivisors(int n) {
        int[] divisors = new int[n + 1];
        Arrays.fill(divisors, 2, n + 1, 1);
        for (int i = 2; i * i <= n; ++i)
            if (divisors[i] == 1)
                for (int j = i; j * i <= n; j++) divisors[i * j] = divisors[j] + 1;
        return divisors;
    }

    // Generates minimum prime divisor of all numbers up to n in O(n) time
    public static int[] generateMinDivisors(int n) {
        int[] lp = new int[n + 1];
        lp[1] = 1;
        int[] primes = new int[n + 1];
        int cnt = 0;
        for (int i = 2; i <= n; ++i) {
            if (lp[i] == 0) {
                lp[i] = i;
                primes[cnt++] = i;
            }
            for (int j = 0; j < cnt && primes[j] <= lp[i] && i * primes[j] <= n; ++j) lp[i * primes[j]] = primes[j];
        }
        return lp;
    }

    // Generates prime divisor of all numbers up to n
    public static int[] generateDivisors(int n) {
        int[] divisors = IntStream.range(0, n + 1).toArray();
        for (int i = 2; i * i <= n; i++)
            if (divisors[i] == i)
                for (int j = i * i; j <= n; j += i) divisors[j] = i;
        return divisors;
    }

    // Euler's totient function
    public static int phi(int n) {
        int res = n;
        for (int i = 2; i * i <= n; i++)
            if (n % i == 0) {
                while (n % i == 0) n /= i;
                res -= res / i;
            }
        if (n > 1)
            res -= res / n;
        return res;
    }

    // Euler's totient function
    public static int[] generatePhi(int n) {
        int[] res = IntStream.range(0, n + 1).toArray();
        for (int i = 1; i <= n; i++)
            for (int j = i + i; j <= n; j += i) res[j] -= res[i];
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int n = 31;

        int[] primes1 = generatePrimes(n);
        int[] primes2 = generatePrimesLinearTime(n);
        int[] primes3 = generatePrimesRange(1999999900, 2000000000);

        System.out.println(Arrays.toString(primes1));
        System.out.println(Arrays.toString(primes2));
        System.out.println(Arrays.equals(primes1, primes2));
        System.out.println(Arrays.equals(primes3, new int[]{1999999913, 1999999927, 1999999943, 1999999973}));

        System.out.println(Arrays.toString(numberOfPrimeDivisors(n)));
        System.out.println(Arrays.toString(generateMinDivisors(n)));
        System.out.println(Arrays.toString(generateDivisors(n)));

        n = 1000;
        int[] phi = generatePhi(n);
        long[] PHI = MultiplicativeFunction.PHI.generateValues(n);
        for (int i = 0; i <= n; i++) {
            if (phi[i] != phi(i) || phi[i] != PHI[i]) {
                System.err.println(i);
            }
        }
    }
}
