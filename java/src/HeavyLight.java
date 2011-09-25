import java.util.*;

public class HeavyLight {

	int[] time_in;
	int[] time_out;
	int[] parent;
	int[] subTreeSize;
	int[] walk;
	int time;
	int cnt;

	static void addEdge(List<Long>[] g, int a, int b, int cost) {
		g[a].add(((long) b << 32) + cost);
		g[b].add(((long) a << 32) + cost);
	}

	void dfs(List<Long>[] t, int u, int p, boolean[] vis) {
		time_in[u] = time++;
		vis[u] = true;
		subTreeSize[u] = 1;
		parent[u] = p;
		for (long e : t[u]) {
			int v = (int) (e >>> 32);
			if (!vis[v]) {
				dfs(t, v, u, vis);
				subTreeSize[u] += subTreeSize[v];
			}
		}
		walk[cnt++] = u;
		time_out[u] = time++;
	}

	public void decompose(List<Long>[] t) {
		int n = t.length;
		time_in = new int[n];
		time_out = new int[n];
		parent = new int[n];
		subTreeSize = new int[n];
		walk = new int[n];
		time = 0;
		cnt = 0;

		dfs(t, 0, -1, new boolean[n]);
	}

	// Usage example
	public static void main(String[] args) {
		List<Long>[] t = new List[8];
		for (int i = 0; i < t.length; i++)
			t[i] = new ArrayList<Long>();

		addEdge(t, 0, 1, 0);
		addEdge(t, 1, 3, 0);
		addEdge(t, 1, 4, 0);
		addEdge(t, 0, 2, 0);
		addEdge(t, 2, 5, 1);
		addEdge(t, 5, 6, 0);
		addEdge(t, 5, 7, 0);

		new HeavyLight().decompose(t);
	}

}
