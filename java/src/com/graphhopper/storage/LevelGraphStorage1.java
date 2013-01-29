package com.graphhopper.storage;

import com.graphhopper.routing.util.CarStreetType;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeSkipIterator;
import com.graphhopper.util.RawEdgeIterator;

import java.util.Arrays;

/**
 * @author Andrey Naumenko
 */
public class LevelGraphStorage1 implements LevelGraph {

	static final int EDGES = 1000;
	static final int NODES = 1000;

	int[] tail = new int[NODES];
	int[] levels = new int[NODES];

	double[] len = new double[EDGES];
	double[] flags = new double[EDGES];
	double[] u = new double[EDGES];
	double[] v = new double[EDGES];
	int[] skipEdges = new int[EDGES];
	int[] prev = new int[EDGES];

	{
		Arrays.fill(prev, -1);
	}

	int nodes;
	int edges;

	@Override
	public void setLevel(int index, int level) {
		levels[index] = level;
	}

	@Override
	public int getLevel(int index) {
		return levels[index];
	}

	@Override
	public int nodes() {
		return nodes;
	}

	@Override
	public EdgeSkipIterator edge(int a, int b, double distance, boolean bothDir) {
		return edge(a, b, distance, CarStreetType.flagsDefault(bothDir));
	}

	@Override
	public EdgeSkipIterator getEdgeProps(int edgeId, int endNode) {
		return null;
	}

	@Override
	public EdgeSkipIterator edge(int a, int b, double distance, int flags) {
//		u[edges] = v;
//		cap[edges] = cap1;
//		flow[edges] = 0;
//		prev[edges] = last[u];
//		last[u] = edges++;
//
//		ensureNodeIndex(Math.max(a, b));
//		int edgeId = internalEdgeAdd(a, b, distance, flags);
//		EdgeSkipIteratorImpl it = new EdgeSkipIteratorImpl(edgeId, a, false, false);
//		it.next();
//		it.setSkippedEdge(-1);
//		return it;
		return null;
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

	private EdgeSkipIterator createEdgeIterable(int baseNode, boolean in, boolean out) {
		return new EdgeIteratorImpl1(tail[baseNode], baseNode, in, out);
	}

	@Override
	public RawEdgeIterator allEdges() {
		return new RawEdgeIterator() {
			int curEdge;

			@Override
			public boolean next() {
				return curEdge++ < edges;
			}

			@Override
			public int edge() {
				return curEdge - 1;
			}
		};
	}

	protected class EdgeIteratorImpl1 implements EdgeSkipIterator {

		final int baseNode;
		final boolean in;
		final boolean out;

		int flags;
		int node;
		int curEdge;

		// used for SingleEdge and as return value of edge()
		public EdgeIteratorImpl1(int edgeId, int baseNode, boolean in, boolean out) {
			this.curEdge = edgeId;
			this.baseNode = baseNode;
			this.in = in;
			this.out = out;
		}

		@Override
		public boolean next() {
			for (int edgeId = curEdge - 1; edgeId >= 0; edgeId--) {
				if (u[edgeId] == baseNode && in) {
					curEdge = edgeId;
					return true;
				}
				if (v[edgeId] == baseNode && out) {
					curEdge = edgeId;
					return true;
				}
			}
			return false;
		}

		@Override
		public int node() {
			return node;
		}

		@Override
		public double distance() {
			return LevelGraphStorage1.this.len[curEdge];
		}

		@Override
		public void distance(double dist) {
			LevelGraphStorage1.this.len[curEdge] = dist;
		}

		@Override
		public int flags() {
			return flags;
		}

		@Override
		public void flags(int fl) {
			LevelGraphStorage1.this.flags[curEdge] = fl;
		}

		@Override
		public int baseNode() {
			return baseNode;
		}

		@Override
		public int edge() {
			return curEdge;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public int getSkippedEdge() {
			return LevelGraphStorage1.this.skipEdges[curEdge];
		}

		@Override
		public void setSkippedEdge(int edgeId) {
			LevelGraphStorage1.this.skipEdges[curEdge] = edgeId;
		}
	}

}
