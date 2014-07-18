import java.util.*;

public class PrimitiveRoot {

	static int pow(int x, int n, int MOD) {
		long y = x;
		int res = 1;
		for (; n > 0; n >>= 1) {
			if ((n & 1) != 0)
				res = (int) (res * y % MOD);
			y = y * y % MOD;
		}
		return res;
	}

	public static int generator(int p) {
		List<Integer> fact = new ArrayList<>();
		int phi = p - 1;
		int n = phi;
		for (int i = 2; i * i <= n; ++i)
			if (n % i == 0) {
				fact.add(i);
				while (n % i == 0)
					n /= i;
			}
		if (n > 1)
			fact.add(n);
		for (int res = 2; res <= p; ++res) {
			int i;
			for (i = 0; i < fact.size() && pow(res, phi / fact.get(i), p) != 1; ++i) ;
			if (i == fact.size())
				return res;
		}
		return -1;
	}

	// usage example
	public static void main(String[] args) {
		int mod = 11;
		int g = generator(mod);
		int cur = g;
		for (int i = 1; i < mod; i++) {
			System.out.println(cur);
			cur = (cur * g) % mod;
		}
	}
}
