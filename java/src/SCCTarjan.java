import java.util.*;

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
		stack = new ArrayList<Integer>();
		components = new ArrayList<List<Integer>>();
		time = 0;

		for (int u = 0; u < n; u++)
			if (!used[u])
				dfs(u);

		return components;
	}

	void dfs(int u) {
		lowlink[u] = time++;
		used[u] = true;
		stack.add(u);
		boolean isRoot = true;

		for (int v : graph[u]) {
			if (!used[v])
				dfs(v);
			if (lowlink[u] > lowlink[v]) {
				lowlink[u] = lowlink[v];
				isRoot = false;
			}
		}

		if (isRoot) {
			List<Integer> component = new ArrayList<Integer>();
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
		int n = 3;
		List<Integer>[] g = new List[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<Integer>();
		}
		g[2].add(0);
		g[2].add(1);
		g[0].add(1);
		g[1].add(0);

		List<List<Integer>> components = new SCCTarjan().scc(g);
		System.out.println(components);
	}
}
