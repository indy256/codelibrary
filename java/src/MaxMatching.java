import java.util.*;

public class MaxMatching {

	static boolean findPath(boolean[][] graph, int u1, int[] matching, boolean[] vis) {
		vis[u1] = true;
		for (int v = 0; v < matching.length; ++v) {
			int u2 = matching[v];
			if (graph[u1][v] && (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis))) {
				matching[v] = u1;
				return true;
			}
		}
		return false;
	}

	public static int maxMatching(boolean[][] graph) {
		int n1 = graph.length;
		int n2 = n1 == 0 ? 0 : graph[0].length;
		int[] matching = new int[n2];
		Arrays.fill(matching, -1);
		int matches = 0;
		for (int u = 0; u < n1; u++)
			if (findPath(graph, u, matching, new boolean[n1]))
				++matches;
		return matches;
	}

	// Usage example
	public static void main(String[] args) {
		int res = maxMatching(new boolean[][] { { true, true }, { true, false } });
		System.out.println(2 == res);
	}
}
