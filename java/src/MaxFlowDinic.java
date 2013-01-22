import java.util.*;

public class MaxFlowDinic {

	static class Edge {
		int t, rev, cap, f;

		public Edge(int t, int rev, int cap) {
			this.t = t;
			this.rev = rev;
			this.cap = cap;
		}
	}

	public static List<Edge>[] createGraph(int nodes) {
		List<Edge>[] graph = new List[nodes];
		for (int i = 0; i < nodes; i++)
			graph[i] = new ArrayList<>();
		return graph;
	}

	public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
		graph[s].add(new Edge(t, graph[t].size(), cap));
		graph[t].add(new Edge(t, graph[s].size() - 1, 0));
	}

	static boolean dinic_bfs(List<Edge>[] graph, int[] dist, int[] Q, int src, int dest) {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int sizeQ = 0;
		Q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (Edge e : graph[u]) {
				if (dist[e.t] < 0 && e.f < e.cap) {
					dist[e.t] = dist[u] + 1;
					Q[sizeQ++] = e.t;
				}
			}
		}
		return dist[dest] >= 0;
	}

	static int dinic_dfs(List<Edge>[] graph, int[] ptr, int[] dist, int dest, int u, int f) {
		if (u == dest)
			return f;
		for (; ptr[u] < graph[u].size(); ++ptr[u]) {
			Edge e = graph[u].get(ptr[u]);
			if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
				int df = dinic_dfs(graph, ptr, dist, dest, e.t, Math.min(f, e.cap - e.f));
				if (df > 0) {
					e.f += df;
					graph[e.t].get(e.rev).f -= df;
					return df;
				}
			}
		}
		return 0;
	}

	public static int maxFlow(List<Edge>[] graph, int src, int dest) {
		int[] ptr = new int[graph.length];
		int[] Q = new int[graph.length];
		int[] dist = new int[graph.length];
		int flow = 0;
		while (dinic_bfs(graph, dist, Q, src, dest)) {
			Arrays.fill(ptr, 0);
			while (true) {
				int df = dinic_dfs(graph, ptr, dist, dest, src, Integer.MAX_VALUE);
				if (df == 0)
					break;
				flow += df;
			}
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		List<Edge>[] graph = createGraph(3);
		addEdge(graph, 0, 1, 3);
		addEdge(graph, 0, 2, 2);
		addEdge(graph, 1, 2, 2);
		System.out.println(4 == maxFlow(graph, 0, 2));
	}
}
