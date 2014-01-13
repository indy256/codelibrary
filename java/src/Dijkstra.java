import java.util.*;

public class Dijkstra {

	public static void shortestPaths(List<Edge>[] edges, int s, int[] dist, int[] pred) {
		int n = dist.length;
		Arrays.fill(pred, -1);
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[s] = 0;
		boolean[] visited = new boolean[n];
		for (int i = 0; i < n; i++) {
			int u = -1;
			for (int j = 0; j < n; j++) {
				if (!visited[j] && (u == -1 || dist[u] > dist[j]))
					u = j;
			}
			if (dist[u] == Integer.MAX_VALUE)
				continue;
			visited[u] = true;

			for (Edge e : edges[u]) {
				int v = e.t;
				int nprio = dist[u] + e.cost;
				if (dist[v] > nprio) {
					dist[v] = nprio;
					pred[v] = u;
				}
			}
		}
	}

	static class Edge {
		int t, cost;

		public Edge(int t, int cost) {
			this.t = t;
			this.cost = cost;
		}
	}

	// Usage example
	public static void main(String[] args) {
		int[][] cost = {{0, 3, 2}, {0, 0, -2}, {0, 0, 0}};
		int n = cost.length;
		List<Edge>[] edges = new List[n];
		for (int i = 0; i < n; i++) {
			edges[i] = new ArrayList<>();
			for (int j = 0; j < n; j++) {
				if (cost[i][j] != 0) {
					edges[i].add(new Edge(j, cost[i][j]));
				}
			}
		}
		int[] dist = new int[n];
		int[] pred = new int[n];
		shortestPaths(edges, 0, dist, pred);
		System.out.println(0 == dist[0]);
		System.out.println(3 == dist[1]);
		System.out.println(1 == dist[2]);
		System.out.println(-1 == pred[0]);
		System.out.println(0 == pred[1]);
		System.out.println(1 == pred[2]);
	}
}
