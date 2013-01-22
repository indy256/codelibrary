import java.util.*;

public class Factorize {

	public static TreeMap<Long, Integer> factorize(long n) {
		TreeMap<Long, Integer> factors = new TreeMap<>();
		for (long d = 2; n > 1; ) {
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

	public static int[] getDivisors(int n) {
		List<Integer> list = new ArrayList<>();
		for (int i = 1; (long) i * i <= n; i++)
			if (n % i == 0) {
				list.add(i);
				if (i * i != n)
					list.add(n / i);
			}
		int[] res = new int[list.size()];
		for (int i = 0; i < res.length; i++)
			res[i] = list.get(i);
		Arrays.sort(res);
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		TreeMap<Long, Integer> f = factorize(24);
		System.out.println(f);
		System.out.println(Arrays.toString(getDivisors(16)));
	}
}
