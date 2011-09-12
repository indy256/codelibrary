import java.util.*;

import MaxFlowEdmondKarp.Edge;

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
	int maxnodes;
	int[] prev;
	int[] dist;
	int[] Q;
	int[] qtd;

	public void init(int maxnodes) {
		this.maxnodes = maxnodes;
		graph = new List[maxnodes];
		for (int i = 0; i < maxnodes; i++)
			graph[i] = new ArrayList<Edge>();
		prev = new int[maxnodes];
		dist = new int[maxnodes];
		Q = new int[maxnodes];
		qtd = new int[maxnodes];
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
			for (int e = last[u]; e >= 0; e = prev[e]) {
				int v = head[e];
				if (dist[v] < 0 && flow[e] < cap[e]) {
					dist[v] = dist[u] + 1;
					Q[sizeQ++] = v;
				}
			}
		}
			
		for (int i = 0; i < sizeQ; i++) {
			int no = Q[i];
						
			++qtd[dist[no]];
			for (Edge e : graph[no]) {
				
			}
			for (i = 0; i < nadj[no]; i++) {
				ar = adj[no][i];
				viz = dest[ar];
				if (cap[ar] == 0 && nivel[viz] == NULO) {
					nivel[viz] = nivel[no] + 1;
					fila.push(viz);
				}
			}
		}

	}

	public int maxFlow(int s, int t) {
		int u = s;
		int flow = 0;

		revBfs();

		int[] q = new int[maxnodes];
		while (true) {
			int qh = 0, qt = 0;
			q[qt++] = s;
			Edge[] path = new Edge[maxnodes];
			while (qh < qt && path[t] == null) {
				int cur = q[qh++];
				for (Edge e : graph[cur]) {
					if (path[e.t] == null && e.cap > e.f) {
						path[e.t] = e;
						q[qt++] = e.t;
					}
				}
			}
			if (path[t] == null)
				break;
			int df = Integer.MAX_VALUE;
			for (int u = t; u != s; u = path[u].s)
				df = Math.min(df, path[u].cap - path[u].f);
			for (int u = t; u != s; u = path[u].s) {
				path[u].f += df;
				graph[path[u].t].get(path[u].rev).f -= df;
			}
			flow += df;
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
