import java.util.*;

public class Factorize {

	public static TreeMap<Long, Integer> factorize(long n) {
		TreeMap<Long, Integer> factors = new TreeMap<Long, Integer>();
		n = Math.abs(n);
		for (long d = 2; n > 1;) {
			int cnt = 0;
			while (n % d == 0) {
				cnt++;
				n /= d;
			}
			if (cnt > 0) {
				factors.put(d, cnt);
			}
			d += (d == 2) ? 1 : 2;
			if (d * d > n) {
				d = n;
			}
		}
		return factors;
	}

	// Usage example
	public static void main(String[] args) {
		TreeMap<Long, Integer> f = factorize(24);
		System.out.println(f);
	}
}
