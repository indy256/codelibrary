package com.graphhopper.util;

import com.graphhopper.storage.Graph;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is introduced as a helper to avoid cluttering the Graph interface
 * with all the common methods. Most of the methods are useful for unit tests.
 *
 * @author Peter Karich,
 */
public class GraphUtility {

	public static int count(EdgeIterator iter) {
		int res = 0;
		while (iter.next())
			++res;
		return res;
	}

	public static int count(RawEdgeIterator iter) {
		int counter = 0;
		while (iter.next()) {
			++counter;
		}
		return counter;
	}

	/**
	 * Added this helper method to avoid cluttering the setGraph interface. Good
	 * idea?
	 */
	public static EdgeIterator getEdges(Graph graph, int index, boolean out) {
		if (out)
			return graph.getOutgoing(index);
		else
			return graph.getIncoming(index);
	}

	public static final EdgeSkipIterator EMPTY = new EdgeSkipIterator() {
		@Override
		public int skippedEdge() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public void skippedEdge(int node) {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public void distance(double dist) {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public void flags(int flags) {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public boolean next() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public int edge() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public int baseNode() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public int node() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public double distance() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public int flags() {
			throw new UnsupportedOperationException("Not supported. Edge is empty.");
		}

		@Override
		public boolean isEmpty() {
			return true;
		}
	};
}