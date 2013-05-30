import java.math.BigDecimal;
import java.math.BigInteger;

public class Determinant {

	public static double det(double[][] a) {
		final double eps = 1e-10;
		int n = a.length;
		double res = 1;
		boolean[] used = new boolean[n];
		for (int i = 0; i < n; i++) {
			int p;
			for (p = 0; p < n; p++)
				if (!used[p] && Math.abs(a[p][i]) > eps)
					break;
			if (p == n)
				return 0;
			res *= a[p][i];
			used[p] = true;
			double z = 1 / a[p][i];
			for (int j = 0; j < n; j++)
				a[p][j] *= z;
			for (int j = 0; j < n; ++j)
				if (j != p) {
					z = a[j][i];
					for (int k = 0; k < n; ++k)
						a[j][k] -= z * a[p][k];
				}
		}
		return res;
	}

	public static BigInteger detCrout(BigDecimal a[][], int n) {
		try {
			for (int i = 0; i < n; i++) {
				boolean nonzero = false;
				for (int j = 0; j < n; j++)
					if (a[i][j].compareTo(new BigDecimal(BigInteger.ZERO)) > 0)
						nonzero = true;
				if (!nonzero)
					return BigInteger.ZERO;
			}

			BigDecimal scaling[] = new BigDecimal[n];
			for (int i = 0; i < n; i++) {
				BigDecimal big = new BigDecimal(BigInteger.ZERO);
				for (int j = 0; j < n; j++)
					if (a[i][j].abs().compareTo(big) > 0)
						big = a[i][j].abs();
				scaling[i] = (new BigDecimal(BigInteger.ONE)).divide(big, 100, BigDecimal.ROUND_HALF_EVEN);
			}

			int sign = 1;

			for (int j = 0; j < n; j++) {

				for (int i = 0; i < j; i++) {
					BigDecimal sum = a[i][j];
					for (int k = 0; k < i; k++)
						sum = sum.subtract(a[i][k].multiply(a[k][j]));
					a[i][j] = sum;
				}

				BigDecimal big = new BigDecimal(BigInteger.ZERO);
				int imax = -1;
				for (int i = j; i < n; i++) {
					BigDecimal sum = a[i][j];
					for (int k = 0; k < j; k++)
						sum = sum.subtract(a[i][k].multiply(a[k][j]));
					a[i][j] = sum;
					BigDecimal cur = sum.abs();
					cur = cur.multiply(scaling[i]);
					if (cur.compareTo(big) >= 0) {
						big = cur;
						imax = i;
					}
				}

				if (j != imax) {

					for (int k = 0; k < n; k++) {
						BigDecimal t = a[j][k];
						a[j][k] = a[imax][k];
						a[imax][k] = t;
					}

					BigDecimal t = scaling[imax];
					scaling[imax] = scaling[j];
					scaling[j] = t;

					sign = -sign;
				}

				if (j != n - 1)
					for (int i = j + 1; i < n; i++)
						a[i][j] = a[i][j].divide(a[j][j], 100, BigDecimal.ROUND_HALF_EVEN);

			}

			BigDecimal result = new BigDecimal(1);
			if (sign == -1)
				result = result.negate();
			for (int i = 0; i < n; i++)
				result = result.multiply(a[i][i]);

			return result.divide(BigDecimal.valueOf(1), 0, BigDecimal.ROUND_HALF_EVEN).toBigInteger();

		} catch (Exception e) {
			return BigInteger.ZERO;
		}
	}

	public static BigInteger detBigInteger(long[][] a) {
		int n = a.length;
		BigInteger[][] b = new BigInteger[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				b[i][j] = BigInteger.valueOf(a[i][j]);

		int sign = 1;
		int[] pow = new int[n];
		for (int i = 0; i < n; ++i) {
			int k = i;
			for (int j = i + 1; j < n; ++j) {
				if (b[k][i].equals(BigInteger.ZERO) || !b[j][i].equals(BigInteger.ZERO)
						&& b[k][i].abs().compareTo(b[j][i].abs()) > 0) {
					k = j;
				}
			}
			if (b[k][i].equals(BigInteger.ZERO))
				return BigInteger.ZERO;
			if (i != k) {
				sign = -sign;
				BigInteger[] t = b[i];
				b[i] = b[k];
				b[k] = t;
			}
			++pow[i];
			for (int j = i + 1; j < n; ++j)
				if (!b[j][i].equals(BigInteger.ZERO)) {
					for (int p = i + 1; p < n; ++p) {
						b[j][p] = b[j][p].multiply(b[i][i]).subtract(b[i][p].multiply(b[j][i]));
					}
					--pow[i];
				}
		}
		BigInteger det = BigInteger.ONE;
		for (int i = 0; i < n; i++)
			if (pow[i] > 0)
				det = det.multiply(b[i][i].pow(pow[i]));
		for (int i = 0; i < n; i++)
			if (pow[i] < 0)
				det = det.divide(b[i][i].pow(-pow[i]));
		if (sign < 0)
			det = det.negate();
		return det;
	}

	// Usage example
	public static void main(String[] args) {
		check(new long[][] { { 2, 4, 3, 5, 4 }, { 5, 4, 0, 2, 4 }, { 0, 5, 5, 2, 3 }, { 1, 0, 4, 3, 0 },
				{ 0, 5, 1, 4, 4 } });
		check(new long[][] { { 3, 2, 2 }, { 0, 0, 5 }, { 4, 3, 1 } });
		check(new long[][] { { 2, 2, 2 }, { 1, 2, 0 }, { 2, 2, 0 } });
		check(new long[][] { { 1, 2 }, { 3, 4 } });
		check(new long[][] { { 2, 4 }, { 0, 3 } });
	}

	private static void check(long[][] a1) {
		int n = a1.length;
		double[][] a2 = new double[n][n];
		BigDecimal[][] a3 = new BigDecimal[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a2[i][j] = a1[i][j];
				a3[i][j] = BigDecimal.valueOf(a1[i][j]);
			}
		}
		double res1 = det(a2);
		BigInteger res2 = detCrout(a3, n);
		BigInteger res3 = detBigInteger(a1);
		System.out.println(res1 + " " + res2 + " " + res3);
	}
}
