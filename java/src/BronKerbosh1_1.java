import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.*;

// Search for maximum independent set
// See Christofides "Graph Theory" 2.3 for the description of the algorithm
public class BronKerbosh1_1 {

	// input:
	// cur - current independent state (initially empty)
	// used - used vertices (initially empty)
	// notUsed - unused vertices (initially all graph vertices)
	// (used + notUsed) constitute vertices that can be used to extend cur
	// edges - graph adjacency list (see http://en.wikipedia.org/wiki/Adjacency_list)
	// output:
	// result - maximum independent set of vertices
	static void findIndependentSet(TIntSet cur, TIntSet used, TIntSet notUsed, TIntList[] edges, TIntSet result) {
		if (used.isEmpty() && notUsed.isEmpty() && cur.size() > result.size()) {
			result.clear();
			result.addAll(cur);
			return;
		}

		while (!notUsed.isEmpty()) {
			int v = notUsed.iterator().next();
			if (!used.isEmpty()) {
				int min = Integer.MAX_VALUE;
				for (TIntIterator it = used.iterator(); it.hasNext();) {
					int u = it.next();
					TIntSet t = new TIntHashSet(edges[u]);
					t.retainAll(notUsed);
					if (t.isEmpty())
						return;
					if (min > t.size()) {
						min = t.size();
						v = t.iterator().next();
					}
				}
			}
			TIntSet next_used = new TIntHashSet(used);
			TIntSet next_notUsed = new TIntHashSet(notUsed);
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

	public static TIntSet maximumIndependentSet(TIntList[] graph) {
		int n = graph.length;
		TIntSet allVertices = new TIntHashSet();
		for (int i = 0; i < n; i++) {
			allVertices.add(i);
		}
		TIntSet resultedMaxSet = new TIntHashSet();
		findIndependentSet(new TIntHashSet(), new TIntHashSet(), allVertices, graph, resultedMaxSet);
		return resultedMaxSet;
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random(1);
		int V = 70;
		int E = V * (V - 1) / 2 / 5;
		System.out.println(V + " " + E);
		List<Integer>[] graph = RandomGraph.getRandomConnectedGraph(V, E, rnd);
		TIntList[] g = new TIntList[graph.length];
		for (int i = 0; i < g.length; i++) {
			g[i]=new TIntArrayList();
			for (int x : graph[i]) {
				g[i].add(x);
			}
		}
		long time = System.currentTimeMillis();
		TIntSet mis = maximumIndependentSet(g);
		System.out.println(System.currentTimeMillis() - time);
		System.out.println(mis.size() + " " + mis);
	}
}
