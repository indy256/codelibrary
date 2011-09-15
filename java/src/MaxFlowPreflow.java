public class MaxFlowPreflow {

	int[][] cap;

	public void init(int nodes) {
		cap = new int[nodes][nodes];
	}

	public void addEdge(int s, int t, int capacity) {
		cap[s][t] = capacity;
	}

	public int maxFlow(int s, int t) {
		int n = cap.length;

		int[] h = new int[n];
		h[s] = n - 1;

		int[] maxh = new int[n];

		int[][] f = new int[n][n];
		int[] e = new int[n];

		for (int i = 0; i < n; ++i) {
			f[s][i] = cap[s][i];
			f[i][s] = -f[s][i];
			e[i] = cap[s][i];
		}

		for (int sz = 0;;) {
			if (sz == 0) {
				for (int i = 0; i < n; ++i)
					if (i != s && i != t && e[i] > 0) {
						if (sz != 0 && h[i] > h[maxh[0]])
							sz = 0;
						maxh[sz++] = i;
					}
			}
			if (sz == 0)
				break;
			while (sz != 0) {
				int i = maxh[sz - 1];
				boolean pushed = false;
				for (int j = 0; j < n && e[i] != 0; ++j) {
					if (h[i] == h[j] + 1 && cap[i][j] - f[i][j] > 0) {
						int df = Math.min(cap[i][j] - f[i][j], e[i]);
						f[i][j] += df;
						f[j][i] -= df;
						e[i] -= df;
						e[j] += df;
						if (e[i] == 0)
							--sz;
						pushed = true;
					}
				}
				if (!pushed) {
					h[i] = Integer.MAX_VALUE;
					for (int j = 0; j < n; ++j)
						if (h[i] > h[j] + 1 && cap[i][j] - f[i][j] > 0)
							h[i] = h[j] + 1;
					if (h[i] > h[maxh[0]]) {
						sz = 0;
						break;
					}
				}
			}
		}

		int flow = 0;
		for (int i = 0; i < n; i++)
			flow += f[s][i];

		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		MaxFlowPreflow flow = new MaxFlowPreflow();
		flow.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					flow.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == flow.maxFlow(0, 2));
	}
}
