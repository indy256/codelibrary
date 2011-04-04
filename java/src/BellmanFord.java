import java.util.*;

public class BellmanFord {

	static final int INF = Integer.MAX_VALUE / 3;

	static class Edge {
		int u, v;
		int cost;

		public Edge(int u, int v, int cost) {
			this.u = u;
			this.v = v;
			this.cost = cost;
		}
	}

	public static boolean bellmanFord(int n, List<Edge> edges, int s, int[] prio, int[] pred) {
		Arrays.fill(pred, -1);
		Arrays.fill(prio, INF);
		prio[s] = 0;
		boolean wasChanged = true;
		for (int k = 0; k < n; k++) {
			wasChanged = false;
			for (Edge e : edges)
				if (prio[e.v] > prio[e.u] + e.cost) {
					prio[e.v] = prio[e.u] + e.cost;
					pred[e.v] = e.u;
					wasChanged = true;
				}
			if (!wasChanged)
				break;
		}
		// wasChanged is true iff graph has a negative cycle
		return wasChanged;
	}

	public static int[] findNegativeCycle(int n, List<Edge> edges) {
		int[] pred = new int[n];
		Arrays.fill(pred, -1);
		int[] prio = new int[n];
		Arrays.fill(prio, INF);
		prio[0] = 0;
		int last = 0;
		for (int k = 0; k < n; k++) {
			last = -1;
			for (Edge e : edges)
				if (prio[e.v] > prio[e.u] + e.cost) {
					prio[e.v] = prio[e.u] + e.cost;
					pred[e.v] = e.u;
					last = e.v;
				}
			if (last == -1)
				return null;
		}
		int[] path = new int[n];
		int[] pos = new int[n];
		for (int i = 0; ; i++) {
			if (pos[last] != 0) {
				int[] cycle = new int[i + 1 - pos[last]];
				for (int j = 0; j < cycle.length; j++)
					cycle[j] = path[i - 1 - j];
				return cycle;
			}
			path[i] = last;
			pos[last] = i + 1;
			last = pred[last];
		}
	}

	// Usage example
	public static void main(String[] args) {
		List<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(0, 1, 1));
		edges.add(new Edge(1, 0, 1));
		edges.add(new Edge(1, 2, 1));
		edges.add(new Edge(2, 3, -10));
		edges.add(new Edge(3, 1, 1));
		int[] cycle = findNegativeCycle(4, edges);
		System.out.println(Arrays.toString(cycle));
	}
}
