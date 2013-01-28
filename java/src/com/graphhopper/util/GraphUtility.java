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
		return out ? graph.getOutgoing(index) : graph.getIncoming(index);
	}
}
