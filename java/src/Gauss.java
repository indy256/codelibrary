public class Gauss {
	public static double[] gauss(double[][] a, double[] b) {
		int n = a.length;
		boolean[] vis = new boolean[n];
		for (int i = 0; i < n; i++) {
			int cur = 0;
			for (int j = 0; j < n; j++) {
				if (!vis[j] && (vis[cur] || Math.abs(a[j][i]) > Math.abs(a[cur][i]))) {
					cur = j;
				}
			}
			vis[cur] = true;
			for (int j = i + 1; j < n; j++) {
				a[cur][j] /= a[cur][i];
			}
			b[cur] /= a[cur][i];
			for (int j = 0; j < n; j++) {
				if (j != cur && a[j][i] != 0) {
					for (int p = i + 1; p < n; p++) {
						a[j][p] -= a[cur][p] * a[j][i];
					}
					b[j] -= b[cur] * a[j][i];
				}
			}
		}
		return b;
	}

	// Usage example
	public static void main(String[] args) {

	}
}
