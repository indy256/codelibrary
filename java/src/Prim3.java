public class Prim3 {

	public static long mstPrim(int[][] d) {
		int n = d.length;
		boolean[] vis = new boolean[n];
		vis[0] = true;
		long res = 0;
		for (int steps = 0; steps < n - 1; steps++) {
			int bd = Integer.MAX_VALUE;
			int bj = -1;
			for (int i = 0; i < n; i++) {
				if (vis[i]) {
					for (int j = 0; j < n; j++) {
						if (!vis[j] && bd > d[i][j]) {
							bd = d[i][j];
							bj = j;
						}
					}
				}
			}
			res += bd;
			vis[bj] = true;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
	}
}
