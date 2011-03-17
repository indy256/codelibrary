import java.util.*;

public class PrimHeap {

	public static long mst(List<Edge>[] edges, int[] pred) {
		int n = edges.length;
		Arrays.fill(pred, -1);
		boolean[] vis = new boolean[n];
		int[] prio = new int[n];
		Arrays.fill(prio, Integer.MAX_VALUE);
		prio[0] = 0;
		Queue<QItem> q = new PriorityQueue<QItem>();
		q.add(new QItem(0, 0));
		long res = 0;

		while (!q.isEmpty()) {
			QItem cur = q.poll();
			int u = cur.u;
			if (vis[u])
				continue;
			vis[u] = true;
			res += cur.prio;
			for (Edge e : edges[u]) {
				int v = e.t;
				if (!vis[v] && prio[v] > e.cost) {
					prio[v] = e.cost;
					pred[v] = u;
					q.add(new QItem(prio[v], v));
				}
			}
		}
		return res;
	}

	static class Edge {
		int t, cost;

		public Edge(int t, int cost) {
			this.t = t;
			this.cost = cost;
		}
	}

	static class QItem implements Comparable<QItem> {
		int prio;
		int u;

		public QItem(int prio, int u) {
			this.prio = prio;
			this.u = u;
		}

		public int compareTo(QItem q) {
			return prio < q.prio ? -1 : prio > q.prio ? 1 : 0;
		}
	}

	// Usage example
	public static void main(String[] args) {
		int[][] cost = { { 0, 1, 2 }, { 1, 0, 3 }, { 2, 3, 0 } };
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
		long res = mst(edges, new int[n]);
		System.out.println(res);
	}
}
