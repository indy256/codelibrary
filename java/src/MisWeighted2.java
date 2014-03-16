import java.util.*;

public class MisWeighted2 {

	public static int mis(BitSet[] g, BitSet unused, int[] weights) {
		int res = 0;
		int[][] dp = new int[2][g.length];
		for (int u = unused.nextSetBit(0); u >= 0; u = unused.nextSetBit(u + 1)) {
			BitSet newUnused = (BitSet) unused.clone();
			if (isTree(g, newUnused, u, -1)) {
				misOnTree(g, unused, weights, dp, u, -1);
				res += Math.max(dp[0][u], dp[1][u]);
			}
		}
		boolean deg2 = true;
		for (int u = unused.nextSetBit(0); u >= 0; u = unused.nextSetBit(u + 1)) {
			BitSet nu = (BitSet) g[u].clone();
			nu.and(unused);
			if (nu.cardinality() - 1 > 2) {
				deg2 = false;
				break;
			}
		}
		if (deg2) {
			BitSet newUnused = (BitSet) unused.clone();
			for (int d = 0; d <= 2; d++) {
				for (int u = newUnused.nextSetBit(0); u >= 0; u = newUnused.nextSetBit(u + 1)) {
					BitSet nu = (BitSet) g[u].clone();
					nu.and(newUnused);
					if (nu.cardinality() - 1 == d) {
						List<Integer> w = new ArrayList<>();
						int cur = u;
						do {
							w.add(weights[cur]);
							newUnused.clear(cur);
							BitSet ncur = (BitSet) g[cur].clone();
							ncur.and(newUnused);
							cur = ncur.nextSetBit(0);
						} while (cur != -1);
						res += d <= 1 ? misOnPath(w) : Math.max(misOnPath(w.subList(1, w.size())), misOnPath(w.subList(0, w.size() - 1)));
					}
				}
			}
			return res;
		}

		int v = -1;
		for (int u = unused.nextSetBit(0); u >= 0; u = unused.nextSetBit(u + 1))
			if (v == -1 || g[v].cardinality() < g[u].cardinality())
				v = u;
		if (v == -1)
			return 0;

		BitSet newUnused = (BitSet) unused.clone();
		newUnused.andNot(g[v]);
		int res1 = weights[v] + mis(g, newUnused, weights);

		unused.clear(v);
		int res2 = mis(g, unused, weights);
		unused.set(v);
		return Math.max(res1, res2) + res;
	}

	static boolean isTree(BitSet[] g, BitSet unused, int u, int p) {
		unused.clear(u);
		for (int v = g[u].nextSetBit(0); v >= 0; v = g[u].nextSetBit(u + 1)) {
			if (v == p)
				continue;
			if (!unused.get(v))
				return false;
			if (!isTree(g, unused, v, u))
				return false;
		}
		return true;
	}

	static void misOnTree(BitSet[] g, BitSet unused, int[] weights, int[][] dp, int u, int p) {
		unused.clear(u);
		dp[u][0] = 0;
		dp[u][1] = weights[u];
		for (int v = g[u].nextSetBit(0); v >= 0; v = g[u].nextSetBit(u + 1)) {
			if (v == p)
				continue;
			misOnTree(g, unused, weights, dp, v, u);
			dp[u][0] += Math.max(dp[v][0], dp[v][1]);
			dp[u][1] += dp[v][0];
		}
	}

	static int misOnPath(List<Integer> w) {
		int n = w.size();
		if (n == 1)
			return w.get(0);
		int[] dp = new int[n];
		dp[0] = w.get(0);
		dp[1] = Math.max(w.get(0), w.get(1));
		for (int i = 2; i < n; i++)
			dp[i] = Math.max(dp[i - 1], dp[i - 2] + w.get(i));
		return Math.max(dp[n - 1], dp[n - 2]);
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(16) + 1;
			BitSet[] g = new BitSet[n];
			int[] weights = new int[n];
			for (int i = 0; i < g.length; i++) {
				weights[i] = rnd.nextInt(1000);
				g[i] = new BitSet(n);
				// for convenience of mis()
				g[i].set(i);
			}
			for (int i = 0; i < n; i++)
				for (int j = 0; j < i; j++)
					if (rnd.nextBoolean()) {
						g[i].set(j);
						g[j].set(i);
					}
			BitSet unused = new BitSet();
			unused.set(0, n);
			int res1 = mis(g, unused, weights);
			int res2 = misSlow(g, weights);
			if (res1 != res2)
				throw new RuntimeException();
		}
	}

	static int misSlow(BitSet[] g, int[] weights) {
		int res = 0;
		int n = g.length;
		for (int set = 0; set < 1 << n; set++) {
			boolean ok = true;
			for (int i = 0; i < n; i++)
				for (int j = 0; j < i; j++)
					ok &= (set & (1 << i)) == 0 || (set & (1 << j)) == 0 || !g[i].get(j);
			if (ok) {
				int cur = 0;
				for (int i = 0; i < n; i++)
					if ((set & (1 << i)) != 0)
						cur += weights[i];
				res = Math.max(res, cur);
			}
		}
		return res;
	}
}
