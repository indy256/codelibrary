import java.util.*;

// Search for maximum independent set
// Based on http://web.cecs.pdx.edu/~mperkows/temp/HOM1/findMaxClique.pdf
public class BronKerboshSort {

	static long time;

	static void findMaximumIndependentSet(List<Integer> cur, List<Integer> result, boolean[][] graph, int[] oldSet,
			int ne, int ce) {
		// if(System.currentTimeMillis()-time>1000)return;
		int nod = 0;
		int minnod = ce;
		int fixp = -1;
		int s = -1;

		for (int i = 0; i < ce && minnod != 0; i++) {
			int p = oldSet[i];
			int cnt = 0;
			int pos = -1;

			for (int j = ne; j < ce; j++)
				if (graph[p][oldSet[j]]) {
					if (++cnt == minnod)
						break;
					pos = j;
				}

			if (minnod > cnt) {
				minnod = cnt;
				fixp = p;
				if (i < ne) {
					s = pos;
				} else {
					s = i;
					nod = 1;
				}
			}
		}

		int[] newSet = new int[ce];

		for (int k = minnod + nod; k >= 1; k--) {
			int sel = oldSet[s];
			oldSet[s] = oldSet[ne];
			oldSet[ne] = sel;

			int newne = 0;
			for (int i = 0; i < ne; i++)
				if (!graph[sel][oldSet[i]])
					newSet[newne++] = oldSet[i];

			int newce = newne;
			for (int i = ne + 1; i < ce; i++)
				if (!graph[sel][oldSet[i]])
					newSet[newce++] = oldSet[i];

			cur.add(sel);
			if (newce == 0) {
				if (result.size() < cur.size()) {
					result.clear();
					result.addAll(cur);
				}
			} else if (newne < newce) {
				if (cur.size() + newce - newne > result.size())
					findMaximumIndependentSet(cur, result, graph, newSet, newne, newce);
			}

			cur.remove(cur.size() - 1);
			if (k > 1)
				for (s = ++ne; !graph[fixp][oldSet[s]]; s++)
					;
		}
	}

	public static List<Integer> maximumIndependentSet(boolean[][] graph) {
		int n = graph.length;
		int[] all = new int[n];
		for (int i = 0; i < n; i++)
			all[i] = i;
		List<Integer> res = new ArrayList<>();
		findMaximumIndependentSet(new ArrayList<Integer>(), res, graph, all, 0, n);
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		time = System.currentTimeMillis();
		Random rnd = new Random(1);
		int V = 20;
		int E = V * (V - 1) / 2 / 5;
		System.out.println(V + " " + E);
		List<Integer>[] graph = RandomGraph.getRandomUndirectedConnectedGraph(V, E, rnd);
		long time = System.currentTimeMillis();

		graph = sortVertices(graph);
		System.out.println(Arrays.toString(graph));

		List<Integer> mis = maximumIndependentSet(convert(graph));
		System.out.println(System.currentTimeMillis() - time);
		System.out.println(mis.size() + " " + mis);
	}

	static List<Integer>[] sortVertices(List<Integer>[] graph) {
		int V = graph.length;
		Graph g = new Graph();
		Graph g1 = new Graph();
		for (int i = 0; i < graph.length; i++) {
			for (int j : graph[i]) {
				g.addEdge(i, j);
				g1.addEdge(i, j);
			}
		}
		int[] p = new int[V];
		for (int i = 0; i < V; i++) {
			int max = Integer.MIN_VALUE;
			int maxv = -1;
			for (int v : g.edges.keySet()) {
				if (max < g.edges.get(v).size()) {
					max = g.edges.get(v).size();
					maxv = v;
				}
			}
			p[i] = maxv;
			g.removeNode(maxv);
		}
		int[] rp = new int[V];
		for (int i = 0; i < V; i++) {
			rp[p[i]] = i;
		}
		graph = new List[V];
		for (int i = 0; i < V; i++) {
			graph[i] = new ArrayList<>();
		}
		for (int i = 0; i < V; i++) {
			for (int j : g1.edges.get(p[i])) {
				graph[i].add(rp[j]);
			}
		}
		return graph;
	}

	static boolean[][] convert(List<Integer>[] graph) {
		int n = graph.length;
		boolean[][] a = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			for (int j : graph[i]) {
				a[i][j] = true;
			}
		}
		return a;
	}
}
