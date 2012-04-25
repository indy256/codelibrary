import java.util.*;

public class MaxFlowEdmondKarp {

	static class Edge {
		int s, t, rev, cap, f;

		public Edge(int s, int t, int rev, int cap) {
			this.s = s;
			this.t = t;
			this.rev = rev;
			this.cap = cap;
		}
	}

	public List<Edge>[] graph;

	public void init(int nodes) {
		graph = new List[nodes];
		for (int i = 0; i < nodes; i++)
			graph[i] = new ArrayList<Edge>();
	}

	public void addEdge(int s, int t, int cap) {
		graph[s].add(new Edge(s, t, graph[t].size(), cap));
		graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
	}

	public int maxFlow(int s, int t) {
		int flow = 0;
		int[] q = new int[graph.length];
		while (true) {
			int qt = 0;
			q[qt++] = s;
			Edge[] pred = new Edge[graph.length];
			for (int qh = 0; qh < qt && pred[t] == null; qh++) {
				int cur = q[qh];
				for (Edge e : graph[cur]) {
					if (pred[e.t] == null && e.cap > e.f) {
						pred[e.t] = e;
						q[qt++] = e.t;
					}
				}
			}
			if (pred[t] == null)
				break;
			int df = Integer.MAX_VALUE;
			for (int u = t; u != s; u = pred[u].s)
				df = Math.min(df, pred[u].cap - pred[u].f);
			for (int u = t; u != s; u = pred[u].s) {
				pred[u].f += df;
				graph[pred[u].t].get(pred[u].rev).f -= df;
			}
			flow += df;
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		MaxFlowEdmondKarp flow = new MaxFlowEdmondKarp();
		flow.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					flow.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == flow.maxFlow(0, 2));
	}
}
