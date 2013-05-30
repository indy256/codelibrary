public class Determinant1 {
	static long gcd(long a, long b) {
		while (b != 0) {
			long t = b;
			b = a % b;
			a = t;
		}
		return Math.abs(a);
	}

	public static long det(long[][] a) {
		int n = a.length;
		long div = 1;
		long det = 1;
		for (int i = 0; i < n; ++i) {
			int k = i;
			for (int j = i + 1; j < n; ++j) {
				if (a[j][i] != 0) {
					k = j;
					break;
				}
			}
			if (a[k][i] == 0)
				return 0;
			if (i != k) {
				det = -det;
				long[] t = a[i];
				a[i] = a[k];
				a[k] = t;
			}
			det *= a[i][i];
			long gcd = gcd(det, div);
			det /= gcd;
			div /= gcd;
			for (int j = i + 1; j < n; ++j)
				if (j != i && a[j][i] != 0) {
					for (int p = i + 1; p < n; ++p)
						a[j][p] = a[j][p] * a[i][i] - a[i][p] * a[j][i];
					// a[j][i] = 0;
					if (i < n - 1)
						div *= a[i][i];
					gcd = gcd(det, div);
					det /= gcd;
					div /= gcd;
				}
		}
		return det / div;
	}

	public static void main(String[] args) {

		System.out.println(279 == det(new long[][] { { 2, 4, 3, 5, 4 }, { 5, 4, 0, 2, 4 }, { 0, 5, 5, 2, 3 },
				{ 1, 0, 4, 3, 0 }, { 0, 5, 1, 4, 4 } }));
		System.out.println(-5 == det(new long[][] { { 3, 2, 2 }, { 0, 0, 5 }, { 4, 3, 1 } }));
		System.out.println(-4 == det(new long[][] { { 2, 2, 2 }, { 1, 2, 0 }, { 2, 2, 0 } }));
		System.out.println(-2 == det(new long[][] { { 1, 2 }, { 3, 4 } }));
		System.out.println(6 == det(new long[][] { { 2, 4 }, { 0, 3 } }));
	}
}
