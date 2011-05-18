import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import java.util.*;

// Search for maximum independent set
// See Christofides "Graph Theory" 2.3 for the description of the algorithm
public class BronKerbosh1_2 {

	// input:
	// cur - current independent state (initially empty)
	// used - used vertices (initially empty)
	// notUsed - unused vertices (initially all graph vertices)
	// (used + notUsed) constitute vertices that can be used to extend cur
	// edges - graph adjacency list (see http://en.wikipedia.org/wiki/Adjacency_list)
	// output:
	// result - maximum independent set of vertices
	static void findIndependentSet(IntSet cur, IntSet used, IntSet notUsed, IntList[] edges, IntSet result) {
		if (used.isEmpty() && notUsed.isEmpty() && cur.size() > result.size()) {
			result.clear();
			result.addAll(cur);
			return;
		}

		while (!notUsed.isEmpty()) {
			int v = notUsed.iterator().next();
			if (!used.isEmpty()) {
				int min = Integer.MAX_VALUE;
				for (IntIterator it = used.iterator(); it.hasNext();) {
					int u = it.next();
					IntSet t = new IntOpenHashSet(edges[u]);
					t.retainAll(notUsed);
					if (t.isEmpty())
						return;
					if (min > t.size()) {
						min = t.size();
						v = t.iterator().next();
					}
				}
			}
			IntSet next_used = new IntOpenHashSet(used);
			IntSet next_notUsed = new IntOpenHashSet(notUsed);
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

	public static IntSet maximumIndependentSet(IntList[] graph) {
		int n = graph.length;
		IntSet allVertices = new IntOpenHashSet();
		for (int i = 0; i < n; i++) {
			allVertices.add(i);
		}
		IntSet resultedMaxSet = new IntOpenHashSet();
		findIndependentSet(new IntOpenHashSet(), new IntOpenHashSet(), allVertices, graph, resultedMaxSet);
		return resultedMaxSet;
	}

	// Usage example
	public static void main(String[] args) {
		Random rnd = new Random(1);
		int V = 70;
		int E = V * (V - 1) / 2 / 5;
		System.out.println(V + " " + E);
		List<Integer>[] graph = RandomGraph.getRandomConnectedGraph(V, E, rnd);
		IntList[] g = new IntList[graph.length];
		for (int i = 0; i < g.length; i++) {
			g[i]=new IntArrayList();
			for (int x : graph[i]) {
				g[i].add(x);
			}
		}
		long time = System.currentTimeMillis();
		IntSet mis = maximumIndependentSet(g);
		System.out.println(System.currentTimeMillis() - time);
		System.out.println(mis.size() + " " + mis);
	}
}
