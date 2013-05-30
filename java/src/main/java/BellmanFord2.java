import java.util.*;

public class BellmanFord2 {

	static final int INF = Integer.MAX_VALUE / 2;

	public static class Edge {
		int u, v;
		int cost;

		public Edge(int u, int v, int cost) {
			this.u = u;
			this.v = v;
			this.cost = cost;
		}
	}

	public static boolean bellmanFord(int n, List<Edge> edges, int s, int[] dist, int[] pred) {
		Arrays.fill(pred, -1);
		Arrays.fill(dist, INF);
		dist[s] = 0;
		boolean updated = false;
		for (int step = 0; step < n; step++) {
			updated = false;
			for (Edge e : edges) {
				if (dist[e.u] == INF) continue;
				if (dist[e.v] > dist[e.u] + e.cost) {
					dist[e.v] = dist[e.u] + e.cost;
					dist[e.v] = Math.max(dist[e.v], -INF);
					pred[e.v] = e.u;
					updated = true;
				}
			}
			if (!updated)
				break;
		}
		// if updated is true then a negative cycle exists
		return updated == false;
	}

	public static int[] findNegativeCycle(int n, List<Edge> edges) {
		int[] pred = new int[n];
		Arrays.fill(pred, -1);
		int[] dist = new int[n];
		int last = -1;
		for (int step = 0; step < n; step++) {
			last = -1;
			for (Edge e : edges) {
				if (dist[e.u] == INF) continue;
				if (dist[e.v] > dist[e.u] + e.cost) {
					dist[e.v] = dist[e.u] + e.cost;
					dist[e.v] = Math.max(dist[e.v], -INF);
					pred[e.v] = e.u;
					last = e.v;
				}
			}
			if (last == -1)
				return null;
		}
		for (int i = 0; i < n; i++) {
			last = pred[last];
		}
		int[] p = new int[n];
		int cnt = 0;
		for (int u = last; u != last || cnt == 0; u = pred[u]) {
			p[cnt++] = u;
		}
		int[] cycle = new int[cnt];
		for (int i = 0; i < cycle.length; i++) {
			cycle[i] = p[--cnt];
		}
		return cycle;
	}

	// Usage example
	public static void main(String[] args) {
		List<Edge> edges = new ArrayList<>();
		edges.add(new Edge(0, 1, 1));
		edges.add(new Edge(1, 0, 1));
		edges.add(new Edge(1, 2, 1));
		edges.add(new Edge(2, 3, -10));
		edges.add(new Edge(3, 1, 1));
		int[] cycle = findNegativeCycle(4, edges);
		System.out.println(Arrays.toString(cycle));
	}
}
