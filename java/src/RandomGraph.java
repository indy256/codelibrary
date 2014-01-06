import java.util.*;

public class RandomGraph {

	public static List<Integer>[] getRandomTree2(int n, Random rnd) {
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++)
			t[i] = new ArrayList<>();
		int[] p = new int[n];
		for (int i = 0, j; i < n; j = rnd.nextInt(i + 1), p[i] = p[j], p[j] = i, i++) ; // random permutation
		for (int i = 1; i < n; i++) {
			int parent = p[rnd.nextInt(i)];
			t[parent].add(p[i]);
			t[p[i]].add(parent);
		}
		return t;
	}

	public static List<Integer>[] pruferCode2Tree(int[] a) {
		int n = a.length + 2;
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++) {
			t[i] = new ArrayList<>();
		}
		int[] degree = new int[n];
		for (int x : a) {
			++degree[x];
		}
		PriorityQueue<Long> q = new PriorityQueue<>();
		for (int i = 0; i < n; i++) {
			++degree[i];
			q.add(((long) degree[i] << 32) + i);
		}
		for (int x : a) {
			int num = 0;
			int deg = 0;
			do {
				long node = q.poll();
				deg = (int) (node >>> 32);
				num = (int) node;
			} while (deg != degree[num]);
			t[x].add(num);
			t[num].add(x);
			--degree[x];
			if (degree[x] >= 1) {
				q.add(((long) degree[x] << 32) + x);
			}
		}
		int u = q.poll().intValue();
		int v = q.poll().intValue();
		t[u].add(v);
		t[v].add(u);
		return t;
	}

	// precondition: n >= 2
	public static List<Integer>[] getRandomTree(int V, Random rnd) {
		int[] a = new int[V - 2];
		for (int i = 0; i < a.length; i++) {
			a[i] = rnd.nextInt(V);
		}
		return pruferCode2Tree(a);
	}

	// precondition: V >= 2, V-1 <= E <= V*(V-1)/2
	public static List<Integer>[] getRandomUndirectedConnectedGraph(int V, int E, Random rnd) {
		List<Integer>[] g = getRandomTree(V, rnd);
		Set<Long> edgeSet = new LinkedHashSet<>();
		for (int i = 0; i < V; i++) {
			for (int j = i + 1; j < V; j++) {
				edgeSet.add(((long) i << 32) + j);
			}
		}
		for (int i = 0; i < V; i++) {
			for (int j : g[i]) {
				edgeSet.remove(((long) i << 32) + j);
			}
		}
		List<Long> edges = new ArrayList<>(edgeSet);
		for (int x : getRandomCombination(edges.size(), E - (V - 1), rnd)) {
			long e = edges.get(x);
			int u = (int) (e >>> 32);
			int v = (int) e;
			g[u].add(v);
			g[v].add(u);
		}
		for (int i = 0; i < V; i++)
			Collections.sort(g[i]);
		return g;
	}

	// precondition: V >= 2, V-1 <= E <= V*(V-1)/2
	public static List<Integer>[] getRandomUndirectedConnectedGraph2(int V, int E, Random rnd) {
		List<Integer>[] g = getRandomTree(V, rnd);
		Set<Long> edgeSet = new LinkedHashSet<>();
		for (int i = 0; i < V; i++) {
			for (int j : g[i]) {
				edgeSet.add(((long) i << 32) + j);
			}
		}
		for (int i = 0; i < E - (V - 1); i++) {
			int u;
			int v;
			long edge;
			while (true) {
				u = rnd.nextInt(V);
				v = rnd.nextInt(V);
				edge = ((long) u << 32) + v;
				if (u < v && !edgeSet.contains(edge)) break;
			}
			edgeSet.add(edge);
			g[u].add(v);
			g[v].add(u);
		}
		for (int i = 0; i < V; i++)
			Collections.sort(g[i]);
		return g;
	}

	static int[] getRandomCombination(int n, int m, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++) {
			res[i] = i;
		}
		for (int i = 0; i < m; i++) {
			int j = n - 1 - rnd.nextInt(n - i);
			int t = res[i];
			res[i] = res[j];
			res[j] = t;
		}
		return Arrays.copyOf(res, m);
	}

	// Usage example
	public static void main(String[] args) {
		System.out.println(Arrays.toString(pruferCode2Tree(new int[]{3, 3, 3, 4})));
		System.out.println(Arrays.toString(pruferCode2Tree(new int[]{0, 0})));

		Random rnd = new Random(1);
		for (int step = 0; step < 1000; step++) {
			int V = rnd.nextInt(50) + 2;
			checkGraph(V, V - 1, rnd);
			checkGraph(V, V * (V - 1) / 2, rnd);
			checkGraph(V, rnd.nextInt(V * (V - 1) / 2 - (V - 1) + 1) + V - 1, rnd);
		}
	}

	static void checkGraph(int V, int E, Random rnd) {
		List<Integer>[] g = getRandomUndirectedConnectedGraph(V, E, rnd);
		int n = g.length;
		int[][] a = new int[n][n];
		int edges = 0;
		for (int i = 0; i < n; i++) {
			for (int j : g[i]) {
				++a[i][j];
				++edges;
			}
		}
		if (edges != 2 * E) {
			throw new RuntimeException();
		}
		for (int i = 0; i < n; i++) {
			if (a[i][i] != 0) {
				throw new RuntimeException();
			}
			for (int j = 0; j < n; j++) {
				if (a[i][j] != a[j][i] || a[i][j] != 0 && a[i][j] != 1) {
					throw new RuntimeException();
				}
			}
		}
	}
}
