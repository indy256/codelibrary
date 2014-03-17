package obsolete;

import java.util.*;

// Search for maximum independent set
// See Christofides "Graph Theory" 2.3 for the description of the algorithm
public class BronKerboshOld {

	// input:
	// cur - current independent state (initially empty)
	// used - used vertices (initially empty)
	// notUsed - unused vertices (initially all graph vertices)
	// (used + notUsed) constitute vertices that can be used to extend cur
	// edges - graph adjacency list (see http://en.wikipedia.org/wiki/Adjacency_list)
	// output:
	// result - maximum independent set of vertices
	static void findIndependentSet(Set<Integer> cur, Set<Integer> used, Set<Integer> notUsed, List<Integer>[] edges,
			Set<Integer> result) {
		if (used.isEmpty() && notUsed.isEmpty() && cur.size() > result.size()) {
			result.clear();
			result.addAll(cur);
			return;
		}

		while (!notUsed.isEmpty()) {
			int v = notUsed.iterator().next();
			if (!used.isEmpty()) {
				int min = Integer.MAX_VALUE;
				for (int u : used) {
					Set<Integer> t = new HashSet<>(edges[u]);
					t.retainAll(notUsed);
					if (t.isEmpty())
						return;
					if (min > t.size()) {
						min = t.size();
						v = t.iterator().next();
					}
				}
			}
			Set<Integer> next_used = new HashSet<>(used);
			Set<Integer> next_notUsed = new HashSet<>(notUsed);
			next_used.removeAll(edges[v]);
			next_notUsed.removeAll(edges[v]);
			next_notUsed.remove(v);
			cur.add(v);
			findIndependentSet(cur, next_used, next_notUsed, edges, result);
			cur.remove(v);
			notUsed.remove(v);
			used.add(v);
		}
	}

	public static Set<Integer> maximumIndependentSet(List<Integer>[] graph) {
		int n = graph.length;
		Set<Integer> allVertices = new HashSet<>();
		for (int i = 0; i < n; i++) {
			allVertices.add(i);
		}
		Set<Integer> resultedMaxSet = new HashSet<>();
		findIndependentSet(new HashSet<Integer>(), new HashSet<Integer>(), allVertices, graph, resultedMaxSet);
		return resultedMaxSet;
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random(1);
		int V = 70;
		int E = V * (V - 1) / 2 / 5;
		System.out.println(V + " " + E);
//		List<Integer>[] graph = RandomGraph.getRandomUndirectedConnectedGraph(V, E, rnd);
//		long time = System.currentTimeMillis();
//		Set<Integer> mis = maximumIndependentSet(graph);
//		System.out.println(System.currentTimeMillis() - time);
//		System.out.println(mis.size() + " " + mis);
	}
}
