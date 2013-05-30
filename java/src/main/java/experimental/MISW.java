package experimental;

import java.util.*;

public class MISW {

	static int result;
	static int bruteInvocations;

	static int[] graphColoring(BitSet[] g) {
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

	static void mis(BitSet[] g, int[] colors, int[] w) {
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
			mis(g, vertices, w[i], colors, b, w);
			b[i] = result;
		}
	}

	static void mis(BitSet[] g, BitSet vertices, int cur, int[] colors, int[] b, int[] w) {
		++bruteInvocations;
		int card = vertices.cardinality();
		if (card == 0) {
			if (result < cur) {
				result = cur;
			}
			return;
		}

		int colorCnt = 0;
		for (int c : colors) {
			colorCnt = Math.max(colorCnt, c);
		}
		++colorCnt;

		int[] ind = new int[card];
		card = 0;
		int[] cnt = new int[colorCnt];
		for (int i = vertices.nextSetBit(0); i >= 0; i = vertices.nextSetBit(i + 1)) {
			ind[card++] = i;
			++cnt[colors[i]];
		}
		++cnt[0];
		for (int i = 1; i < cnt.length; i++) {
			cnt[i] += cnt[i - 1] + 1;
		}
		int sum = 0;
		int[] maxcol = new int[g.length + colorCnt];
		for (int j = ind.length - 1; j >= 0; j--) {
			int i = ind[j];
			int p = --cnt[colors[i]];
			maxcol[p - 1] = Math.max(maxcol[p], w[i]);
			sum += maxcol[p - 1] - maxcol[p];
		}

		for (int i : ind) {
			if (cur + sum <= result)
				return;
			if (b[i] != -1 && cur + b[i] <= result)
				return;
			vertices.clear(i);
			BitSet nv = (BitSet) vertices.clone();
			nv.andNot(g[i]);
			mis(g, nv, cur + w[i], colors, b, w);
			int p = cnt[colors[i]]++;
			sum += maxcol[p] - maxcol[p - 1];
		}
	}

	static void getMIS(BitSet[] g, int[] w) {
		int n = g.length;

		int[] p = new int[n];
		for (int i = 0; i < p.length; i++) {
			p[i] = i;
		}

		for (int i = 0; i + 1 < n; i++) {
			for (int j = 0; j + 1 < n; j++) {
				if (w[p[j]] < w[p[j + 1]]) {
					int t = p[j];
					p[j] = p[j + 1];
					p[j + 1] = t;
				}
			}
		}

		g = perm(g, p);
		w = perm(w, p);

		BitSet vertices = new BitSet(n);
		vertices.set(0, n);
		int[] colors = graphColoring(g);
		int col = 0;
		for (int i : colors) {
			col = Math.max(col, i);
		}
		int[] maxColor = new int[col + 1];
		int[] f = new int[col + 1];
		for (int i = 0; i < n; i++) {
			++f[colors[i]];
			maxColor[colors[i]] = Math.max(maxColor[colors[i]], w[i]);
		}
		// System.out.println(col + " " + Arrays.toString(f));

		for (int i = 0; i < p.length; i++) {
			p[i] = i;
		}
		for (int i = 0; i + 1 < n; i++) {
			for (int j = 0; j + 1 < n; j++) {
				if (w[p[j]] < w[p[j + 1]] || w[p[j]] == w[p[j + 1]]
						&& g[p[j]].cardinality() < g[p[j + 1]].cardinality()) {
					int t = p[j];
					p[j] = p[j + 1];
					p[j + 1] = t;
				}
			}
		}
		colors = perm(colors, p);
		w = perm(w, p);
		g = perm(g, p);

		mis(g, colors, w);
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

	static int referenceMIS(BitSet[] g, int[] w) {
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
			int s = 0;
			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) != 0)
					s += w[i];
			}
			res = Math.max(res, s);
		}
		return res;
	}

	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1; step++) {
			int n = 200;// rnd.nextInt(15) + 1;
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

			int[] w = new int[n];
			for (int i = 0; i < n; i++) {
				w[i] = 1;
//				 w[i] = rnd.nextInt(1000);
			}

			if (step == 113) {
				int z = 1;
			}

			long time = System.currentTimeMillis();
			bruteInvocations = 0;
			getMIS(g, w);
			System.out.println((System.currentTimeMillis() - time) + "ms");
			System.out.println(bruteInvocations + " bruteInvocations");
			System.out.println(result);
			System.out.println();
			// int res2 = referenceMIS(g, w);
			// if (result != res2) {
			// for (int i = 0; i < n; i++) {
			// System.out.println(g[i]);
			// }
			// System.err.println(result);
			// System.err.println(res2);
			// System.out.println(Arrays.toString(w));
			// break;
			// }
		}
	}
}
