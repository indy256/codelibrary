package experimental;

import java.util.*;

public class MISHugePruning {

	static int result = 0;
	static int bruteInvocations = 0;

	static BitSet[] used = new BitSet[256];
	static boolean[] colored = new boolean[256];

	static {
		for (int i = 0; i < used.length; i++) {
			used[i] = new BitSet();
		}
	}

	static class FastPriorityQueue {
		int[] h;
		int n;

		public FastPriorityQueue(int initialSize) {
			h = new int[initialSize];
		}

		public void add(int value) {
			if (n == h.length) {
				h = Arrays.copyOf(h, h.length * 2);
			}
			++n;
			int k = n - 1;
			while (k > 0) {
				int parent = (k - 1) >> 1;
				int p = h[parent];
				if (value >= p) {
					break;
				}
				h[k] = p;
				k = parent;
			}
			h[k] = value;
		}

		public int remove() {
			int res = h[0];
			--n;
			percolateDown(0, h[n]);
			return res;
		}

		void percolateDown(int k, int value) {
			if (n == 0) {
				return;
			}
			int x = n >> 1;
			while (k < x) {
				int child = (k << 1) + 1;
				if (child < n - 1 && h[child] > h[child + 1]) {
					++child;
				}
				if (value <= h[child]) {
					break;
				}
				h[k] = h[child];
				k = child;
			}
			h[k] = value;
		}
	}

	public static int misUpperBound(BitSet[] g, BitSet[] rg, BitSet vertices) {
		FastPriorityQueue q = new FastPriorityQueue(vertices.cardinality());
		for (int i = vertices.nextSetBit(0); i >= 0; i = vertices.nextSetBit(i + 1)) {
			used[i].clear();
			colored[i] = false;
			q.add(i);
		}
		int colors = 0;
		for (int i = vertices.nextSetBit(0); i >= 0; i = vertices.nextSetBit(i + 1)) {
			int bestu;
			while (true) {
				bestu = (short) q.remove();
				if (!colored[bestu])
					break;
			}
			int c = used[bestu].nextClearBit(0);
			colors = Math.max(colors, c + 1);
			colored[bestu] = true;
			BitSet vvv = (BitSet) rg[bestu].clone();
			vvv.and(vertices);
			for (int v = vvv.nextSetBit(0); v >= 0; v = vvv.nextSetBit(v + 1)) {
				if (!used[v].get(c)) {
					used[v].set(c);
					if (!colored[v])
						q.add(v - (used[v].cardinality() << 16));
				}
			}
		}
		return colors;
	}

	static void mis(BitSet[] g, BitSet[] rg, BitSet vertices, int cur) {
		++bruteInvocations;
		result = Math.max(result, cur);

		int upperBound;
		upperBound = misUpperBound(g, rg, vertices);
		// upperBound = vertices.cardinality();
		if (cur + upperBound <= result)
			return;

		int besti = -1;
		int bestd = -1;

		for (int i = vertices.nextSetBit(0); i >= 0; i = vertices.nextSetBit(i + 1)) {
			BitSet a = (BitSet) g[i].clone();
			a.and(vertices);
			int deg = a.cardinality();
			if (bestd < deg) {
				bestd = deg;
				besti = i;
			}
		}

		BitSet tmp = (BitSet) vertices.clone();

		vertices.clear(besti);
		mis(g, rg, vertices, cur);

		vertices.andNot(g[besti]);
		mis(g, rg, vertices, cur + 1);

		vertices.clear();
		vertices.or(tmp);
	}

	public static void main(String[] args) {
		int n = 200;

		BitSet[] g = new BitSet[n];
		for (int i = 0; i < g.length; i++) {
			g[i] = new BitSet(n);
		}

		Random rnd = new Random(1);
		for (int i = 0; i < n * (n - 1) / 2 * 40 / 100; i++) {
			int u = rnd.nextInt(n - 1) + 1;
			int v = rnd.nextInt(u);
			g[u].set(v);
			g[v].set(u);
		}

		BitSet[] rg = new BitSet[n];
		for (int i = 0; i < n; i++) {
			rg[i] = (BitSet) g[i].clone();
			rg[i].flip(0, n);
			rg[i].clear(i);
		}

		BitSet vertices = new BitSet(n);
		vertices.set(0, n);

		long time = System.currentTimeMillis();
		mis(g, rg, vertices, 0);
		System.out.println((System.currentTimeMillis() - time) + "ms");
		System.out.println(bruteInvocations + " bruteInvocations");

		System.out.println(result);
	}
}
