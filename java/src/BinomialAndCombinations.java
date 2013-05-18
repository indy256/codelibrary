import java.math.BigInteger;
import java.util.Arrays;

public class BinomialAndCombinations {

	public static long[][] binomialTable(int maxn) {
		long[][] c = new long[maxn + 1][maxn + 1];
		for (int i = 0; i <= maxn; i++)
			for (int j = 0; j <= i; j++)
				c[i][j] = (j == 0) ? 1 : c[i - 1][j - 1] + c[i - 1][j];
		return c;
	}

	public static long binomial(long n, long m) {
		if (n < m || m < 0 || n < 0)
			return 0;
		long res = 1;
		for (long i = 0; i < Math.min(m, n - m); i++) {
			res = res * (n - i) / (i + 1);
		}
		return res;
	}

	// n! % mod
	public static int factorial(int n, int mod) {
		long result = 1;
		for (int i = 2; i <= n; i++)
			result = result * i % mod;
		return (int) (result % mod);
	}

	// n! mod p, p - prime, p*log(n) complexity
	int factorial2(int n, int p) {
		int res = 1;
		while (n > 1) {
			res = (res * ((n / p) % 2 == 1 ? p - 1 : 1)) % p;
			for (int i = 2; i <= n % p; ++i)
				res = (res * i) % p;
			n /= p;
		}
		return res % p;
	}

	public static int binomial(int n, int m, int mod) {
		if (n < m || m < 0 || n < 0)
			return 0;
		if (2 * m > n)
			m = n - m;
		long res = 1;
		for (int i = n - m + 1; i <= n; i++)
			res = res * i % mod;
		return (int) (res * BigInteger.valueOf(factorial(m, mod)).modInverse(BigInteger.valueOf(mod)).intValue() % mod);
	}

	public static boolean nextCombination(int[] p, int n) {
		int m = p.length;
		for (int i = m - 1; i >= 0; i--) {
			if (p[i] < n + i - m) {
				++p[i];
				while (++i < m) {
					p[i] = p[i - 1] + 1;
				}
				return true;
			}
		}
		return false;
	}

	public static boolean nextCombinationWithRepeats(int[] p, int n) {
		int m = p.length;
		int[] f = new int[n];
		for (int i = 0; i < m; i++) {
			++f[p[i]];
		}
		int[] b = new int[n + m - 1];
		for (int i = 0, sum = -1; i < n - 1; i++) {
			sum += f[i] + 1;
			b[sum] = 1;
		}
		if (!nextPermutation(b)) {
			return false;
		}
		for (int i = 0, j = 0, k = 0; i < b.length; i++) {
			if (b[i] == 0) {
				p[j++] = k;
			} else {
				++k;
			}
		}
		return true;
	}

	// auxiliary
	static boolean nextPermutation(int[] p) {
		for (int a = p.length - 2; a >= 0; --a)
			if (p[a] < p[a + 1])
				for (int b = p.length - 1; ; --b)
					if (p[b] > p[a]) {
						int t = p[a];
						p[a] = p[b];
						p[b] = t;
						for (++a, b = p.length - 1; a < b; ++a, --b) {
							t = p[a];
							p[a] = p[b];
							p[b] = t;
						}
						return true;
					}
		return false;
	}

	// Usage example
	public static void main(String[] args) {
		int[] p = {0, 1};
		System.out.println(false == nextCombination(p, 2));
		System.out.println(true == Arrays.equals(new int[]{0, 1}, p));

		p = new int[]{0, 0};
		System.out.println(true == nextCombinationWithRepeats(p, 2));
		System.out.println(true == Arrays.equals(new int[]{0, 1}, p));

		System.out.println(true == nextCombinationWithRepeats(p, 2));
		System.out.println(true == Arrays.equals(new int[]{1, 1}, p));

		System.out.println(false == nextCombinationWithRepeats(p, 2));
	}
}
