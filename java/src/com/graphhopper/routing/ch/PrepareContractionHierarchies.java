package com.graphhopper.routing.ch;

import com.graphhopper.routing.DijkstraBidirectionRef;
import com.graphhopper.routing.DijkstraSimple;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.routing.util.AbstractAlgoPreparation;
import com.graphhopper.routing.util.CarStreetType;
import com.graphhopper.routing.util.EdgeLevelFilter;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.storage.LevelGraph;
import com.graphhopper.util.*;
import gnu.trove.list.array.TIntArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * This class prepares the setGraph for a bidirectional algorithm supporting
 * contraction hierarchies ie. an algorithm returned by createAlgo.
 * <p/>
 * There are several description of contraction hierarchies available. The
 * following is one of the more detailed:
 * http://web.cs.du.edu/~sturtevant/papers/highlevelpathfinding.pdf
 * <p/>
 * The only difference is that we use a skipped edgeId instead of skipped nodes
 * for faster unpacking.
 *
 * @author Peter Karich
 */
public class PrepareContractionHierarchies extends AbstractAlgoPreparation<PrepareContractionHierarchies> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private LevelGraph g;
	// the most important nodes comes last
	private TreeSet<WeightedNode> sortedNodes = new TreeSet<>();
	private WeightedNode refs[];
	private TIntArrayList originalEdges;
	// shortcut is one direction, speed is only involved while recalculating the endNode weights - see prepareEdges
	static final int scOneDir = CarStreetType.flags(0, false);
	static final int scBothDir = CarStreetType.flags(0, true);
	private Collection<Shortcut> shortcuts = new ArrayList<Shortcut>();
	private EdgeLevelFilterCH edgeFilter;
	private OneToManyDijkstraCH algo;

	@Override
	public PrepareContractionHierarchies setGraph(Graph g) {
		this.g = (LevelGraph) g;
		return this;
	}

	@Override
	public PrepareContractionHierarchies doWork() {
		super.doWork();
		initFromGraph();
		// TODO integrate PrepareRoutingShortcuts -> so avoid all nodes with negative level in the other methods
		// in PrepareShortcuts level 0 and -1 is already used move that to level 1 and 2 so that level 0 stays as uncontracted

		prepareEdges();
		prepareNodes();
		contractNodes();
		return this;
	}

	PrepareContractionHierarchies initFromGraph() {
		originalEdges = new TIntArrayList(g.nodes() / 2, -1);
		edgeFilter = new EdgeLevelFilterCH(this.g);
		sortedNodes = new TreeSet<>();
		refs = new WeightedNode[g.nodes()];
		return this;
	}

	void prepareEdges() {
		for (RawEdgeIterator it = g.allEdges(); it.next(); ) {
			setOrigEdgeCount(it.edge(), 1);
		}
	}

	void prepareNodes() {
		int len = g.nodes();
		// minor idea: 1. sort nodes randomly and 2. pre-init with endNode degree
		for (int node = 0; node < len; node++) {
			refs[node] = new WeightedNode(node, calculatePriority(node));
			sortedNodes.add(refs[node]);
		}
	}

	/**
	 * Calculates the priority of endNode v without changing the setGraph. Warning:
	 * the calculated priority must NOT depend on priority(v) and therefor
	 * findShortcuts should also not depend on the priority(v). Otherwise
	 * updating the priority before contracting in contractNodes() could lead to
	 * a slowishor even endless loop.
	 */
	int calculatePriority(int v) {
		Collection<Shortcut> tmpShortcuts = findShortcuts(v);

		int degree = GraphUtility.count(g.getEdges(v));
		int edgeDifference = tmpShortcuts.size() - degree;

		int originalEdgesCount = 0;
		for (Shortcut sc : tmpShortcuts)
			originalEdgesCount += sc.originalEdges;

		int contractedNeighbors = 0;
		for (EdgeSkipIterator it = g.getEdges(v); it.next(); )
			if (EdgeIterator.Edge.isValid(it.skippedEdge()))
				++contractedNeighbors;

		return 10 * edgeDifference + 50 * originalEdgesCount + contractedNeighbors;
	}

	void contractNodes() {
		int level = 1;
		int newShortcuts = 0;
		final int updateSize = Math.max(10, sortedNodes.size() / 10);
		int counter = 0;
		int updateCounter = 0;
		// no update all => 600k shortcuts and 3min
		while (!sortedNodes.isEmpty()) {
			if (counter % updateSize == 0) {
				// periodically update priorities of ALL nodes
				if (updateCounter > 0 && updateCounter % 2 == 0) {
					// TODO avoid to traverse all nodes -> via a new sortedNodes.iterator()
					for (int node = 0; node < g.nodes(); node++) {
						if (g.getLevel(node) != 0)
							continue;
						WeightedNode wNode = refs[node];
						sortedNodes.remove(wNode);
						wNode.priority = calculatePriority(node);
						sortedNodes.add(wNode);
					}
				}
				updateCounter++;
			}

			++counter;
			WeightedNode wn = refs[sortedNodes.pollFirst().node];
			wn.priority = calculatePriority(wn.node);
			if (!sortedNodes.isEmpty() && wn.priority > sortedNodes.first().priority) {
				// endNode got more important => insert as new value and contract it later
				sortedNodes.add(wn);
				continue;
			}

			// contract!
			newShortcuts += addShortcuts(wn.node);
			g.setLevel(wn.node, level);
			level++;

			// recompute priority of uncontracted neighbors
			EdgeIterator iter = g.getEdges(wn.node);
			while (iter.next()) {
				if (g.getLevel(iter.node()) != 0)
					// already contracted no update necessary
					continue;

				int nn = iter.node();
				WeightedNode neighborWn = refs[nn];
				int newPriority = calculatePriority(nn);
				if (neighborWn.priority != newPriority) {
					sortedNodes.remove(neighborWn);
					neighborWn.priority = newPriority;
					sortedNodes.add(neighborWn);
				}
			}
		}
		logger.info("new shortcuts " + newShortcuts + ", prioNodeCollection:" + sortedNodes);
		// System.out.println("new shortcuts " + newShortcuts);
	}

	static class EdgeLevelFilterCH extends EdgeLevelFilter {

		int avoidNode;

		public EdgeLevelFilterCH(LevelGraph g) {
			super(g);
		}

		public EdgeLevelFilterCH setAvoidNode(int node) {
			this.avoidNode = node;
			return this;
		}

		@Override
		public boolean accept() {
			// ignore if it is skipNode or a endNode already contracted
			return avoidNode != node() && graph.getLevel(node()) == 0;
		}
	}

	/**
	 * Finds shortcuts, does not change the underlying setGraph.
	 */
	Collection<Shortcut> findShortcuts(int v) {
		// we can use distance instead of weight, see prepareEdges where distance is overwritten by weight!
		List<NodeCH> goalNodes = new ArrayList<NodeCH>();
		shortcuts.clear();
		EdgeIterator iter1 = g.getIncoming(v);
		// TODO PERFORMANCE collect outgoing nodes (goal-nodes) only once and just skip u
		while (iter1.next()) {
			int u = iter1.node();
			int lu = g.getLevel(u);
			if (lu != 0)
				continue;

			double v_u_weight = iter1.distance();

			// one-to-many extractPath path
			goalNodes.clear();
			EdgeIterator iter2 = g.getOutgoing(v);
			double maxWeight = 0;
			while (iter2.next()) {
				int w = iter2.node();
				int lw = g.getLevel(w);
				if (w == u || lw != 0)
					continue;

				NodeCH n = new NodeCH();
				n.endNode = w;
				n.originalEdges = getOrigEdgeCount(iter2.edge());
				n.distance = v_u_weight + iter2.distance();
				goalNodes.add(n);

				if (maxWeight < n.distance)
					maxWeight = n.distance;
			}

			if (goalNodes.isEmpty())
				continue;

			// TODO instead of a weight-limit we could use a hop-limit
			// and successively increasing it when mean-degree of setGraph increases
			algo = new OneToManyDijkstraCH(g).setFilter(edgeFilter.setAvoidNode(v));
			algo.setLimit(maxWeight).calcPath(u, goalNodes);
			internalFindShortcuts(goalNodes, u, iter1.edge());
		}
		return shortcuts;
	}

	void internalFindShortcuts(List<NodeCH> goalNodes, int u, int skippedEdge) {
		int uOrigEdgeCount = getOrigEdgeCount(skippedEdge);
		for (NodeCH n : goalNodes) {
			if (n.entry != null) {
				Path path = algo.extractPath(n.entry);
				if (path.found() && path.distance() <= n.distance) {
					// FOUND witness path, so do not add shortcut
					continue;
				}
			}

			// FOUND shortcut but be sure that it is the only shortcut in the collection
			// and also in the setGraph for u->w. If existing AND identical length => update flags.
			// Hint: shortcuts are always one-way due to distinct level of every endNode but we don't
			// know yet the levels so we need to determine the correct direction or if both directions

			// minor improvement: if (shortcuts.containsKey((long) n.endNode * refs.length + u))
			// then two shortcuts with the same nodes (u<->n.endNode) exists => check current shortcut against both

			boolean found = false;
			for (Shortcut tmp : shortcuts) {
				if (Double.compare(n.distance, tmp.distance) == 0) {
					// same direction -> no shortcut, no update
					if (tmp.from == u && tmp.to == n.endNode) {
						found = true;
						break;
						// different direction -> no shortcut, update
					} else if (tmp.from == n.endNode && tmp.to == u) {
						tmp.flags = scBothDir;
						found = true;
						break;
					}
					// new shortcut
				}
			}
			if (found)
				continue;
			Shortcut sc = new Shortcut(u, n.endNode, n.distance);
			shortcuts.add(sc);
			sc.skippedEdge = skippedEdge;
			sc.originalEdges = uOrigEdgeCount + n.originalEdges;
		}
	}

	/**
	 * Introduces the necessary shortcuts for endNode v in the setGraph.
	 */
	int addShortcuts(int v) {
		Collection<Shortcut> foundShortcuts = findShortcuts(v);
		int newShortcuts = 0;
		for (Shortcut sc : foundShortcuts) {
			boolean updatedInGraph = false;
			// check if we need to update some existing shortcut in the setGraph
			EdgeSkipIterator iter = g.getOutgoing(sc.from);
			while (iter.next()) {
				if (EdgeIterator.Edge.isValid(iter.skippedEdge())
						&& iter.node() == sc.to
//						&& CarStreetType.canBeOverwritten(iter.flags(), sc.flags)
						&& iter.distance() > sc.distance) {
					iter.flags(sc.flags);
					iter.skippedEdge(sc.skippedEdge);
					iter.distance(sc.distance);
					setOrigEdgeCount(iter.edge(), sc.originalEdges);
					updatedInGraph = true;
					break;
				}
			}

			if (!updatedInGraph) {
				iter = g.edge(sc.from, sc.to, sc.distance, sc.flags);
				iter.skippedEdge(sc.skippedEdge);
				setOrigEdgeCount(iter.edge(), sc.originalEdges);
				newShortcuts++;
			}
		}
		return newShortcuts;
	}

	private void setOrigEdgeCount(int index, int value) {
		originalEdges.ensureCapacity(index + 1);
		originalEdges.setQuick(index, value);
	}

	private int getOrigEdgeCount(int index) {
		originalEdges.ensureCapacity(index + 1);
		return originalEdges.getQuick(index);
	}

	@Override
	public DijkstraBidirectionRef createAlgo() {
		// do not change weight within DijkstraBidirectionRef => so use ShortestCalc
		DijkstraBidirectionRef dijkstra = new DijkstraBidirectionRef(g) {
			@Override
			protected void initCollections(int nodes) {
				// algorithm with CH does not need that much memory pre allocated
				super.initCollections(Math.min(10000, nodes));
			}

			@Override
			public boolean checkFinishCondition() {
				// changed finish condition for CH
				if (currFrom == null)
					return currTo.weight >= shortest.distance();
				else if (currTo == null)
					return currFrom.weight >= shortest.distance();
				return currFrom.weight >= shortest.distance() && currTo.weight >= shortest.distance();
			}

			@Override
			protected PathBidirRef createPath() {
				return new Path4CH(graph);
			}
		};
		dijkstra.edgeFilter(new EdgeLevelFilter(g));
		return dijkstra;
	}

	// we need to use DijkstraSimple as AStar or DijkstraBidirection cannot be efficiently used with multiple goals
	static class OneToManyDijkstraCH extends DijkstraSimple {

		EdgeLevelFilter filter;
		double limit;
		Collection<NodeCH> goals;

		public OneToManyDijkstraCH(Graph graph) {
			super(graph);
		}

		public OneToManyDijkstraCH setFilter(EdgeLevelFilter filter) {
			this.filter = filter;
			return this;
		}

		@Override
		protected final EdgeIterator neighbors(int neighborNode) {
			return filter.doFilter(super.neighbors(neighborNode));
		}

		OneToManyDijkstraCH setLimit(double weight) {
			limit = weight;
			return this;
		}

		@Override
		public Path calcPath(int from, int to) {
			throw new IllegalArgumentException("call the other calcPath instead");
		}

		Path calcPath(int from, Collection<NodeCH> goals) {
			this.goals = goals;
			return super.calcPath(from, -1);
		}

		@Override
		public boolean finished(EdgeEntry curr, int _ignoreTo) {
			if (curr.weight > limit)
				return true;

			int found = 0;
			for (NodeCH n : goals) {
				if (n.endNode == curr.endNode) {
					n.entry = curr;
					found++;
				} else if (n.entry != null) {
					found++;
				}
			}
			return found == goals.size();
		}
	}

	private static class WeightedNode implements Comparable<WeightedNode> {
		final int node;
		int priority;

		public WeightedNode(int node, int priority) {
			this.node = node;
			this.priority = priority;
		}

		@Override
		public int compareTo(WeightedNode o) {
			int d = Integer.compare(priority, o.priority);
			if (d != 0) return d;
			return Integer.compare(node, o.node);
		}
	}

	static class Shortcut {

		int from;
		int to;
		int skippedEdge;
		double distance;
		int originalEdges;
		int flags = scOneDir;

		public Shortcut(int from, int to, double dist) {
			this.from = from;
			this.to = to;
			this.distance = dist;
		}

		@Override
		public String toString() {
			return from + "->" + to + ", dist:" + distance;
		}
	}

	static class NodeCH {

		int endNode;
		int originalEdges;
		EdgeEntry entry;
		double distance;

		@Override
		public String toString() {
			return "" + endNode;
		}
	}
}
