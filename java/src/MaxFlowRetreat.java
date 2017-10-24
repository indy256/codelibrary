import java.util.*;
import java.util.stream.Stream;

public class MaxFlowRetreat {

	static class Edge {
		int s, t, rev, cap, f;

		public Edge(int s, int t, int rev, int cap) {
			this.s = s;
			this.t = t;
			this.rev = rev;
			this.cap = cap;
		}
	}

	public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
		graph[s].add(new Edge(s, t, graph[t].size(), cap));
		graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
	}

	static void revBfs(List<Edge>[] graph, int s, int t, int[] dist) {
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[t] = 0;
		int[] Q = new int[graph.length];
		int sizeQ = 0;
		Q[sizeQ++] = t;

		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (Edge e : graph[u]) {
				int v = e.t;
				if (dist[v] == Integer.MAX_VALUE) {
					dist[v] = dist[u] + 1;
					Q[sizeQ++] = v;
				}
			}
		}
	}

	static Edge advance(List<Edge>[] graph, int[] dist, int[] ptr, int u) {
		for (; ptr[u] < graph[u].size(); ++ptr[u]) {
			Edge e = graph[u].get(ptr[u]);
			if (dist[u] == dist[e.t] + 1 && e.cap - e.f > 0)
				return e;
		}
		ptr[u] = 0;
		return null;
	}

	static int retreat(List<Edge>[] graph, int[] dist, int[] freq, Edge[] pred, int u) {
		if (--freq[dist[u]] == 0)
			return -1;

		int minDist = Integer.MAX_VALUE;

		for (Edge e : graph[u]) {
			if (e.cap - e.f > 0 && minDist > dist[e.t])
				minDist = dist[e.t];
		}

		if (minDist != Integer.MAX_VALUE)
			dist[u] = minDist;
		++freq[++dist[u]];

		return pred[u] == null ? u : pred[u].s;
	}

	static int augment(List<Edge>[] graph, Edge[] pred, int s, int t) {
		int df = Integer.MAX_VALUE;
		for (int u = t; u != s; u = pred[u].s)
			df = Math.min(df, pred[u].cap - pred[u].f);
		for (int u = t; u != s; u = pred[u].s) {
			pred[u].f += df;
			graph[pred[u].t].get(pred[u].rev).f -= df;
		}
		return df;
	}

	public static int maxFlow(List<Edge>[] graph, int s, int t) {
		int n = graph.length;
		int[] dist = new int[n];
		revBfs(graph, s, t, dist);
		int[] freq = new int[n];
		for (int i = 0; i < n; i++) {
			if (dist[i] != Integer.MAX_VALUE)
				++freq[dist[i]];
		}
		int[] ptr = new int[n];
		Edge[] pred = new Edge[n];
		int flow = 0;

		for (int u = s; dist[s] < n && u != -1; ) {
			Edge e = advance(graph, dist, ptr, u);
			if (e != null) {
				u = e.t;
				pred[u] = e;
				if (u == t) {
					flow += augment(graph, pred, s, t);
					u = s;
				}
			} else {
				u = retreat(graph, dist, freq, pred, u);
			}
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = {{0, 3, 2}, {0, 0, 2}, {0, 0, 0}};
		int n = capacity.length;
		List<Edge>[] graph = Stream.generate(ArrayList::new).limit(n).toArray(List[]::new);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					addEdge(graph, i, j, capacity[i][j]);
		System.out.println(4 == maxFlow(graph, 0, 2));
	}
}
