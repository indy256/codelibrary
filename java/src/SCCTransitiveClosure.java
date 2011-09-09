import java.util.*;

public class SCCTransitiveClosure {

	static boolean[][] transitiveClosure(List<Integer>[] graph) {
		int n = graph.length;
		boolean[][] res = new boolean[n][n];
		for (int i = 0; i < n; i++)
			for (int j : graph[i])
				res[i][j] = true;

		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++) {
					res[i][j] |= res[i][k] && res[k][j];
				}

		return res;
	}

	public static List<List<Integer>> scc(List<Integer>[] graph) {
		boolean[][] d = transitiveClosure(graph);
		List<List<Integer>> res = new ArrayList<List<Integer>>();

		int n = graph.length;
		boolean[] used = new boolean[n];

		for (int i = 0; i < n; i++) {
			if (!used[i]) {
				List<Integer> component = new ArrayList<Integer>();
				for (int j = 0; j < n; j++)
					if (i == j || d[i][j] && d[j][i]) {
						component.add(j);
						used[j] = true;
					}
				res.add(component);
			}
		}

		return res;
	}

	// Usage example
	public static void main(String[] args) {
		List<Integer>[] g = new List[3];
		for (int i = 0; i < g.length; i++)
			g[i] = new ArrayList<Integer>();

		g[2].add(0);
		g[2].add(1);
		g[0].add(1);
		g[1].add(0);

		List<List<Integer>> res = scc(g);
		System.out.println(res);
	}
}
