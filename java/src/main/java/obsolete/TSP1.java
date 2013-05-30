package obsolete;
import java.util.*;

public class TSP1 {
	static final int INF = 1000000;

	public static void main(String[] args) {
		Random rand = new Random(1);
		for (int steps = 0; steps < 10000; steps++) {
			int n = rand.nextInt(10) + 1;
			int[][] edgeCost = new int[n][n];
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					edgeCost[i][j] = edgeCost[j][i] = rand.nextInt(100);
				}
			}
//			edgeCost[0][1] = edgeCost[1][0] = 1;
//			edgeCost[0][2] = edgeCost[2][0] = 10;
//			edgeCost[1][2] = edgeCost[2][1] = 1;
			int res1 = doDynamic(n, edgeCost);
			int res2 = doBruteForce(n, edgeCost);
			if (res1 != res2) {
				System.out.println(res1 + " " + res2);
			}
		}
	}

	public static boolean nextPermutation(int[] p) {
		int a = p.length - 2;
		while (a >= 0 && p[a] >= p[a + 1]) {
			a--;
		}
		if (a == -1) {
			return false;
		}
		int b = p.length - 1;
		while (p[b] <= p[a]) {
			b--;
		}
		int t = p[a];
		p[a] = p[b];
		p[b] = t;
		for (int i = a + 1, j = p.length - 1; i < j; i++, j--) {
			t = p[i];
			p[i] = p[j];
			p[j] = t;
		}
		return true;
	}

	static int doBruteForce(int n, int[][] edgeCost) {
		int[] p = new int[n];
		for (int i = 0; i < n; i++) {
			p[i] = i;
		}
		int best = Integer.MAX_VALUE;
		do {
			int res = edgeCost[p[n - 1]][p[0]];
			for (int i = 0; i + 1 < n; i++) {
				res += edgeCost[p[i]][p[i + 1]];
			}
			best = Math.min(best, res);
		} while (nextPermutation(p));
		return best;
	}

	static int doDynamic(int n, int[][] edgeCost) {
		int[][] dp = new int[n][1 << n];
		for (int i = 0; i < n; i++) {
			Arrays.fill(dp[i], INF);
		}
		dp[0][0] = 0;
		for (int mask = 0; mask < 1 << n; mask++) {
			for (int next = 0; next < n; next++) {
				if ((mask & 1 << next) == 0) {
					for (int cur = 0; cur < n; cur++) {
						dp[next][mask | 1 << next] = Math.min(dp[next][mask | 1 << next], edgeCost[next][cur]
								+ dp[cur][mask]);
					}
				}
			}
		}
		return dp[0][(1 << n) - 1];
	}
}
