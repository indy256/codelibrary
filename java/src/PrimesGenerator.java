import java.util.*;

public class PrimesGenerator {
	public static int[] getPrimesFast(int n) {
		if (n <= 1) {
			return new int[0];
		}
		boolean[] prime = new boolean[n + 1];
		Arrays.fill(prime, true);
		prime[0] = prime[1] = false;
		int cnt = n - 1;
		for (int i = 2; i * i <= n; i++) {
			if (prime[i]) {
				for (int j = i + i; j <= n; j += i) {
					if (prime[j]) {
						--cnt;
						prime[j] = false;
					}
				}
			}
		}
		int[] primes = new int[cnt];
		int k = 0;
		for (int i = 2; i <= n; i++)
			if (prime[i])
				primes[k++] = i;
		return primes;
	}

	public static boolean isPrime(long n) {
		if (n <= 1)
			return false;

		for (long i = 2; i * i <= n; i++)
			if (n % i == 0)
				return false;

		return true;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 10;
		for (int p : getPrimesFast(n))
			System.out.println(p);

		System.out.println();

		for (int i = 0; i <= n; i++)
			if (isPrime(i))
				System.out.println(i);
	}
}
