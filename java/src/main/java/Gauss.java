public class Gauss {

	public static double[] gauss(double[][] a, double[] b) {
		int n = a.length;
		for (int row = 0; row < n; row++) {
			int best = row;
			for (int i = row + 1; i < n; i++)
				if (Math.abs(a[best][row]) < Math.abs(a[i][row]))
					best = i;
			double[] tt = a[row];
			a[row] = a[best];
			a[best] = tt;
			double t = b[row];
			b[row] = b[best];
			b[best] = t;
			for (int i = row + 1; i < n; i++)
				a[row][i] /= a[row][row];
			b[row] /= a[row][row];
			// a[row][row] = 1;
			for (int i = 0; i < n; i++) {
				double x = a[i][row];
				if (i != row && x != 0) {
					// row + 1 instead of row is an optimization
					for (int j = row + 1; j < n; j++)
						a[i][j] -= a[row][j] * x;
					b[i] -= b[row] * x;
				}
			}
		}
		return b;
	}

	// Usage example
	public static void main(String[] args) {
		double[][] a = { { 4, 2, -1 }, { 2, 4, 3 }, { -1, 3, 5 } };
		double[] b = { 1, 0, 0 };
		double[][] a1 = a.clone();
		for (int i = 0; i < a.length; i++)
			a1[i] = a[i].clone();
		double[] b1 = b.clone();
		double[] x = gauss(a, b);
		for (int i = 0; i < a.length; i++) {
			double y = 0;
			for (int j = 0; j < a[i].length; j++)
				y += a1[i][j] * x[j];
			if (Math.abs(b1[i] - y) > 1e-9) {
				System.err.println("error");
				return;
			}
		}
	}
}
