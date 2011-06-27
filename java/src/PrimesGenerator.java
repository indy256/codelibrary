import java.util.*;

public class PrimesGenerator {

	public static int[] getPrimes(int n) {
		if (n <= 1)
			return new int[0];
		boolean[] prime = new boolean[n + 1];
		Arrays.fill(prime, 2, n + 1, true);
		int[] primes = new int[n + 1];
		int cnt = 0;
		for (int i = 2; i <= n; i++) {
			if (prime[i]) {
				for (int j = i * i; j <= n; j += i)
					if (prime[j])
						prime[j] = false;
				primes[cnt++] = i;
			}
		}
		int[] res = new int[cnt];
		System.arraycopy(primes, 0, res, 0, cnt);
		return res;
	}

	public static boolean isPrime(long n) {
		if (n <= 1)
			return false;

		for (long i = 2; i * i <= n; i++)
			if (n % i == 0)
				return false;

		return true;
	}

	public static int[] numberOfDivisors(int n) {
		int[] am = new int[n + 1];
		for (int i = 0; i <= n; ++i)
			am[i] = 1;
		am[1] = 0;
		for (int i = 2; i * i <= n; ++i)
			if (am[i] == 1) {
				for (int j = i * i, k = i; j <= n; j += i, ++k)
					am[j] = am[k] + 1;
			}
		return am;
	}

	// Usage example
	public static void main(String[] args) {
		String primes1 = "";
		String primes2 = "";
		int n = 31;
		for (int p : getPrimes(n))
			primes1 += p + " ";

		for (int i = 0; i <= n; i++)
			if (isPrime(i))
				primes2 += i + " ";

		System.out.println(primes1);
		System.out.println(primes2);
		System.out.println(primes1.equals(primes2));
		
		System.out.println(Arrays.toString(numberOfDivisors(n)));
	}
}
