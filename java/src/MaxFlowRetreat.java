import java.util.*;

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

	List<Edge>[] graph;
	int src, dest;
	int[] ptr, Q, dist, qtd, lim;
	Edge[] pred;

	public void init(int nodes) {
		graph = new List[nodes];
		for (int i = 0; i < nodes; i++)
			graph[i] = new ArrayList<Edge>();
		pred = new Edge[nodes];
		dist = new int[nodes];
		Q = new int[nodes];
		qtd = new int[nodes];
		ptr = new int[nodes];
		lim = new int[nodes];
	}

	public void addEdge(int s, int t, int cap) {
		graph[s].add(new Edge(s, t, graph[t].size(), cap));
		graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
	}

	void revBfs(int s, int t) {
		Arrays.fill(dist, -1);
		dist[t] = 0;
		int sizeQ = 0;
		Q[sizeQ++] = t;
		Arrays.fill(qtd, 0);

		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			++qtd[dist[u]];

			for (Edge e : graph[u]) {
				int v = e.t;
				if (e.cap == 0 && dist[v] == -1) {
					dist[v] = dist[u] + 1;
					Q[sizeQ++] = v;
				}
			}
		}
	}

	Edge advance(int u) {
		for (; ptr[u] < graph[u].size(); ++ptr[u]) {
			Edge e = graph[u].get(ptr[u]);
			if (dist[u] == dist[e.t] + 1 && e.cap - e.f > 0)
				return e;
		}
		ptr[u] = 0;
		return null;
	}

	int retreat(int u) {
		if (--qtd[dist[u]] == 0)
			return -1;

		int minu = -1;

		for (Edge e : graph[u]) {
			if (e.cap - e.f > 0 && (minu == -1 || dist[minu] > dist[e.t]))
				minu = e.t;
		}

		if (minu != -1)
			dist[u] = dist[minu];
		++qtd[++dist[u]];

		return pred[u] == null ? u : pred[u].s;
	}

	int augment(int s, int t) {
//		int df = Integer.MAX_VALUE;
//		for (int u = t; u != s; u = pred[u].s)
//			df = Math.min(df, pred[u].cap - pred[u].f);
		int df=lim[t];
		for (int u = t; u != s; u = pred[u].s) {
			pred[u].f += df;
			graph[pred[u].t].get(pred[u].rev).f -= df;
		}
		return df;
	}

	public int maxFlow(int s, int t) {
		revBfs(s, t);
		Arrays.fill(ptr, 0);
		lim[s]=Integer.MAX_VALUE;
		int flow = 0;
		pred[s] = null;

		for (int u = s; dist[s] < graph.length && u != -1;) {
			Edge e = advance(u);
			if (e == null) {
				u = retreat(u);
			} else {
				u = e.t;
				pred[u] = e;
				lim[u] = Math.min(lim[e.s], e.cap - e.f);
				if (u == t) {
					flow += augment(s, t);
					u = s;
				}
			}
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		MaxFlowRetreat flowSolver = new MaxFlowRetreat();
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		flowSolver.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					flowSolver.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == flowSolver.maxFlow(0, 2));
	}
}
