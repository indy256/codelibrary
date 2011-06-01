import java.util.*;

public class DijkstraHeap {

	public static void shortestPaths(List<Edge>[] edges, int s, long[] prio, int[] pred) {
		Arrays.fill(pred, -1);
		Arrays.fill(prio, Long.MAX_VALUE);
		prio[s] = 0;
		Queue<QItem> q = new PriorityQueue<QItem>();
		q.add(new QItem(prio[s], s));
		while (!q.isEmpty()) {
			QItem cur = q.poll();
			if (cur.prio != prio[cur.u])
				continue;
			for (Edge e : edges[cur.u]) {
				int v = e.t;
				long nprio = prio[cur.u] + e.cost;
				if (prio[v] > nprio) {
					prio[v] = nprio;
					pred[v] = cur.u;
					q.add(new QItem(nprio, v));
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

	static class QItem implements Comparable<QItem> {
		long prio;
		int u;

		public QItem(long prio, int u) {
			this.prio = prio;
			this.u = u;
		}

		public int compareTo(QItem q) {
			return prio < q.prio ? -1 : prio > q.prio ? 1 : 0;
		}
	}

	// Usage example
	public static void main(String[] args) {
		int[][] cost = { { 0, 3, 2 }, { 0, 0, -2 }, { 0, 0, 0 } };
		int n = cost.length;
		List<Edge>[] edges = new List[n];
		for (int i = 0; i < n; i++) {
			edges[i] = new ArrayList<Edge>();
			for (int j = 0; j < n; j++) {
				if (cost[i][j] != 0) {
					edges[i].add(new Edge(j, cost[i][j]));
				}
			}
		}
		long[] distances = new long[n];
		int[] pred = new int[n];
		shortestPaths(edges, 0, distances, pred);
		System.out.println(0 == distances[0]);
		System.out.println(3 == distances[1]);
		System.out.println(1 == distances[2]);
		System.out.println(-1 == pred[0]);
		System.out.println(0 == pred[1]);
		System.out.println(1 == pred[2]);
	}
}
