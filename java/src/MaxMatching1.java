import java.util.*;

public class MaxMatching1 {

	static boolean findPath(List<Integer>[] g, int u1, int[] matching, boolean[] vis) {
		vis[u1] = true;
		for (int v : g[u1]) {
			int u2 = matching[v];
			if (u2 == -1 || !vis[u2] && findPath(g, u2, matching, vis)) {
				matching[v] = u1;
				return true;
			}
		}
		return false;
	}

	public static int maxMatching(List<Integer>[] g) {
		int n = g.length;
		int[] matching = new int[n];
		Arrays.fill(matching, -1);
		int matches = 0;
		for (int u = 0; u < n; u++) {
			if (findPath(g, u, matching, new boolean[n]))
				++matches;
		}
		return matches;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 2;
		List<Integer>[] g = new List[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<Integer>();
		}
		g[0].add(0);
		g[0].add(1);
		g[1].add(1);
		System.out.println(2 == maxMatching(g));
	}
}
