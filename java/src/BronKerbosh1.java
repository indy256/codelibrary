import java.util.*;

// Search for maximum independent set
// See Christofides "Graph Theory" 2.3 for the description of the algorithm
public class BronKerbosh1 {

	// input:
	//   cur - current independent state (initially empty)
	//   used - used vertices (initially empty)
	//   notUsed - unused vertices (initially all graph vertices)
	//   (used + notUsed) constitute vertices that can be used to extend cur
	//   edges - graph adjacency list (see http://en.wikipedia.org/wiki/Adjacency_list)
	// output:
	//   result - maximum independent set of vertices
	public static void findIndependentSet(Set<Integer> cur, Set<Integer> used, Set<Integer> notUsed,
			List<Integer>[] edges, Set<Integer> result) {
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
					Set<Integer> t = new HashSet<Integer>(edges[u]);
					t.retainAll(notUsed);
					if (t.isEmpty())
						return;
					if (min > t.size()) {
						min = t.size();
						v = t.iterator().next();
					}
				}
			}
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

	// Usage example
	public static void main(String[] args) {
		int n = 4;
		List<Integer>[] edges = new List[n];
		Set<Integer> allVertices = new HashSet<Integer>();
		for (int i = 0; i < n; i++) {
			edges[i] = new ArrayList<Integer>();
			allVertices.add(i);
		}

		// create a simple cycle
		edges[0].add(1);
		edges[1].add(2);
		edges[2].add(3);
		edges[3].add(0);

		Set<Integer> resultedMaxSet = new HashSet<Integer>();
		findIndependentSet(new HashSet<Integer>(), new HashSet<Integer>(), allVertices, edges, resultedMaxSet);

		Set<Integer> expectedResult = new HashSet<Integer>();
		Collections.addAll(expectedResult, 0, 2);
		System.out.println(resultedMaxSet.equals(expectedResult));
	}
}
