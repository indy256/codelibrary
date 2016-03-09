import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// https://en.wikipedia.org/wiki/Blossom_algorithm in O(V^3)
public class MaxMatchingEdmonds {

	public static int maxMatching(List<Integer>[] graph) {
		int n = graph.length;
		int[] match = new int[n];
		Arrays.fill(match, -1);
		int[] p = new int[n];
		for (int i = 0; i < n; ++i) {
			if (match[i] == -1) {
				int v = findPath(graph, match, p, i);
				while (v != -1) {
					int pv = p[v];
					int ppv = match[pv];
					match[v] = pv;
					match[pv] = v;
					v = ppv;
				}
			}
		}

		int matches = 0;
		for (int i = 0; i < n; ++i)
			if (match[i] != -1)
				++matches;
		return matches / 2;
	}

	static int findPath(List<Integer>[] graph, int[] match, int[] p, int root) {
		Arrays.fill(p, -1);
		int n = graph.length;
		int[] base = IntStream.range(0, n).toArray();

		boolean[] used = new boolean[n];
		used[root] = true;
		int[] q = new int[n];
		int qt = 0;
		q[qt++] = root;
		for (int qh = 0; qh < qt; qh++) {
			int v = q[qh];

			for (int to : graph[v]) {
				if (base[v] == base[to] || match[v] == to) continue;
				if (to == root || match[to] != -1 && p[match[to]] != -1) {
					int curbase = lca(match, base, p, v, to);
					boolean[] blossom = new boolean[n];
					markPath(match, base, blossom, p, v, curbase, to);
					markPath(match, base, blossom, p, to, curbase, v);
					for (int i = 0; i < n; ++i)
						if (blossom[base[i]]) {
							base[i] = curbase;
							if (!used[i]) {
								used[i] = true;
								q[qt++] = i;
							}
						}
				} else if (p[to] == -1) {
					p[to] = v;
					if (match[to] == -1)
						return to;
					to = match[to];
					used[to] = true;
					q[qt++] = to;
				}
			}
		}
		return -1;
	}

	static void markPath(int[] match, int[] base, boolean[] blossom, int[] p, int v, int b, int children) {
		for (; base[v] != b; v = p[match[v]]) {
			blossom[base[v]] = blossom[base[match[v]]] = true;
			p[v] = children;
			children = match[v];
		}
	}

	static int lca(int[] match, int[] base, int[] p, int a, int b) {
		boolean[] used = new boolean[match.length];
		while (true) {
			a = base[a];
			used[a] = true;
			if (match[a] == -1) break;
			a = p[match[a]];
		}
		while (true) {
			b = base[b];
			if (used[b]) return b;
			b = p[match[b]];
		}
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(10) + 1;
			boolean[][] g = new boolean[n][n];
			List<Integer>[] graph = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					g[i][j] = g[j][i] = rnd.nextBoolean();
					if (g[i][j]) {
						graph[i].add(j);
						graph[j].add(i);
					}
				}
			}
			int res1 = maxMatching(graph);
			int res2 = maxMatchingSlow(g);
			if (res1 != res2) {
				System.err.println(res1 + " " + res2);
			}
		}
	}

	static int maxMatchingSlow(boolean[][] g) {
		int n = g.length;
		boolean[] can = new boolean[1 << n];
		can[0] = true;
		int res = 0;
		for (int mask = 0; mask < can.length; mask++) {
			if (can[mask]) {
				for (int i = 0; i < n; i++) {
					if ((mask & (1 << i)) == 0) {
						for (int j = i + 1; j < n; j++) {
							if ((mask & (1 << j)) == 0) {
								if (g[i][j]) {
									can[mask | (1 << i) | (1 << j)] = true;
									res = Math.max(res, Integer.bitCount(mask) / 2 + 1);
								}
							}
						}
					}
				}
			}
		}
		return res;
	}
}
