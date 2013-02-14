import java.util.*;

public class EulerCycle {

	static void dfs(List<Integer>[] graph, int[] curEdge, List<Integer> res, int u) {
		while (curEdge[u] < graph[u].size()) {
			dfs(graph, curEdge, res, graph[u].get(curEdge[u]++));
		}
		res.add(u);
	}

	public static List<Integer> eulerCycle(List<Integer>[] graph) {
		int n = graph.length;
		int[] curEdge = new int[n];
		List<Integer> res = new ArrayList<>();
		dfs(graph, curEdge, res, 0);
		Collections.reverse(res);
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 5;
		List<Integer>[] g = new List[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}
		g[0].add(1);
		g[1].add(2);
		g[2].add(0);

		g[1].add(3);
		g[3].add(4);
		g[4].add(1);

		List<Integer> res = eulerCycle(g);
		System.out.println(res);
	}
}
