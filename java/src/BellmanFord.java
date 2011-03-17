import java.util.*;

public class BellmanFord {

	static class Edge {
		int t, cost;

		public Edge(int t, int cost) {
			this.t = t;
			this.cost = cost;
		}
	}

	public static boolean bellmanFord(List<Edge>[] edges, int s, int[] prio, int[] pred) {
		Arrays.fill(pred, -1);
		Arrays.fill(prio, Integer.MAX_VALUE);
		prio[s] = 0;
		int n = edges.length;
		boolean wasChanged = true;
		for (int k = 0; k < n && wasChanged; k++) {
			wasChanged = false;
			for (int u = 0; u < n; u++) {
				if (prio[u] == Integer.MAX_VALUE) {
					continue;
				}
				for (Edge e : edges[u]) {
					int nprio = prio[u] + e.cost;
					if (prio[e.t] > nprio) {
						prio[e.t] = nprio;
						pred[e.t] = u;
						wasChanged = true;
					}
				}
			}
		}
		// wasChanged is true iff graph has a negative cycle
		return wasChanged;
	}

	public static int[] findNegativeCycle(List<Edge>[] edges) {
		int n = edges.length;
		int[] pred = new int[n];
		Arrays.fill(pred, -1);
		int[] prio = new int[n];
		Arrays.fill(prio, Integer.MAX_VALUE);
		prio[0] = 0;
		int last = 0;
		for (int k = 0; k < n && last != -1; k++) {
			last = -1;
			for (int u = 0; u < n; u++) {
				if (prio[u] == Integer.MAX_VALUE) {
					continue;
				}
				for (Edge e : edges[u]) {
					int v = e.t;
					int nprio = prio[u] + e.cost;
					if (prio[v] > nprio) {
						prio[v] = nprio;
						pred[v] = u;
						if (last == -1)
							last = v;
					}
				}
			}
		}
		if (last == -1)
			return null;
		int[] path = new int[n + 1];
		int[] pos = new int[n + 1];
		for (int i = 0;; i++) {
			path[i] = last;
			if (pos[last] != 0) {
				int len = i + 1 - pos[last];
				int[] cycle = new int[len];
				for (int j = 0; j < len; j++) {
					cycle[j] = path[i - j];
				}
				return cycle;
			}
			pos[last] = i + 1;
			last = pred[last];
		}
	}

	// Usage example
	public static void main(String[] args) {
		List<Edge>[] edges = new List[4];
		for (int i = 0; i < edges.length; i++) {
			edges[i] = new ArrayList<Edge>();
		}
		edges[0].add(new Edge(1, 1));
		edges[1].add(new Edge(0, 1));
		edges[1].add(new Edge(2, 1));
		edges[2].add(new Edge(3, -10));
		edges[3].add(new Edge(1, 1));
		int[] cycle = findNegativeCycle(edges);
		System.out.println(Arrays.toString(cycle));
	}
}
