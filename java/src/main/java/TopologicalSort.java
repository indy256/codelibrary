import java.util.*;

public class TopologicalSort {

	static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
		used[u] = true;
		for (int v : graph[u])
			if (!used[v])
				dfs(graph, used, res, v);
		res.add(u);
	}

	public static List<Integer> topologicalSort(List<Integer>[] graph) {
		int n = graph.length;
		boolean[] used = new boolean[n];
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < n; i++)
			if (!used[i])
				dfs(graph, used, res, i);
		Collections.reverse(res);
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 3;
		List<Integer>[] g = new List[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}
		g[2].add(0);
		g[2].add(1);
		g[0].add(1);

		List<Integer> res = topologicalSort(g);
		System.out.println(res);
	}
}
