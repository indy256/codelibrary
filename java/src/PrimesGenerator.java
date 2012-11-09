import java.util.*;

public class PrimesGenerator {

	public static int[] getPrimes(int n) {
		boolean[] prime = new boolean[n + 1];
		Arrays.fill(prime, 2, n + 1, true);
		for (int i = 2; i * i <= n; i++) {
			if (prime[i]) {
				for (int j = i * i; j <= n; j += i) {
					prime[j] = false;
				}
			}
		}
		int[] primes = new int[n + 1];
		int cnt = 0;
		for (int i = 0; i < prime.length; i++)
			if (prime[i])
				primes[cnt++] = i;

		return Arrays.copyOf(primes, cnt);
	}

	public static int[] getPrimesLinear(int n) {
		int[] lp = new int[n + 1];
		int[] primes = new int[n + 1];
		int cnt = 0;

		for (int i = 2; i <= n; ++i) {
			if (lp[i] == 0) {
				lp[i] = i;
				primes[cnt++] = i;
			}
			for (int j = 0; j < cnt && primes[j] <= lp[i] && i * primes[j] <= n; ++j) {
				lp[i * primes[j]] = primes[j];
			}
		}

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
				for (int j = i; j * i <= n; j++)
					divisors[j * i] = divisors[j] + 1;
		return divisors;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 31;

		int[] primes1 = getPrimes(n);
		int[] primes2 = getPrimesLinear(n);

		System.out.println(Arrays.toString(primes1));
		System.out.println(Arrays.toString(primes2));
		System.out.println(Arrays.equals(primes1, primes2));

		System.out.println(Arrays.toString(numberOfPrimeDivisors(n)));
	}
}
