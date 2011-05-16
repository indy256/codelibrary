import java.util.*;

public class RandomGraph {

	static class Node implements Comparable<Node> {
		int degree;
		int num;

		public Node(int degree, int num) {
			this.degree = degree;
			this.num = num;
		}

		@Override
		public int compareTo(Node o) {
			if (degree != o.degree)
				return degree < o.degree ? -1 : 1;
			return num < o.num ? -1 : num > o.num ? 1 : 0;
		}
	}

	public static List<Integer>[] prufer2Tree(int[] a) {
		int n = a.length + 2;
		List<Integer>[] t = new List[n];
		for (int i = 0; i < n; i++) {
			t[i] = new ArrayList<Integer>();
		}
		int[] degree = new int[n];
		for (int x : a) {
			++degree[x];
		}
		PriorityQueue<Node> q = new PriorityQueue<Node>();
		for (int i = 0; i < n; i++) {
			++degree[i];
			q.add(new Node(degree[i], i));
		}
		for (int x : a) {
			Node node = null;
			do {
				node = q.poll();
			} while (node.degree != degree[node.num]);
			t[x].add(node.num);
			t[node.num].add(x);
			--degree[x];
			if (degree[x] >= 1) {
				q.add(new Node(degree[x], x));
			}
		}
		Node u = q.poll();
		Node v = q.poll();
		t[u.num].add(v.num);
		t[v.num].add(u.num);
		return t;
	}

	// precondition: n >= 2
	public static List<Integer>[] getRandomTree(int V, Random rnd) {
		int[] a = new int[V - 2];
		for (int i = 0; i < a.length; i++) {
			a[i] = rnd.nextInt(V);
		}
		return prufer2Tree(a);
	}

	static class Edge {
		int u;
		int v;

		public Edge(int u, int v) {
			this.u = u;
			this.v = v;
		}

		@Override
		public int hashCode() {
			return u * 31 + v;
		}

		@Override
		public boolean equals(Object o) {
			Edge e = (Edge) o;
			return u == e.u && v == e.v;
		}
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

	// precondition: V >= 2, V-1 <= E <= V*(V-1)/2
	public static List<Integer>[] getRandomConnectedGraph(int V, int E, Random rnd) {
		List<Integer>[] g = getRandomTree(V, rnd);
		Set<Edge> edgeSet = new LinkedHashSet<Edge>();
		for (int i = 0; i < V; i++) {
			for (int j = i + 1; j < V; j++) {
				edgeSet.add(new Edge(i, j));
			}
		}
		for (int i = 0; i < V; i++) {
			for (int j : g[i]) {
				edgeSet.remove(new Edge(i, j));
			}
		}
		List<Edge> edges = new ArrayList<Edge>(edgeSet);
		boolean[] used = new boolean[edges.size()];
		for (int x : getRandomCombination(edges.size(), E - (V - 1), rnd)) {
			used[x] = true;
		}
		for (int i = 0; i < edges.size(); i++) {
			if (used[i]) {
				Edge e = edges.get(i);
				g[e.u].add(e.v);
				g[e.v].add(e.u);
			}
		}
		for (int i = 0; i < V; i++)
			Collections.sort(g[i]);
		return g;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(prufer2Tree(new int[] { 3, 3, 3, 4 })));
		System.out.println(Arrays.toString(prufer2Tree(new int[] { 0, 0 })));
	}
}
