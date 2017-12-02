import java.util.*;
import java.util.stream.Stream;

import mooc.EdxIO;

public class DijkstraSinglePath {

	public static void shortestPaths(List<Edge>[] graph, int s, int f, int[] prio, int[] pred) {
		int n = prio.length;
		Arrays.fill(pred, -1);
		Arrays.fill(prio, Integer.MAX_VALUE);
		prio[s] = 0;
		boolean[] visited = new boolean[n];
		for (int i = 0; i < n; i++) {
			int u = -1;
			for (int j = 0; j < n; j++) {
				if (!visited[j] && (u == -1 || prio[u] > prio[j]))
					u = j;
			}
			if (prio[u] == Integer.MAX_VALUE)
				break;
			visited[u] = true;

			if (u == f) return;
			
			for (Edge e : graph[u]) {
				int v = e.t;
				int nprio = prio[u] + e.cost;
				if (prio[v] > nprio) {
					prio[v] = nprio;
					pred[v] = u;
				}
			}
		}
	}

	static class Edge {
		int t, cost;

		public Edge(int t, int cost) {
			this.t = t;
			this.cost = cost;
		}
	}

	// Usage example
	public static void main(String[] args) {
        try (EdxIO io = EdxIO.create()) {
        	int N = io.nextInt();
        	int S = io.nextInt() - 1;
        	int F = io.nextInt() - 1;
        	
        	List<Edge>[] graph = Stream.generate(ArrayList::new).limit(N).toArray(List[]::new);
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					int weight = io.nextInt();
					
					if (weight > -1 && i != j) {
						graph[i].add(new Edge(j, weight));
					}
				}
			}
			int[] dist = new int[N];
			int[] pred = new int[N];
			shortestPaths(graph, S, F, dist, pred);
			
			io.println(dist[F]);
        }
	}
}
