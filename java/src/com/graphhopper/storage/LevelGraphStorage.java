package com.graphhopper.storage;

import com.graphhopper.routing.util.CarStreetType;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeSkipIterator;
import com.graphhopper.util.GraphUtility;
import com.graphhopper.util.RawEdgeIterator;

public class LevelGraphStorage implements LevelGraph {

	private static final float INT_DIST_FACTOR = 1000f;
	protected final int E_NODEA, E_NODEB, E_LINKA, E_LINKB, E_DIST, E_FLAGS, I_SKIP_EDGE;
	protected final int I_LEVEL, N_EDGE_REF;

	protected final int edgeEntrySize;
	protected final MyDataAccess edges;

	protected final int nodeEntrySize;
	final int[] nodes = new int[1000_000];

	private int edgeCount = 0;
	private int nodeCount;

	public LevelGraphStorage(int nodeCount) {
		int edgeEntryIndex = 0;
		E_NODEA = edgeEntryIndex++;
		E_NODEB = edgeEntryIndex++;
		E_LINKA = edgeEntryIndex++;
		E_LINKB = edgeEntryIndex++;
		E_DIST = edgeEntryIndex++;
		E_FLAGS = edgeEntryIndex++;
		I_SKIP_EDGE = edgeEntryIndex++;
		edgeEntrySize = edgeEntryIndex;

		int nodeEntryIndex = 0;
		I_LEVEL = nodeEntryIndex++;
		N_EDGE_REF = nodeEntryIndex++;
		nodeEntrySize = nodeEntryIndex;

		int initBytes = Math.max(nodeCount * 4 / 50, 100);
		for (int i = 0; i < nodes.length; i += nodeEntryIndex)
			nodes[i + N_EDGE_REF] = -1;

		edges = new MyDataAccess();
		edges.createNew((long) initBytes * edgeEntrySize);
	}

	@Override
	public int nodes() {
		return nodeCount;
	}

	/**
	 * Translates double VALUE to integer in order to save it in a DataAccess
	 * object
	 */
	private int distToInt(double f) {
		return (int) (f * INT_DIST_FACTOR);
	}

	private static long incCapacity(MyDataAccess da, long deltaCap) {
		long cap = da.capacity() + (deltaCap + 3) / 4 * 4;
		da.ensureCapacity(cap);
		return cap;
	}

	void ensureNodeIndex(int nodeIndex) {
		nodeCount = Math.max(nodeCount, nodeIndex + 1);
	}

	private void ensureEdgeIndex(int edgeIndex) {
		long deltaCap = (long) edgeIndex * edgeEntrySize * 4 - edges.capacity();
		if (deltaCap <= 0)
			return;

		incCapacity(edges, deltaCap);
	}

	/**
	 * @return edgeIdPointer which is edgeId * edgeEntrySize
	 */
	int internalEdgeAdd(int fromNodeId, int toNodeId, double dist, int flags) {
		int newOrExistingEdge = nextEdge();
		connectNewEdge(fromNodeId, newOrExistingEdge);
		if (fromNodeId != toNodeId)
			connectNewEdge(toNodeId, newOrExistingEdge);
		writeEdge(newOrExistingEdge, fromNodeId, toNodeId, EdgeIterator.NO_EDGE, EdgeIterator.NO_EDGE, dist, flags);
		return newOrExistingEdge;
	}

	private int nextEdge() {
		int nextEdge = edgeCount;
		edgeCount++;
		if (edgeCount < 0)
			throw new IllegalStateException("too many edges. new edge id would be negative.");
		ensureEdgeIndex(edgeCount);
		return nextEdge;
	}

	private void connectNewEdge(int fromNodeId, int newOrExistingEdge) {
		long nodePointer = (long) fromNodeId * nodeEntrySize;
		int edge = nodes[((int) (nodePointer + N_EDGE_REF))];
		if (edge > EdgeIterator.NO_EDGE) {
			// append edge and overwrite EMPTY_LINK
			long lastEdge = getLastEdge(fromNodeId, edge);
			edges.setInt(lastEdge, newOrExistingEdge);
		} else {
			nodes[((int) (nodePointer + N_EDGE_REF))] = newOrExistingEdge;
		}
	}

	private long writeEdge(int edge, int nodeThis, int nodeOther, int nextEdge, int nextEdgeOther,
						   double distance, int flags) {
		if (nodeThis > nodeOther) {
			int tmp = nodeThis;
			nodeThis = nodeOther;
			nodeOther = tmp;

			tmp = nextEdge;
			nextEdge = nextEdgeOther;
			nextEdgeOther = tmp;

			flags = CarStreetType.swapDirection(flags);
		}

		long edgePointer = (long) edge * edgeEntrySize;
		edges.setInt(edgePointer + E_NODEA, nodeThis);
		edges.setInt(edgePointer + E_NODEB, nodeOther);
		edges.setInt(edgePointer + E_LINKA, nextEdge);
		edges.setInt(edgePointer + E_LINKB, nextEdgeOther);
		edges.setInt(edgePointer + E_DIST, distToInt(distance));
		edges.setInt(edgePointer + E_FLAGS, flags);
		return edgePointer;
	}

