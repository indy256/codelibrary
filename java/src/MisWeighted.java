import java.util.*;

public class MisWeighted {

	public static int mis(BitSet[] g, BitSet unused, int[] weights) {
		if (unused.cardinality() == 0)
			return 0;
		int v = -1;
		for (int u = unused.nextSetBit(0); u >= 0; u = unused.nextSetBit(u + 1))
			if (v == -1 || g[v].cardinality() > g[u].cardinality())
				v = u;
		int res = 0;
		BitSet nv = (BitSet) g[v].clone();
		nv.and(unused);
		for (int y = nv.nextSetBit(0); y >= 0; y = nv.nextSetBit(y + 1)) {
			BitSet newUnused = (BitSet) unused.clone();
			newUnused.andNot(g[y]);
			res = Math.max(res, weights[y] + mis(g, newUnused, weights));
		}
		return res;
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
