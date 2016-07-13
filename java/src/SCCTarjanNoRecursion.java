import java.util.*;
import java.util.stream.Stream;

public class SCCTarjanNoRecursion {

	public static List<List<Integer>> scc(List<Integer>[] graph) {
		int n = graph.length;
		int[] stack = new int[n];
		int st = 0;
		int[] stack_cur = new int[n];
		int[] stack_pos = new int[n];
		int[] stack_prev = new int[n];
		int[] index = new int[n];
		Arrays.fill(index, -1);
		int[] lowlink = new int[n];
		int time = 0;
		List<List<Integer>> components = new ArrayList<>();

		for (int u = 0; u < n; u++) {
			if (index[u] == -1) {
				int top = 0;
				stack_cur[top] = u;
				stack_pos[top] = 0;
				stack_prev[top] = -1;
				while (top >= 0) {
					int cur = stack_cur[top];
					int pos = stack_pos[top];
					if (index[cur] == -1) {
						index[cur] = time;
						lowlink[cur] = time;
						++time;
						stack[st++] = cur;
					}
					if (pos < graph[cur].size()) {
						int v = graph[cur].get(pos);
						++stack_pos[top];
						if (index[v] == -1) {
							++top;
							stack_cur[top] = v;
							stack_pos[top] = 0;
							stack_prev[top] = cur;
						} else {
							lowlink[cur] = Math.min(lowlink[cur], lowlink[v]);
						}
					} else {
						int prev = stack_prev[top];
						if (prev != -1) {
							lowlink[prev] = Math.min(lowlink[prev], lowlink[cur]);
						}
						if (lowlink[cur] == index[cur]) {
							List<Integer> component = new ArrayList<>();
							while (true) {
								int v = stack[--st];
								lowlink[v] = Integer.MAX_VALUE;
								component.add(v);
								if (v == cur)
									break;
							}
							components.add(component);
						}
						--top;
					}
				}
			}
		}

		return components;
	}

	// Usage example
	public static void main(String[] args) {
		List<Integer>[] g = Stream.generate(ArrayList::new).limit(3).toArray(List[]::new);
		g[2].add(0);
		g[2].add(1);
		g[0].add(1);
		g[1].add(0);

		List<List<Integer>> res = scc(g);
		System.out.println(res);
	}
}
