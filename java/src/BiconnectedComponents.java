import java.util.*;

public class BiconnectedComponents {

	static int time;
	static List<Integer>[] graph;
	static boolean[] visited;
	static int[] tin;
	static int[] lowlink;
	static List<Integer> cutPoints;
	static List<String> bridges;
	static Stack<Integer> st;
	static List<List<Integer>> components;

	static void dfs(int u, int p) {
		visited[u] = true;
		lowlink[u] = tin[u] = time++;
		st.add(u);
		int children = 0;
		boolean cutPoint = false;
		for (int v : graph[u]) {
			if (v == p)
				continue;
			if (visited[v]) {
				// lowlink[u] = Math.min(lowlink[u], lowlink[v]);
				lowlink[u] = Math.min(lowlink[u], tin[v]);
			} else {
				dfs(v, u);
				lowlink[u] = Math.min(lowlink[u], lowlink[v]);
				cutPoint |= lowlink[v] >= tin[u];
				//if (lowlink[v] == tin[v])
				if (lowlink[v] > tin[u])
					bridges.add("(" + u + "," + v + ")");
				++children;
			}
		}
		if (p == -1)
			cutPoint = children >= 2;
		if (cutPoint)
			cutPoints.add(u);
		if (lowlink[u] == tin[u]) {
			List<Integer> component = new ArrayList<>();
			while (true) {
				int x = st.pop();
				component.add(x);
				if (x == u)
					break;
			}
			components.add(component);
		}
	}

	// Usage example
	public static void main(String[] args) {
		time = 0;
		int n = 6;
		graph = new List[n];
		for (int i = 0; i < n; i++) {
			graph[i] = new ArrayList<>();
		}
		int[][] edges = {{0, 1}, {1, 2}, {0, 2}, {2, 3}, {3, 4}, {4, 5}, {3, 5}};
		for (int[] edge : edges) {
			graph[edge[0]].add(edge[1]);
			graph[edge[1]].add(edge[0]);
		}

		visited = new boolean[n];
		tin = new int[n];
		lowlink = new int[n];
		cutPoints = new ArrayList<>();
		bridges = new ArrayList<>();
		st = new Stack<>();
		components = new ArrayList<>();

		dfs(0, -1);

		System.out.println("cutPoints: " + cutPoints);
		System.out.println("bridges:" + bridges);
		System.out.println("biconnected components:" + components);
	}
}
