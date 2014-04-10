import java.util.*;

public class SCCKosaraju {

	public static List<List<Integer>> scc(List<Integer>[] graph) {
		int n = graph.length;
		boolean[] used = new boolean[n];
		List<Integer> order = new ArrayList<>();
		for (int i = 0; i < n; i++)
			if (!used[i])
				dfs(graph, used, order, i);

		List<Integer>[] reverseGraph = new List[n];
		for (int i = 0; i < n; i++)
			reverseGraph[i] = new ArrayList<>();
		for (int i = 0; i < n; i++)
			for (int j : graph[i])
				reverseGraph[j].add(i);

		List<List<Integer>> components = new ArrayList<>();
		Arrays.fill(used, false);
		Collections.reverse(order);

		for (int u : order)
			if (!used[u]) {
				List<Integer> component = new ArrayList<>();
				dfs(reverseGraph, used, component, u);
				components.add(component);
			}

		return components;
	}

	static void dfs(List<Integer>[] graph, boolean[] used, List<Integer> res, int u) {
		used[u] = true;
		for (int v : graph[u])
			if (!used[v])
				dfs(graph, used, res, v);
		res.add(u);
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

		List<List<Integer>> components = scc(g);
		System.out.println(components);
	}
}
