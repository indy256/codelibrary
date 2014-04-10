import java.util.*;

public class SCCTransitiveClosure {

	public static List<List<Integer>> scc(List<Integer>[] graph) {
		boolean[][] d = transitiveClosure(graph);
		List<List<Integer>> res = new ArrayList<>();

		int n = graph.length;
		boolean[] used = new boolean[n];

		for (int i = 0; i < n; i++) {
			if (!used[i]) {
				List<Integer> component = new ArrayList<>();
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

	static boolean[][] transitiveClosure(List<Integer>[] graph) {
		int n = graph.length;
		boolean[][] res = new boolean[n][n];
		for (int i = 0; i < n; i++)
			for (int j : graph[i])
				res[i][j] = true;

		for (int k = 0; k < n; k++)
			for (int i = 0; i < n; i++)
				for (int j = 0; j < n; j++)
					res[i][j] |= res[i][k] && res[k][j];

		return res;
	}
}
