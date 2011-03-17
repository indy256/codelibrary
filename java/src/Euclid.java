public class Euclid {

	public static long gcd(long a, long b) {
		return b == 0 ? Math.abs(a) : gcd(b, a % b);
	}

	public static long lcm(long a, long b) {
		return Math.abs(a == 0 ? b : b == 0 ? a : a / gcd(a, b) * b);
	}

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
		return a > 0 ? new long[] { a, x, y } : new long[] { -a, -x, -y };
	}

	public static long mod(long a, long m) {
		a %= m;
		return a >= 0 ? a : a + m;
	}

	// precondition: m != 0 && (a, m) = 1
	public static long inverse(long a, long m) {
		a = mod(a, m);
		return a == 1 ? 1 : mod((1 - inverse(m, a) * m) / a, m);
	}

	public static long inverse2(long a, long m) {
		return mod(euclid(a, m)[1], m);
	}

	public static long gcd2(long a, long b) {
		while (b != 0) {
			long t = b;
			b = a % b;
			a = t;
		}
		return Math.abs(a);
	}

	public static long[] euclid2(long a, long b) {
		if (b == 0)
			return a > 0 ? new long[] { a, 1, 0 } : new long[] { -a, -1, 0 };
		long[] r = euclid2(b, a % b);
		return new long[] { r[0], r[2], r[1] - a / b * r[2] };
	}

	// Usage example
	public static void main(String[] args) {
		long a = 6;
		long b = 9;
		long[] res = euclid(a, b);
		System.out.println("gcd(a,b) = " + res[0]);
		System.out.println(res[1] + " * a " + " + " + res[2] + " * b " + " = gcd(a,b)");
	}
}
