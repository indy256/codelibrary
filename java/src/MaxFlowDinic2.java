import java.util.*;

public class MaxFlowDinic2 {

	static class Edge {
		int u;
		int cap;

		public Edge(int u, int cap) {
			this.u = u;
			this.cap = cap;
		}
	}

	List<Edge>[] graph;

	static final int INF = 2000000000;
	int maxnodes, maxedges, src, dest, edges;
	int[] last, work, Q;
	int[] head, prev, flow, cap, dist;

	public void init(int maxnodes) {
		graph = new List[maxnodes];
		for (int i = 0; i < maxnodes; i++) {
			graph[i] = new ArrayList<Edge>();
		}
		this.maxnodes = maxnodes;
		this.maxedges = maxedges;
		last = new int[maxnodes];
		work = new int[maxnodes];
		Q = new int[maxnodes];
		head = new int[maxedges];
		prev = new int[maxedges];
		flow = new int[maxedges];
		cap = new int[maxedges];
		dist = new int[maxnodes];
		Arrays.fill(last, -1);
		edges = 0;
	}

	public void addEdge(int u, int v, int cap1, int cap2) {
		graph[u].add(new Edge(v, cap1));
		graph[v].add(new Edge(u, cap2));
	}

	boolean dinic_bfs() {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int sizeQ = 0;
		Q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (int e = last[u]; e >= 0; e = prev[e]) {
				int v = head[e];
				if (dist[v] < 0 && flow[e] < cap[e]) {
					dist[v] = dist[u] + 1;
					Q[sizeQ++] = v;
				}
			}
		}
		return dist[dest] >= 0;
	}

	int dinic_dfs(int u, int f) {
		if (u == dest)
			return f;
		for (int i = work[u]; i < graph[u].size(); work[u] = ++i) {
			Edge e = graph[u].get(i);
			int v = e.u;
			if (dist[v] == dist[u] + 1 && flow[v] < cap[v]) {
				int df = dinic_dfs(v, Math.min(f, cap[v] - flow[v]));
				if (df > 0) {
					flow[v] += df;
					flow[v ^ 1] -= df;
					return df;
				}
			}
		}
		return 0;
	}

	public int maxFlow(int src, int dest) {
		this.src = src;
		this.dest = dest;
		int flow = 0;
		while (dinic_bfs()) {
			System.arraycopy(last, 0, work, 0, maxnodes);
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
		MaxFlowDinic2 dinic = new MaxFlowDinic2();
		dinic.init(n, n * n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					dinic.addEdge(i, j, capacity[i][j], 0);
		System.out.println(4 == dinic.maxFlow(0, 2));
	}
}
