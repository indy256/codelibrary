// https://ru.wikipedia.org/wiki/Интерполяционные_формулы_Ньютона
public class Interpolation {

	static int pow(int x, int n, int mod) {
		int res = 1;
		for (long p = x; n > 0; n >>= 1, p = (p * p) % mod) {
			if ((n & 1) != 0) {
				res = (int) (res * p % mod);
			}
		}
		return res;
	}

	int[] interpolation(int[] y, int mod) {
		int n = y.length;
//		vector<ll> f(n), a(n), b(n);
//		rept(i, n) {
//			f[i] = y[i];
//		}
//		a[0] = y[0];
//		b[0] = 1;
//		rep(i, 6004) {
//			inv[i] = fpow(i, MOD - 2, MOD);
//		}
//		for (int i = 1; i < n; i++) {
//			for (int j = 0; j + i < n; j++) {
//				f[j] = (f[j + 1] - f[j] + MOD) % MOD * inv[i] % MOD;
//			}
//			for (int j = i; j > 0; j--) b[j] = (b[j - 1] - (i - 1) * b[j] % MOD + MOD) % MOD;
//			b[0] *= -(i - 1);
//			b[0] %= MOD;
//			if (b[0] < 0) b[0] += MOD;
//			for (int j = 0; j <= i; j++) {
//				a[j] += b[j] * f[0];
//				a[j] %= MOD;
//			}
//		}
//		return a;
		return null;
	}

}
