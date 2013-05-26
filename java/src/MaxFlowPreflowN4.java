public class MaxFlowPreflowN4 {

	int[][] cap;

	public void init(int nodes) {
		cap = new int[nodes][nodes];
	}

	public void addEdge(int s, int t, int capacity) {
		cap[s][t] = capacity;
	}

	void push(int u, int v, int[][] f, int[] e, int[][] c) {
		int d = Math.min(e[u], c[u][v] - f[u][v]);
		f[u][v] += d;
		f[v][u] = -f[u][v];
		e[u] -= d;
		e[v] += d;
	}

	void lift(int u, int[] h, int[][] f, int[][] c) {
		int d = Integer.MAX_VALUE;
		for (int i = 0; i < f.length; i++)
			if (c[u][i] - f[u][i] > 0)
				d = Math.min(d, h[i]);
		if (d == Integer.MAX_VALUE)
			return;
		h[u] = d + 1;
	}

	public int maxFlow(int src, int dest) {
		int n = cap.length;

		int[][] f = new int[n][n];

		for (int i = 1; i < n; i++) {
			f[0][i] = cap[0][i];
			f[i][0] = -cap[0][i];
		}

		int[] h = new int[n];
		h[0] = n;

		int[] e = new int[n];
		for (int i = 1; i < n; i++)
			e[i] = f[0][i];

		for (;;) {
			int i;
			for (i = 1; i < n - 1; i++)
				if (e[i] > 0)
					break;
			if (i == n - 1)
				break;

			int j;
			for (j = 0; j < n; j++)
				if (cap[i][j] - f[i][j] > 0 && h[i] == h[j] + 1)
					break;
			if (j < n)
				push(i, j, f, e, cap);
			else
				lift(i, h, f, cap);
		}

		int flow = 0;
		for (int i = 0; i < n; i++)
			if (cap[0][i] != 0)
				flow += f[0][i];

		return flow;
	}

	// Usage example
	public static void main(String[] args) {
		int[][] capacity = { { 0, 3, 2 }, { 0, 0, 2 }, { 0, 0, 0 } };
		int n = capacity.length;
		MaxFlowPreflowN4 flow = new MaxFlowPreflowN4();
		flow.init(n);
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				if (capacity[i][j] != 0)
					flow.addEdge(i, j, capacity[i][j]);
		System.out.println(4 == flow.maxFlow(0, 2));
	}
}
