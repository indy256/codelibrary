import java.util.*;

// Search for maximum independent set
// See Christofides "Graph Theory" 2.3 for the description of the algorithm
public class BronKerbosh3 {

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

		if (notUsed.isEmpty()) {
			return;
		}

		Set<Integer> workingSet = null;
		for (int u : notUsed) {
			Set<Integer> t = new HashSet<Integer>(edges[u]);
			t.retainAll(notUsed);
			t.add(u);
			if (workingSet == null || workingSet.size() > t.size()) {
				workingSet = t;
			}
		}

		for (int u : used) {
			Set<Integer> t = new HashSet<Integer>(edges[u]);
			t.retainAll(notUsed);
			if (t.isEmpty())
				return;
			if (workingSet == null || workingSet.size() > t.size()) {
				workingSet = t;
			}
		}

		for (int v : workingSet) {
			Set<Integer> next_used = new HashSet<Integer>(used);
			Set<Integer> next_notUsed = new HashSet<Integer>(notUsed);
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
		Set<Integer> allVertices = new HashSet<Integer>();
		for (int i = 0; i < n; i++) {
			allVertices.add(i);
		}
		Set<Integer> resultedMaxSet = new HashSet<Integer>();
		findIndependentSet(new HashSet<Integer>(), new HashSet<Integer>(), allVertices, graph, resultedMaxSet);
		return resultedMaxSet;
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random(1);
		int V = 70;
		int E = V * (V - 1) / 2 / 5;
		System.out.println(V + " " + E);
		List<Integer>[] graph = RandomGraph.getRandomConnectedGraph(V, E, rnd);
		long time = System.currentTimeMillis();
		Set<Integer> mis = maximumIndependentSet(graph);
		System.out.println(System.currentTimeMillis() - time);
		System.out.println(mis.size() + " " + mis);
	}
}
