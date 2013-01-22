package obsolete;

import java.util.*;

public class MinCostFlow1 {

	static final int INF = Integer.MAX_VALUE / 2;

	List<Edge>[] graph;

	void init(int n) {
		graph = new List[n];
		for (int i = 0; i < n; i++) {
			graph[i] = new ArrayList<>();
		}
	}

	void addEdge(int s, int t, int cap, int cost) {
		graph[s].add(new Edge(s, t, graph[t].size(), cap, cost));
		graph[t].add(new Edge(t, s, graph[s].size() - 1, 0, -cost));
	}

	static class Edge {
		public int u, v, rev, cap, f, cost;

		public Edge(int u, int v, int rev, int cap, int cost) {
			this.u = u;
			this.v = v;
			this.rev = rev;
			this.cap = cap;
			this.cost = cost;
		}
	}

	static class Path {
		public List<Edge> edges = new ArrayList<>();
		public int f;
	}

	static Path getPath(Edge[] p, int s, int t, int maxf) {
		Path path = new Path();
		path.f = maxf;
		for (; t != s; t = p[t].u) {
			path.edges.add(p[t]);
			path.f = Math.min(path.f, p[t].cap - p[t].f);
		}
		return path;
	}

	static class QItem implements Comparable<QItem> {
		int prio;
		int v;

		public QItem(int prio, int v) {
			this.prio = prio;
			this.v = v;
		}

		public int compareTo(QItem q) {
			return prio < q.prio ? -1 : prio > q.prio ? 1 : 0;
		}
	}

	boolean bellmanFord(List<Edge>[] graph, int s, int[] dist) {
		Arrays.fill(dist, INF);
		dist[s] = 0;
		int n = graph.length;
		boolean updated = false;
		for (int step = 0; step < n; step++) {
			updated = false;
			for (int u = 0; u < n; u++) {
				if (dist[u] < INF) {
					for (Edge e : graph[u]) {
						if (e.cap > 0 && dist[e.v] > dist[u] + e.cost) {
							dist[e.v] = Math.max(dist[u] + e.cost, -INF);
							updated = true;
						}
					}
				}
			}
			if (!updated)
				break;
		}
		// if updated is true then a negative cycle exists
		return updated == false;
	}

	public int[] minCostFlow(int s, int t, int maxf) {
		int[] pot = new int[graph.length];
		if (!bellmanFord(graph, s, pot)) {
			throw new IllegalArgumentException("Negative cycles are not supported");
		}
		int flow = 0;
		int flowCost = 0;
		while (flow < maxf) {
			Queue<QItem> q = new PriorityQueue<QItem>();
			q.add(new QItem(0, s));
			Edge[] p = new Edge[graph.length];
			int[] prio = new int[graph.length];
			Arrays.fill(prio, INF);
			prio[s] = 0;
			while (!q.isEmpty()) {
				QItem cur = q.poll();
				if (cur.prio != prio[cur.v]) {
					continue;
				}
				for (Edge e : graph[cur.v]) {
					int nprio = prio[cur.v] + e.cost + pot[cur.v] - pot[e.v];
					if (e.cap > e.f && prio[e.v] > nprio) {
						prio[e.v] = nprio;
						p[e.v] = e;
						q.add(new QItem(nprio, e.v));
					}
				}
			}
			if (p[t] == null) {
				break;
			}
			for (int i = 0; i < graph.length; i++) {
				if (p[i] != null) {
					pot[i] += prio[i];
				}
			}
			Path path = getPath(p, s, t, maxf);
			for (Edge e : path.edges) {
				e.f += path.f;
				graph[e.v].get(e.rev).f -= path.f;
				flowCost += path.f * e.cost;
			}
			flow += path.f;
		}
		return new int[] { flow, flowCost };
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		MinCostFlow1 f = new MinCostFlow1();
		f.init(n);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (capacity[i][j] != 0) {
					f.addEdge(i, j, capacity[i][j], 1);
				}
			}
		}
		int s = 0;
		int t = 2;
		int[] res = f.minCostFlow(s, t, Integer.MAX_VALUE);
		int flow = res[0];
		int flowCost = res[1];
		System.out.println(flow == 4);
		System.out.println(flowCost == 6);
	}
}
