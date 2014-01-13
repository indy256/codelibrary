import java.util.*;

public class CutPoints {

	static int time;
	static List<Integer>[] graph;
	static boolean[] used;
	static int[] tin;
	static int[] lowlink;
	static Set<Integer> cutPoints;

	static void dfs(int u, int p) {
		used[u] = true;
		lowlink[u] = tin[u] = time++;
		int children = 0;
		for (int v : graph[u]) {
			if (v == p) {
				continue;
			}
			if (used[v]) {
				lowlink[u] = Math.min(lowlink[u], tin[v]);
			} else {
				dfs(v, u);
				lowlink[u] = Math.min(lowlink[u], lowlink[v]);
				if (lowlink[v] >= tin[u] && p != -1) {
					cutPoints.add(u);
				}
				++children;
			}
		}
		if (p == -1 && children > 1) {
			cutPoints.add(u);
		}
	}

	// Usage example
	public static void main(String[] args) {
		time = 0;
//		int n = 4;
		int n = 5;
		graph = new List[n];
		for (int i = 0; i < n; i++) {
			graph[i] = new ArrayList<>();
		}
		int[][] edges = {{0, 1}, {1, 2}, {0, 2}, {2, 3}, {3, 4}, {2, 4}};
//		int[][] edges = {{0, 1}, {1, 2}, {1, 3}};
		for (int[] edge : edges) {
			graph[edge[0]].add(edge[1]);
			graph[edge[1]].add(edge[0]);
		}

		used = new boolean[n];
		tin = new int[n];
		lowlink = new int[n];
		cutPoints = new TreeSet<>();

		dfs(0, -1);

		System.out.println(cutPoints);
	}
}
