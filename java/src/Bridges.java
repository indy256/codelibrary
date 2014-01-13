import java.util.*;

public class Bridges {

	static int time;
	static List<Integer>[] graph;
	static boolean[] used;
	static int[] tin;
	static int[] lowlink;
	static List<String> bridges;

	static void dfs(int u, int p) {
		used[u] = true;
		lowlink[u] = tin[u] = time++;
		for (int v : graph[u]) {
			if (v == p) {
				continue;
			}
			if (used[v]) {
				// lowlink[u] = Math.min(lowlink[u], lowlink[v]);
				lowlink[u] = Math.min(lowlink[u], tin[v]);
			} else {
				dfs(v, u);
				lowlink[u] = Math.min(lowlink[u], lowlink[v]);
				if (lowlink[v] > tin[u]) {
					bridges.add("(" + u + "," + v + ")");
				}
			}
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

		used = new boolean[n];
		tin = new int[n];
		lowlink = new int[n];
		bridges = new ArrayList<>();

		dfs(0, -1);

		System.out.println(bridges);
	}
}
