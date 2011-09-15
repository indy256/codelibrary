package obsolete;
import java.util.*;

public class MaxFlowDinicMatrix {

	int[][] cap, f;
	int src, dest;
	int[] ptr, q, dist;

	public void init(int nodes) {
		cap = new int[nodes][nodes];
		f = new int[nodes][nodes];
		ptr = new int[nodes];
		q = new int[nodes];
		dist = new int[nodes];
	}

	public void addEdge(int s, int t, int capacity) {
		cap[s][t] = capacity;
	}

	boolean dinic_bfs() {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int sizeQ = 0;
		q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = q[i];
			for (int v = 0; v < cap.length; v++) {
				if (dist[v] < 0 && f[u][v] < cap[u][v]) {
					dist[v] = dist[u] + 1;
					q[sizeQ++] = v;
				}
			}
		}
		return dist[dest] >= 0;
	}

	int dinic_dfs(int u, int flow) {
		if (u == dest)
			return flow;
		for (; ptr[u] < cap.length; ++ptr[u]) {
			int v = ptr[u];
			if (dist[v] == dist[u] + 1 && f[u][v] < cap[u][v]) {
				int df = dinic_dfs(v, Math.min(flow, cap[u][v] - f[u][v]));
				if (df > 0) {
					f[u][v] += df;
					f[v][u] -= df;
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
			Arrays.fill(ptr, 0);
			while (true) {
				int df = dinic_dfs(src, Integer.MAX_VALUE);
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
		MaxFlowDinicMatrix flow = new MaxFlowDinicMatrix();
		flow.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					flow.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == flow.maxFlow(0, 2));
	}
}
