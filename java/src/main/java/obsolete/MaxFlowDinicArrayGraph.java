package obsolete;
import java.util.*;

public class MaxFlowDinicArrayGraph {

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
	Edge[][] graph2;

	static final int INF = 2000000000;
	int src, dest;
	int[] work, Q, dist;

	public void init(int maxnodes) {
		graph = new List[maxnodes];
		for (int i = 0; i < maxnodes; i++) {
			graph[i] = new ArrayList<>();
		}
		work = new int[maxnodes];
		Q = new int[maxnodes];
		dist = new int[maxnodes];
	}

	public void addEdge(int s, int t, int cap) {
		graph[s].add(new Edge(s, t, graph[t].size(), cap));
		graph[t].add(new Edge(t, s, graph[s].size() - 1, 0));
	}

	boolean dinic_bfs() {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int sizeQ = 0;
		Q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (Edge e : graph2[u]) {
				if (dist[e.t] < 0 && e.f < e.cap) {
					dist[e.t] = dist[u] + 1;
					Q[sizeQ++] = e.t;
				}
			}
		}
		return dist[dest] >= 0;
	}

	int dinic_dfs(int u, int f) {
		if (u == dest)
			return f;
		for (int i = work[u]; i < graph2[u].length; work[u] = ++i) {
			Edge e = graph2[u][i];
			if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
				int df = dinic_dfs(e.t, Math.min(f, e.cap - e.f));
				if (df > 0) {
					e.f += df;
					graph2[e.t][e.rev].f -= df;
					return df;
				}
			}
		}
		return 0;
	}

	public int maxFlow(int src, int dest) {
		long time=System.currentTimeMillis();
		graph2 = convert(graph);
		System.err.println(System.currentTimeMillis()-time);
		this.src = src;
		this.dest = dest;
		int flow = 0;
		while (dinic_bfs()) {
			Arrays.fill(work, 0);
			while (true) {
				int df = dinic_dfs(src, INF);
				if (df == 0)
					break;
				flow += df;
			}
		}
		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		MaxFlowDinicArrayGraph dinic = new MaxFlowDinicArrayGraph();
		dinic.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					dinic.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == dinic.maxFlow(0, 2));
	}

	static Edge[][] convert(List<Edge>[] g) {
		Edge[][] graph = new Edge[g.length][];
		for (int i = 0; i < g.length; i++) {
			graph[i] = g[i].toArray(new Edge[0]);
		}
		return graph;
	}
}
