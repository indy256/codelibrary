import java.util.*;

public class Factorize {

    // returns map: prime_divisor -> power
	public static TreeMap<Long, Integer> factorize(long n) {
		TreeMap<Long, Integer> factors = new TreeMap<>();
		for (long divisor = 2; n > 1; ) {
			int power = 0;
			while (n % divisor == 0) {
				++power;
				n /= divisor;
			}
			if (power > 0) {
				factors.put(divisor, power);
			}
			++divisor;
			if (divisor * divisor > n) {
				divisor = n;
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

	// Usage example
	public static void main(String[] args) {
		TreeMap<Long, Integer> f = factorize(24);
		System.out.println(f);
		System.out.println(Arrays.toString(getAllDivisors(16)));
	}
}
