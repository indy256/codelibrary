package experimental.spoj;
import java.io.*;
import java.util.*;

public class DYNACON1_2 {

	static class GraphMap {
		Map<Integer, Map<Integer, Integer>> edges = new HashMap<>();

		void add0(int u, int v) {
			Map<Integer, Integer> m = edges.get(u);
			if (m == null) {
				edges.put(u, m = new HashMap<>());
			}
			Integer x = m.get(v);
			if (x == null)
				x = 0;
			m.put(v, x + 1);
		}

		void add(int u, int v) {
			add0(u, v);
			add0(v, u);
		}

		boolean remove0(int u, int v) {
			Map<Integer, Integer> m = edges.get(u);
			if (m == null)
				return false;
			if (!m.containsKey(v))
				return false;
			m.put(v, m.get(v) - 1);
			if (m.get(v) == 0)
				m.remove(v);
			if (m.isEmpty())
				edges.remove(u);
			return true;
		}

		boolean remove(int u, int v) {
			if (!remove0(u, v))
				return false;
			return remove0(v, u);
		}

		Set<Integer> getAdj(int u) {
			if (!edges.containsKey(u))
				return Collections.emptySet();
			return edges.get(u).keySet();
		}
	}

	static class DisjointSets {
		int[] p;
		int[] rank;

		public DisjointSets(int size) {
			p = new int[size];
			for (int i = 0; i < size; i++) {
				p[i] = i;
			}
			rank = new int[size];
		}

		public int root(int x) {
			while (x != p[x])
				x = p[x];
			return x;
		}

		public void unite(int a, int b) {
			a = root(a);
			b = root(b);
			if (a == b)
				return;
			if (rank[a] < rank[b]) {
				p[a] = b;
			} else {
				p[b] = a;
				if (rank[a] == rank[b])
					++rank[a];
			}
		}
	}

	static boolean connected(GraphMap dsGraph, GraphMap deltaGraph, int a, int b, boolean[] vis) {
		vis[a] = true;
		if (a == b)
			return true;
		for (int v : dsGraph.getAdj(a)) {
			if (!vis[v]) {
				if (connected(dsGraph, deltaGraph, v, b, vis))
					return true;
			}
		}
		for (int v : deltaGraph.getAdj(a)) {
			if (!vis[v]) {
				if (connected(dsGraph, deltaGraph, v, b, vis))
					return true;
			}
		}
		return false;
	}

	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter(System.out);

		int n = nextInt();
		Set<Long> g = new HashSet<>();

		int q = nextInt();
		int[] vt = new int[q];
		int[] va = new int[q];
		int[] vb = new int[q];
		boolean[] vis = new boolean[n];

		for (int i = 0; i < q; i++) {
			String type = nextToken();
			vt[i] = "add".equals(type) ? 0 : "rem".equals(type) ? 1 : 2;
			int a = nextInt() - 1;
			int b = nextInt() - 1;
			va[i] = Math.min(a, b);
			vb[i] = Math.max(a, b);
		}

		long t1 = 0;
		long t2 = 0;
		long t3 = 0;
		long t4 = 0;
		long time = 0;

		int block = (int) Math.sqrt(q);

		for (int i = 0; i < q; i += block) {
			List<Long> removedEdges = new ArrayList<>();

			for (int j = i; j < Math.min(i + block, q); j++) {
				if (vt[j] == 1) {
					if (g.remove(((long) va[i] << 32) + vb[i])) {
						removedEdges.add(((long) va[i] << 32) + vb[i]);
					}
				}
			}

			time = System.currentTimeMillis();
			DisjointSets ds = new DisjointSets(n);
			long[] edges = new long[g.size()];
			int edgesCnt = 0;
			for (long e : g) {
				ds.unite((int) (e >>> 32), (int) (e & 0xFFFFFFFF));
				edges[edgesCnt++] = e;
			}
			t1 += System.currentTimeMillis() - time;

			GraphMap deltaGraph = new GraphMap();
			for (long e : removedEdges) {
				deltaGraph.add(ds.root((int) (e >>> 32)), ds.root((int) (e & 0xFFFFFFFF)));
			}

			time = System.currentTimeMillis();
			GraphMap dsGraph = new GraphMap();

			for (long e : edges) {
				int a = ds.root((int) (e >>> 32));
				int b = ds.root((int) (e & 0xFFFFFFFF));

				if (a != b)
					dsGraph.add(a, b);
			}
			t2 += System.currentTimeMillis() - time;

			for (int j = i; j < Math.min(i + block, q); j++) {
				if (vt[j] == 0) {
					if(connected(dsGraph, deltaGraph, ds.root(va[j]), ds.root(vb[j]), vis)){
						System.err.println("ERROR");
						return;
					}
					time = System.currentTimeMillis();
					deltaGraph.add(ds.root(va[j]), ds.root(vb[j]));
					g.add(((long) va[j] << 32) + vb[j]);
					t4 += System.currentTimeMillis() - time;
				} else if (vt[j] == 1) {
					time = System.currentTimeMillis();
					deltaGraph.remove(ds.root(va[j]), ds.root(vb[j]));
					g.remove(((long) va[j] << 32) + vb[j]);
					t4 += System.currentTimeMillis() - time;
				} else {
					time = System.currentTimeMillis();
					Arrays.fill(vis, false);
					String res = connected(dsGraph, deltaGraph, ds.root(va[j]), ds.root(vb[j]), vis) ? "YES" : "NO";
					t3 += System.currentTimeMillis() - time;
					pw.println(res);
				}
			}
		}
		pw.close();
		System.err.println(t1);
		System.err.println(t2);
		System.err.println(t3);
		System.err.println(t4);
	}

	static int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	 static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//	static BufferedReader reader;
//	static {
//		try {
//			reader = new BufferedReader(new InputStreamReader(new FileInputStream("d:/dynacon1_input0.txt")));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
	static StringTokenizer tokenizer;

	static String nextToken() throws IOException {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			tokenizer = new StringTokenizer(reader.readLine());
		}
		return tokenizer.nextToken();
	}
}
