import java.math.BigInteger;
import java.util.*;

public class Euclid {

	public static long gcd(long a, long b) {
		return b == 0 ? Math.abs(a) : gcd(b, a % b);
	}

	public static long gcd2(long a, long b) {
		while (b != 0) {
			long t = b;
			b = a % b;
			a = t;
		}
		return Math.abs(a);
	}

	public static long lcm(long a, long b) {
		return Math.abs(a / gcd(a, b) * b);
	}

	// returns { gcd(a,b), x, y } such that gcd(a,b) = a*x + b*y
	public static long[] euclid(long a, long b) {
		long x = 1, y = 0, x1 = 0, y1 = 1, t;
		while (b != 0) {
			long q = a / b;
			t = x;
			x = x1;
			x1 = t - q * x1;
			t = y;
			y = y1;
			y1 = t - q * y1;
			t = b;
			b = a - q * b;
			a = t;
		}
		return a > 0 ? new long[]{a, x, y} : new long[]{-a, -x, -y};
	}

	public static long[] euclid2(long a, long b) {
		if (b == 0)
			return a > 0 ? new long[]{a, 1, 0} : new long[]{-a, -1, 0};
		long[] r = euclid2(b, a % b);
		return new long[]{r[0], r[2], r[1] - a / b * r[2]};
	}

	// precondition: m > 0
	public static long mod(long a, long m) {
		a %= m;
		return a >= 0 ? a : a + m;
	}

	// precondition: m > 0 && gcd(a, m) = 1
	public static long modInverse(long a, long m) {
		a = mod(a, m);
		return a == 0 ? 0 : mod((1 - modInverse(m % a, a) * m) / a, m);
	}

	// precondition: m > 0 && gcd(a, m) = 1
	public static long modInverse2(long a, long m) {
		return (euclid(a, m)[1] % m + m) % m;
	}

	// precondition: p is prime
	public static int[] generateInverse(int p) {
		int[] res = new int[p];
		res[1] = 1;
		for (int i = 2; i < p; ++i)
			res[i] = (p - (p / i) * res[p % i] % p) % p;
		return res;
	}

	// solve x = a[i] (mod p[i]), where gcd(p[i], p[j]) == 1
	public static int simpleRestore(int[] a, int[] p) {
		int res = a[0];
		int m = 1;
		for (int i = 1; i < a.length; i++) {
			m *= p[i - 1];
			while (res % p[i] != a[i])
				res += m;
		}
		return res;
	}

	public static int garnerRestore(int[] a, int[] p) {
		int[] x = new int[a.length];
		for (int i = 0; i < x.length; ++i) {
			x[i] = a[i];
			for (int j = 0; j < i; ++j) {
				x[i] = (int) modInverse(p[j], p[i]) * (x[i] - x[j]);
				x[i] = (x[i] % p[i] + p[i]) % p[i];
			}
		}
		int res = x[0];
		int m = 1;
		for (int i = 1; i < a.length; i++) {
			m *= p[i - 1];
			res += x[i] * m;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int steps = 0; steps < 10000; steps++) {
			int a = rnd.nextInt(20) - 10;
			int b = rnd.nextInt(20) - 10;
			BigInteger xa = BigInteger.valueOf(a);
			BigInteger xb = BigInteger.valueOf(b);
			long gcd1 = gcd(a, b);
			long gcd2 = gcd2(a, b);
			long gcd = xa.gcd(xb).longValue();

			long[] euclid1 = euclid(a, b);
			long[] euclid2 = euclid2(a, b);

			long inv1 = 0;
			long inv2 = 0;
			long inv = 0;
			if (gcd == 1 && b > 0) {
				inv1 = modInverse(a, b);
				inv2 = modInverse2(a, b);
				inv = xa.modInverse(xb).longValue();
			}

			if (gcd1 != gcd || gcd2 != gcd || !Arrays.equals(euclid1, euclid2) || euclid1[0] != gcd || inv1 != inv
					|| inv2 != inv) {
				System.err.println(a + " " + b);
			}
		}
		long a = 6;
		long b = 9;
		long[] res = euclid(a, b);
		System.out.println(res[1] + " * (" + a + ") " + " + " + res[2] + " * (" + b + ") = gcd(" + a + "," + b + ") = "
				+ res[0]);

		System.out.println(Arrays.toString(generateInverse(7)));
	}
}