	protected final long getLinkPosInEdgeArea(int nodeThis, int nodeOther, long edgePointer) {
		return nodeThis <= nodeOther ? edgePointer + E_LINKA : edgePointer + E_LINKB;
	}

	private long getLastEdge(int nodeThis, long edgePointer) {
		long lastLink = -1;
		int i = 0;
		int otherNode = -1;
		for (; i < 1000; i++) {
			edgePointer *= edgeEntrySize;
			otherNode = getOtherNode(nodeThis, edgePointer);
			lastLink = getLinkPosInEdgeArea(nodeThis, otherNode, edgePointer);
			edgePointer = edges.getInt(lastLink);
			if (edgePointer == EdgeIterator.NO_EDGE)
				break;
		}

		if (i >= 1000)
			throw new IllegalStateException("endless loop? edge count of " + nodeThis
					+ " is probably not higher than " + i
					+ ", edgePointer:" + edgePointer + ", otherNode:" + otherNode);
		return lastLink;
	}

	private int getOtherNode(int nodeThis, long edgePointer) {
		int nodeA = edges.getInt(edgePointer + E_NODEA);
		if (nodeA == nodeThis)
			// return b
			return edges.getInt(edgePointer + E_NODEB);
		// return a
		return nodeA;
	}

	@Override
	public RawEdgeIterator allEdges() {
		return new AllEdgeIterator();
	}

	/**
	 * Include all edges of this storage in the iterator.
	 */
	protected class AllEdgeIterator implements RawEdgeIterator {

		protected long edgePointer = -edgeEntrySize;
		private int maxEdges = edgeCount * edgeEntrySize;

		@Override
		public boolean next() {
			edgePointer += edgeEntrySize;
			return edgePointer < maxEdges;
		}

		@Override
		public int edge() {
			return (int) (edgePointer / edgeEntrySize);
		}
	}

	protected class SingleEdge extends EdgeIteratorImpl {

		protected boolean switchFlags;

		public SingleEdge(int edgeId, int nodeId) {
			super(edgeId, nodeId, false, false);
			edgePointer = edgeId * edgeEntrySize;
			flags = flags();
		}

		@Override
		public int flags() {
			flags = edges.getInt(edgePointer + E_FLAGS);
			if (switchFlags)
				return CarStreetType.swapDirection(flags);
			return flags;
		}
	}

	protected class EdgeIteratorImpl implements EdgeIterator {

		final int baseNode;
		final boolean in;
		final boolean out;

		int flags;
		long edgePointer;
		int node;
		int edgeId;
		int nextEdge;

		// used for SingleEdge and as return value of edge()
		public EdgeIteratorImpl(int edge, int baseNode, boolean in, boolean out) {
			this.nextEdge = this.edgeId = edge;
			this.edgePointer = (long) nextEdge * edgeEntrySize;
			this.baseNode = baseNode;
			this.in = in;
			this.out = out;
		}

		boolean readNext() {
			edgePointer = (long) nextEdge * edgeEntrySize;
			edgeId = nextEdge;
			node = getOtherNode(baseNode, edgePointer);

			// position to next edge
			nextEdge = edges.getInt(getLinkPosInEdgeArea(baseNode, node, edgePointer));
			if (nextEdge == edgeId)
				throw new AssertionError("endless loop detected for " + baseNode + "," + node + "," + edgePointer);

			flags = edges.getInt(edgePointer + E_FLAGS);

			// switch direction flags if necessary
			if (baseNode > node)
				flags = CarStreetType.swapDirection(flags);

			if (!in && !CarStreetType.isForward(flags) || !out && !CarStreetType.isBackward(flags)) {
				// skip this edge as it does not fit to defined filter
				return false;
			} else {
				return true;
			}
		}

		@Override
		public boolean next() {
			int i = 0;
			boolean foundNext = false;
			for (; i < 1000; i++) {
				if (nextEdge == EdgeIterator.NO_EDGE)
					break;
				foundNext = readNext();
				if (foundNext)
					break;
			}
			// road networks typically do not have nodes with plenty of edges!
			if (i > 1000)
				throw new IllegalStateException("something went wrong: no end of edge-list found");
			return foundNext;
		}

		@Override
		public int node() {
			return node;
		}

