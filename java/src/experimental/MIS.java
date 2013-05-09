package experimental;

import java.util.*;

public class MIS {

	static int result;
	static int bruteInvocations;
	static boolean found;

	public static int[] graphColoring(BitSet[] g) {
		int n = g.length;
		BitSet[] used = new BitSet[n];
		for (int i = 0; i < n; i++) {
			used[i] = new BitSet(n);
		}
		int[] colors = new int[n];
		Arrays.fill(colors, -1);
		for (int i = 0; i < n; i++) {
			int best_cnt = -1;
			int bestu = -1;
			for (int u = 0; u < n; u++) {
				if (colors[u] == -1) {
					int cnt = used[u].cardinality();
					if (best_cnt < cnt) {
						best_cnt = cnt;
						bestu = u;
					}
				}
			}
			int c = used[bestu].nextClearBit(0);
			colors[bestu] = c;
			for (int v = g[bestu].nextClearBit(0); v < n; v = g[bestu].nextClearBit(v + 1)) {
				used[v].set(c);
			}
		}
		return colors;
	}

	static void mis(BitSet[] g, int[] colors) {
		result = 0;
		int n = g.length;
		int[] b = new int[n];
		Arrays.fill(b, -1);
		BitSet S = new BitSet(n);
		for (int i = n - 1; i >= 0; i--) {
			S.set(i);
			BitSet vertices = (BitSet) S.clone();
			vertices.andNot(g[i]);
			vertices.clear(i);
			found = false;
			mis(g, vertices, 1, colors, b);
			if (i == 0 || colors[i] != colors[i - 1])
				b[colors[i]] = result;
		}
	}

	static void mis(BitSet[] g, BitSet vertices, int cur, int[] colors, int[] b) {
		++bruteInvocations;
		int card = vertices.cardinality();

		if (card == 0) {
			if (result < cur) {
				result = cur;
				found = true;
			}
			return;
		}

		int[] ind = new int[card];
		card = 0;
		boolean[] colorSet = new boolean[g.length/* colors[0] + 1 */];
		int degree = 1;
		for (int i = vertices.nextSetBit(0); i >= 0; i = vertices.nextSetBit(i + 1)) {
			if (!colorSet[colors[i]]) {
				++degree;
				colorSet[colors[i]] = true;
			}
			ind[card++] = i;
		}
		int previ = -1;
		for (int i : ind) {
			if (colors[i] != previ) {
				--degree;
				previ = colors[i];
			}
			if (cur + degree <= result)
				return;
			if (b[colors[i]] != -1 && cur + b[colors[i]] <= result)
				return;
			vertices.clear(i);
			BitSet nv = (BitSet) vertices.clone();
			nv.andNot(g[i]);
			mis(g, nv, cur + 1, colors, b);
//			if (found)
//				return;
		}
	}

	static void getMIS(BitSet[] g) {
		int n = g.length;

		int[] p = new int[n];
		for (int i = 0; i < p.length; i++) {
			p[i] = i;
		}

		for (int i = 0; i + 1 < n; i++) {
			for (int j = 0; j + 1 < n; j++) {
				if (g[p[j]].cardinality() > g[p[j + 1]].cardinality()) {
					int t = p[j];
					p[j] = p[j + 1];
					p[j + 1] = t;
				}
			}
		}

		g = perm(g, p);

		BitSet vertices = new BitSet(n);
		vertices.set(0, n);
		int[] colors = graphColoring(g);
		int col = 0;
		for (int i : colors) {
			col = Math.max(col, i);
		}
		int[] f = new int[col + 1];
		for (int i = 0; i < n; i++) {
			++f[colors[i]];
		}
		System.out.println(col + " " + Arrays.toString(f));

		for (int i = 0; i < p.length; i++) {
			p[i] = i;
		}
		for (int i = 0; i + 1 < n; i++) {
			for (int j = 0; j + 1 < n; j++) {
				if (colors[p[j]] < colors[p[j + 1]] || colors[p[j]] == colors[p[j + 1]]
						&& g[p[j]].cardinality() > g[p[j + 1]].cardinality()) {
					int t = p[j];
					p[j] = p[j + 1];
					p[j + 1] = t;
				}
			}
		}
		colors = perm(colors, p);
		g = perm(g, p);

		mis(g, colors);
	}

	static BitSet[] perm(BitSet[] g, int[] p) {
		int n = p.length;
		BitSet[] res = new BitSet[n];
		for (int i = 0; i < n; i++) {
			res[i] = new BitSet(n);
			for (int j = 0; j < n; j++) {
				res[i].set(j, g[p[i]].get(p[j]));
			}
		}
		return res;
	}

	static int[] perm(int[] a, int[] p) {
		int n = p.length;
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = a[p[i]];
		}
		return res;
	}

	static int referenceMIS(BitSet[] g) {
		int n = g.length;
		int res = 0;
		m1: for (int mask = 0; mask < 1 << n; mask++) {
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if ((mask & (1 << i)) != 0 && (mask & (1 << j)) != 0 && g[i].get(j)) {
						continue m1;
					}
				}
			}
			res = Math.max(res, Integer.bitCount(mask));
		}
		return res;
	}

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1; step++) {
			int n = 200;// rnd.nextInt(10) + 1;
			BitSet[] g = new BitSet[n];
			for (int i = 0; i < g.length; i++) {
				g[i] = new BitSet(n);
			}

			for (int i = 0; i < n * (n - 1) / 2 * 40 / 100; i++) {
				int u = rnd.nextInt(n - 1) + 1;
				int v = rnd.nextInt(u);
				g[u].set(v);
				g[v].set(u);
			}

			if (step == 6) {
				int z = 1;
			}

			long time = System.currentTimeMillis();
			bruteInvocations = 0;
			getMIS(g);
			System.out.println((System.currentTimeMillis() - time) + "ms");
			System.out.println(bruteInvocations + " bruteInvocations");
			System.out.println(result);
			// int res2 = getMIS2(g);
			// if (result != res2) {
			// for (int i = 0; i < n; i++) {
			// System.out.println(g[i]);
			// }
			// System.err.println(result);
			// System.err.println(res2);
			// break;
			// }
		}
	}
}
