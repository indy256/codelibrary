import java.util.*;

public class Graph {
	public Map<Integer, Set<Integer>> edges = new TreeMap<>();

	public void addNode(int u) {
		if (!edges.containsKey(u)) {
			edges.put(u, new TreeSet<>());
		}
	}

	public void removeNode(int u) {
		if (!edges.containsKey(u)) {
			return;
		}
		for (int v : edges.get(u)) {
			edges.get(v).remove(u);
		}
		edges.remove(u);
	}

	public void addEdge(int u, int v) {
		addNode(u);
		addNode(v);
		edges.get(u).add(v);
		edges.get(v).add(u);
	}

	// Usage example
	public static void main(String[] args) {
		Graph g = new Graph();
		g.addEdge(0, 1);
		g.addEdge(1, 2);
		System.out.println(g.edges);
		g.removeNode(1);
		System.out.println(g.edges);
	}
}
