package obsolete;
import java.util.*;

public class MinCostFlow2 {

	// input: g - graph, s - source, t - sink
	// output: maximum flow and minimum cost
	public static long[] minCostFlow(Graph g, int s, int t) {
		final long INF_D = 100000000000L;
		long flow = 0;
		long flowCost = 0;
		long[] u = new long[g.n];
		while (true) {
			Queue<QItem> q = new PriorityQueue<QItem>();
			q.add(new QItem(0, s));
			Edge[] p = new Edge[g.n];
			long[] prio = new long[g.n];
			Arrays.fill(prio, INF_D);
			prio[s] = 0;
			while (!q.isEmpty()) {
				QItem cur = q.poll();
				if (cur.prio != prio[cur.v]) {
					continue;
				}
				for (Edge e : g.edges[cur.v]) {
					long nprio = prio[cur.v] + e.cost + u[cur.v] - u[e.t];
					if (e.cap > e.f && prio[e.t] > nprio) {
						prio[e.t] = nprio;
						p[e.t] = e;
						q.add(new QItem(nprio, e.t));
					}
				}
			}
			if (p[t] == null) {
				break;
			}
			for (int i = 0; i < g.n; i++) {
				if (p[i] != null) {
					u[i] += prio[i];
				}
			}
			Path path = getPath(p, s, t);
			for (Edge e : path.edges) {
				e.f += path.f;
				g.edges[e.t].get(e.rev).f -= path.f;
				flowCost += path.f * e.cost;
			}
			flow += path.f;
		}
		return new long[] { flow, flowCost };
	}

	public static class QItem implements Comparable<QItem> {
		long prio;
		int v;

		public QItem(long prio, int v) {
			this.prio = prio;
			this.v = v;
		}

		public int compareTo(QItem q) {
			return prio < q.prio ? -1 : prio > q.prio ? 1 : 0;
		}
	}

	public static class Graph {
		public final int n;
		public List<Edge>[] edges;

		public Graph(int n) {
			this.n = n;
			edges = new List[n];
			for (int i = 0; i < n; i++) {
				edges[i] = new ArrayList<>();
			}
		}

		void addEdge(int s, int t, int cap, int cost) {
			edges[s].add(new Edge(s, t, edges[t].size(), cap, cost));
			edges[t].add(new Edge(t, s, edges[s].size() - 1, 0, -cost));
		}

		void clearFlow() {
			for (List<Edge> edgesList : edges) {
				for (Edge e : edgesList) {
					e.f = 0;
				}
			}
		}
	}

	public static class Edge {
		public int s, t, rev, cap, f, cost;

		public Edge(int s, int t, int rev, int cap, int cost) {
			this.s = s;
			this.t = t;
			this.rev = rev;
			this.cap = cap;
			this.cost = cost;
		}
	}

	public static class Path {
		public List<Edge> edges = new ArrayList<>();
		public int f;
	}

	public static Path getPath(Edge[] p, int s, int t) {
		Path path = new Path();
		path.f = Integer.MAX_VALUE;
		for (; t != s; t = p[t].s) {
			path.edges.add(p[t]);
			path.f = Math.min(path.f, p[t].cap - p[t].f);
		}
		return path;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		Graph g = new Graph(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (capacity[i][j] != 0) {
					g.addEdge(i, j, capacity[i][j], 1);
				}
			}
		}
		int s = 0;
		int t = 2;
		long[] res = minCostFlow(g, s, t);
		long flow = res[0];
		long flowCost = res[1];
		System.out.println(flow == 4);
		System.out.println(flowCost == 6);
	}
}
