public class ChineseTheorem {

	// solve x = a[i] (mod p[i])
	public static long solve(int[] a, int[] p) {
		long res = a[0];
		long add = p[0];
		for (int i = 1; i < a.length; i++) {
			while (res % p[i] != a[i])
				res += add;
			add *= p[i];
		}
		return res;
	}
}
