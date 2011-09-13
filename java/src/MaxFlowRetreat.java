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
	int maxnodes;
	int[] prev;
	int[] lim;
	int[] dist;
	int[] Q;
	int[] qtd;
	int[] work;

	public void init(int maxnodes) {
		this.maxnodes = maxnodes;
		graph = new List[maxnodes];
		for (int i = 0; i < maxnodes; i++)
			graph[i] = new ArrayList<Edge>();
		prev = new int[maxnodes];
		dist = new int[maxnodes];
		Q = new int[maxnodes];
		qtd = new int[maxnodes];
		work = new int[maxnodes];
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

	Edge advance(int no) {
		for (; work[no] < graph[no].size(); ++work[no]) {
			Edge e = graph[no].get(work[no]);
			if (dist[no] == dist[e.t] + 1 && e.cap - e.f > 0)
				return e;
		}
		return null;
	}

	int retreat(int no) {
		return 0;
	}

	int augment(int s, int t) {
		int f = lim[t];
		
		return 0;
	}

	public int maxFlow(int s, int t) {
		revBfs(s, t);
		Arrays.fill(work, 0);
		int flow = 0;
		lim[s] = Integer.MAX_VALUE;
		prev[s] = -1;
		int u = s;

		while (dist[s] < maxnodes) {
			Edge e = advance(u);
			if (e == null) {
				u = retreat(u);
			} else {
				flow += augment(s, t);
				u = s;
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
