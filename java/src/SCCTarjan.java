import java.util.*;

// optimized version of http://en.wikipedia.org/wiki/Tarjan's_strongly_connected_components_algorithm
public class SCCTarjan {

	int time;
	List<Integer>[] graph;
	int[] lowlink;
	boolean[] used;
	List<Integer> stack;
	List<List<Integer>> components;

	public List<List<Integer>> scc(List<Integer>[] graph) {
		int n = graph.length;
		this.graph = graph;
		lowlink = new int[n];
		used = new boolean[n];
		stack = new ArrayList<>();
		components = new ArrayList<>();

		for (int u = 0; u < n; u++)
			if (!used[u])
				dfs(u);

		return components;
	}

	void dfs(int u) {
		lowlink[u] = time++;
		used[u] = true;
		stack.add(u);
		boolean isComponentRoot = true;

		for (int v : graph[u]) {
			if (!used[v])
				dfs(v);
			if (lowlink[u] > lowlink[v]) {
				lowlink[u] = lowlink[v];
				isComponentRoot = false;
			}
		}

		if (isComponentRoot) {
			List<Integer> component = new ArrayList<>();
			while (true) {
				int k = stack.remove(stack.size() - 1);
				component.add(k);
				lowlink[k] = Integer.MAX_VALUE;
				if (k == u)
					break;
			}
			components.add(component);
		}
	}

	// Usage example
	public static void main(String[] args) {
		List<Integer>[] g = new List[3];
		for (int i = 0; i < g.length; i++) {
			g[i] = new ArrayList<>();
		}
		g[0].add(1);
		g[0].add(2);
		g[2].add(1);

		List<List<Integer>> components = new SCCTarjan().scc(g);
		System.out.println(components);
	}
}
