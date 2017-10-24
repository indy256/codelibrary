import java.util.*;

public class Factorization {

	// prime_divisor -> power
	public static Map<Long, Integer> factorize(long n) {
		Map<Long, Integer> factors = new LinkedHashMap<>();
		for (long d = 2; n > 1; ) {
			int power = 0;
			while (n % d == 0) {
				++power;
				n /= d;
			}
			if (power > 0) {
				factors.put(d, power);
			}
			++d;
			if (d * d > n) {
				d = n;
			}
		}
		return factors;
	}

	public static int[] getAllDivisors(int n) {
		List<Integer> divisors = new ArrayList<>();
		for (int d = 1; d * d <= n; d++)
			if (n % d == 0) {
				divisors.add(d);
				if (d * d != n)
					divisors.add(n / d);
			}
		int[] res = new int[divisors.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = divisors.get(i);
		Arrays.sort(res);
		return res;
	}

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

	public static long pollard(long n) {
		Random rnd = new Random(1);
		long x = Math.abs(rnd.nextLong()) % n;
		long y = x;
		while (true) {
			x = f(x, n);
			y = f(f(y, n), n);
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

	static long f(long x, long n) {
		return (41 * x + 1) % n;
	}

	// Usage example
	public static void main(String[] args) {
		Map<Long, Integer> f = factorize(24);
		System.out.println(f);
		System.out.println(Arrays.toString(getAllDivisors(16)));

		long n = 1000_003L * 100_000_037;
		System.out.println(ferma(n));
		System.out.println(pollard(n));
	}
}