		@Override
		public double distance() {
			return (double) edges.getInt(edgePointer + E_DIST) / INT_DIST_FACTOR;
		}

		@Override
		public void distance(double dist) {
			edges.setInt(edgePointer + E_DIST, distToInt(dist));
		}

		@Override
		public int flags() {
			return flags;
		}

		@Override
		public void flags(int fl) {
			flags = fl;
			int nep = edges.getInt(getLinkPosInEdgeArea(baseNode, node, edgePointer));
			int neop = edges.getInt(getLinkPosInEdgeArea(node, baseNode, edgePointer));
			writeEdge(edge(), baseNode, node, nep, neop, distance(), flags);
		}

		@Override
		public int baseNode() {
			return baseNode;
		}

		@Override
		public int edge() {
			return edgeId;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}
	}

	@Override
	public final int getLevel(int index) {
		ensureNodeIndex(index);
		return nodes[((int) ((long) index * nodeEntrySize + I_LEVEL))];
	}

	@Override
	public final void setLevel(int index, int level) {
		ensureNodeIndex(index);
		nodes[((int) ((long) index * nodeEntrySize + I_LEVEL))] = level;
	}

	@Override
	public EdgeSkipIterator edge(int a, int b, double distance, boolean bothDir) {
		return edge(a, b, distance, CarStreetType.flagsDefault(bothDir));
	}

	@Override
	public EdgeSkipIterator edge(int a, int b, double distance, int flags) {
		ensureNodeIndex(Math.max(a, b));
		int edgeId = internalEdgeAdd(a, b, distance, flags);
		EdgeSkipIteratorImpl iter = new EdgeSkipIteratorImpl(edgeId, a, false, false);
		iter.next();
		iter.setSkippedEdge(-1);
		return iter;
	}

	@Override
	public EdgeSkipIterator getEdges(int node) {
		return createEdgeIterable(node, true, true);
	}

	@Override
	public EdgeSkipIterator getIncoming(int node) {
		return createEdgeIterable(node, true, false);
	}

	@Override
	public EdgeSkipIterator getOutgoing(int node) {
		return createEdgeIterable(node, false, true);
	}

	protected EdgeSkipIterator createEdgeIterable(int baseNode, boolean in, boolean out) {
		int edge = nodes[((int) ((long) baseNode * nodeEntrySize + N_EDGE_REF))];
		return new EdgeSkipIteratorImpl(edge, baseNode, in, out);
	}

	class EdgeSkipIteratorImpl extends EdgeIteratorImpl implements EdgeSkipIterator {

		public EdgeSkipIteratorImpl(int edge, int node, boolean in, boolean out) {
			super(edge, node, in, out);
		}

		@Override
		public int getSkippedEdge() {
			return edges.getInt(edgePointer + I_SKIP_EDGE);
		}

		@Override
		public void setSkippedEdge(int edgeId) {
			edges.setInt(edgePointer + I_SKIP_EDGE, edgeId);
		}
	}

	@Override
	public EdgeSkipIterator getEdgeProps(int edgeId, int endNode) {
		if (edgeId <= EdgeIterator.NO_EDGE || edgeId > edgeCount)
			throw new IllegalStateException("edgeId " + edgeId + " out of bounds [0," + edgeCount + "]");
		if (endNode < 0)
			throw new IllegalStateException("endNode " + endNode + " out of bounds [0," + nodeCount + "]");
		long edgePointer = (long) edgeId * edgeEntrySize;
		// a bit complex but faster
		int nodeA = edges.getInt(edgePointer + E_NODEA);
		int nodeB = edges.getInt(edgePointer + E_NODEB);
		EdgeSkipIterator edge;
		if (endNode == nodeB) {
			edge = createSingleEdge(edgeId, nodeA);
			((EdgeIteratorImpl) edge).node = nodeB;
			return edge;
		} else if (endNode == nodeA) {
			edge = createSingleEdge(edgeId, nodeB);
			((EdgeIteratorImpl) edge).node = nodeA;
			((SingleEdge) edge).switchFlags = true;
			return edge;
		} else
			return GraphUtility.EMPTY;
	}

	protected EdgeSkipIterator createSingleEdge(int edge, int nodeId) {
		return new SingleLevelEdge(edge, nodeId);
	}

	class SingleLevelEdge extends SingleEdge implements EdgeSkipIterator {

		public SingleLevelEdge(int edge, int nodeId) {
			super(edge, nodeId);
		}

		@Override
		public void setSkippedEdge(int node) {
			edges.setInt(edgePointer + I_SKIP_EDGE, node);
		}

		@Override
		public int getSkippedEdge() {
			return edges.getInt(edgePointer + I_SKIP_EDGE);
		}
	}

}
