import java.util.*;

/**
 * @author Andrey Naumenko
 */
public class ContractionHierarchies {
/*
	int calculatePriority(List<Integer>[] g, List<Integer>[] rg, int v) {
		// set of shortcuts that would be added if endNode v would be contracted next.
		Collection<Shortcut> tmpShortcuts = findShortcuts(g, rg, v);
		// from shortcuts we can compute the edgeDifference

		// # low influence: with it the shortcut creation is slightly faster
		//
		// |shortcuts(v)| − |{(u, v) | v uncontracted}| − |{(v, w) | v uncontracted}|
		// meanDegree is used instead of outDegree+inDegree as if one endNode is in both directions
		// only one bucket memory is used. Additionally one shortcut could also stand for two directions.
		int degree = 0;//GraphUtility.count(g.getEdges(v));
		int edgeDifference = tmpShortcuts.size() - degree;

		// # huge influence: the bigger the less shortcuts gets created and the faster is the preparation
		//
		// every endNode has an 'original edge' number associated. initially it is r=1
		// when a new shortcut is introduced then r of the associated edges is summed up:
		// r(u,w)=r(u,v)+r(v,w) now we can define
		// originalEdgesCount = σ(v) := sum_{ (u,w) ∈ shortcuts(v) } of r(u, w)
		int originalEdgesCount = 0;
		for (Shortcut sc : tmpShortcuts) {
			originalEdgesCount += sc.originalEdges;
		}

		// # lowest influence on preparation speed or shortcut creation count
		// (but according to paper should speed up queries)
		//
		// number of already contracted neighbors of v
		int contractedNeighbors = 0;
//		EdgeSkipIterator iter = g.getEdges(v);
//		while (iter.next()) {
//			if (iter.skippedEdge() >= 0)
//				contractedNeighbors++;
//		}

		// unterfranken example
		// 10, 50, 1 => 180s preparation, q 3.3ms
		//  2,  4, 1 => 200s preparation, q 3.0ms
		// according to the paper do a simple linear combination of the properties to get the priority
		return 10 * edgeDifference + 50 * originalEdgesCount + contractedNeighbors;
	}

	Collection<Shortcut> findShortcuts(int[][] g, int[][] rg, int[][] dist, int v) {
		final int[] incoming = rg[v];
		final int[] outgoing = rg[v];

		for (int i = 0; i < incoming.length; i++) {
			int u = incoming[i];
			int distUV = dist[u][i];


		}


		// we can use distance instead of weight, see prepareEdges where distance is overwritten by weight!
		List<NodeCH> goalNodes = new ArrayList<NodeCH>();
		Map<Long, Shortcut> shortcuts = new HashMap<Long, Shortcut>();
		//shortcuts.clear();
		EdgeWriteIterator iter1 = g.getIncoming(v);
		// TODO PERFORMANCE collect outgoing nodes (goalnodes) only once and just skip u
		while (iter1.next()) {
			int u = iter1.node();
			int lu = g.getLevel(u);
			if (lu != 0)
				continue;

			double v_u_weight = iter1.distance();

			// one-to-many shortest path
			goalNodes.clear();
			EdgeWriteIterator iter2 = g.getOutgoing(v);
			double maxWeight = 0;
			while (iter2.next()) {
				int w = iter2.node();
				int lw = g.getLevel(w);
				if (w == u || lw != 0)
					continue;

				NodeCH n = new NodeCH();
				n.endNode = w;
				n.originalEdges = getOrigEdges(iter2.edge());
				n.distance = v_u_weight + iter2.distance();
				goalNodes.add(n);

				if (maxWeight < n.distance)
					maxWeight = n.distance;
			}

			if (goalNodes.isEmpty())
				continue;

			// TODO instead of a weight-limit we could use a hop-limit
			// and successively increasing it when mean-degree of graph increases
			algo = new OneToManyDijkstraCH(g).setFilter(edgeFilter.setSkipNode(v));
			algo.setLimit(maxWeight).calcPath(u, goalNodes);
			internalFindShortcuts(goalNodes, u, iter1.edge());
		}
		return shortcuts.values();
	}

	void internalFindShortcuts(List<NodeCH> goalNodes, int u, int uEdgeId) {
		int uOrigEdge = getOrigEdges(uEdgeId);
		for (NodeCH n : goalNodes) {
			if (n.entry != null) {
				Path path = algo.extractPath(n.entry);
				if (path.found() && path.weight() <= n.distance) {
					// FOUND witness path, so do not add shortcut
					continue;
				}
			}

			// FOUND shortcut but be sure that it is the only shortcut in the collection
			// and also in the graph for u->w. If existing AND identical length => update flags
			// Hint: shortcuts are always one-way due to distinct level of every endNode but we don't
			// know yet the levels so we need to determine the correct direction or if both directions
			long edgeId = (long) u * refs.length + n.endNode;
			Shortcut sc = shortcuts.get(edgeId);
			if (sc == null)
				sc = shortcuts.get((long) n.endNode * refs.length + u);

			// minor improvement: if (shortcuts.containsKey((long) n.endNode * refs.length + u))
			// then two shortcuts with the same nodes (u<->n.endNode) exists => check current shortcut against both
			if (sc == null || !NumHelper.equals(sc.distance, n.distance)) {
				sc = new Shortcut(u, n.endNode, n.distance);
				shortcuts.put(edgeId, sc);
				sc.edge = uEdgeId;
				sc.originalEdges = uOrigEdge + n.originalEdges;
			} else {
				// the shortcut already exists in the current collection (different direction)
				// but has identical length so change the flags!
				sc.flags = scBothDir;
			}
		}
	}

	static class Shortcut {

		final int from;
		final int to;
		final double distance;
		int edge;
		int originalEdges;

		public Shortcut(int from, int to, double distance) {
			this.from = from;
			this.to = to;
			this.distance = distance;
		}

		public String toString() {
			return from + "->" + to + ", dist:" + distance;
		}

	}

	static class NodeCH {

		int endNode;

		int originalEdges;
		//EdgeEntry entry;
		double distance;

		public String toString() {
			return "" + endNode;
		}
	}

	static class Edge {
		final int t;
		final double dist;

		Edge(int t, double dist) {
			this.t = t;
			this.dist = dist;
		}
	}

	public static void main(String[] args) {
		int n = 3;
		List<Edge>[] g = new List[n];
		for (int i = 0; i < n; i++)
			g[i] = new ArrayList<>();
		g[2].add(new Edge(0, 1));
		g[2].add(new Edge(1, 1));
		g[0].add(new Edge(1, 1));
		List<Edge>[] rg = new List[n];
		for (int i = 0; i < n; i++)
			rg[i] = new ArrayList<>();
		for (int i = 0; i < n; i++)
			for (Edge e : g[i])
				rg[e.t].add(new Edge(i, e.dist));
	}
	*/
}
