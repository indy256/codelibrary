package obsolete;

import java.util.*;

public class SCCTarjanOld {

	int[] lowlink;
	boolean[] vis;
	List<Integer> stack;
	int time;
	int components;
	List<Integer>[] graph;
	int[] res;

	public int[] scc(List<Integer>[] graph) {
		int n = graph.length;
		this.graph = graph;
		lowlink = new int[n];
		vis = new boolean[n];
		stack = new ArrayList<>();
		res = new int[n];
		time = 0;
		components = 0;

		for (int u = 0; u < n; u++)
			if (!vis[u])
				dfs(u);

		return res;
	}

	void dfs(int u) {
		vis[u] = true;
		lowlink[u] = time++;
		stack.add(u);

		for (int v : graph[u])
			if (!vis[v])
				dfs(v);

		boolean isRoot = true;
		for (int v : graph[u])
			if (lowlink[u] > lowlink[v]) {
				lowlink[u] = lowlink[v];
				isRoot = false;
			}

		if (isRoot) {
			while (true) {
				int v = stack.remove(stack.size() - 1);
				res[v] = components;
				lowlink[v] = Integer.MAX_VALUE;
				if (v == u)
					break;
			}
			++components;
		}
	}

	// Usage example
	public static void main(String[] args) {
		List<Integer>[] g = new List[3];
		for (int i = 0; i < g.length; i++)
			g[i] = new ArrayList<>();
		g[2].add(0);
		g[2].add(1);
		g[0].add(1);
		g[1].add(0);

		int[] res = new SCCTarjanOld().scc(g);
		System.out.println(Arrays.toString(res));
	}
}