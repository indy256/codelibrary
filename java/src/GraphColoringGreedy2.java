import java.util.*;

public class GraphColoringGreedy2 {

	public static int[] color(List<Integer>[] graph) {
		int n = graph.length;
		int[] used = new int[n];
		int[] colors = new int[n];
		Arrays.fill(colors, -1);

		for (int i = 0; i < n; i++) {
			int best_cnt = -1;
			int bestu = -1;
			for (int u = 0; u < n; u++) {
				if (colors[u] == -1) {
					int cnt = Integer.bitCount(used[u]);
					if (best_cnt < cnt) {
						best_cnt = cnt;
						bestu = u;
					}
				}
			}
			int c = Integer.numberOfTrailingZeros(~used[bestu]);
			colors[bestu] = c;
			for (int v : graph[bestu]) {
				used[v] |= 1 << c;
			}
		}
		return colors;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 5;
		List<Integer>[] g = new List[n];
		for (int i = 0; i < n; i++) {
			g[i] = new ArrayList<>();
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				g[i].add((i + 1) % n);
				g[(i + 1) % n].add(i);
			}
		}
		System.out.println(Arrays.toString(color(g)));
	}
}
