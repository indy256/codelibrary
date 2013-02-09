package com.graphhopper.routing.ch;

import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeSkipIterator;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Recursively unpack shortcuts.
 *
 * @author Peter Karich,
 * @see PrepareContractionHierarchies
 */
public class Path4CH extends PathBidirRef {

	public Path4CH(Graph g) {
		super(g);
	}

	@Override
	protected void processWeight(int tmpEdge, int endNode) {
		EdgeIterator mainIter = graph.getEdgeProps(tmpEdge, endNode);

		// Shortcuts do only contain valid weight so first expand before adding
		// to distance and time
		EdgeSkipIterator iter = (EdgeSkipIterator) mainIter;
		if (EdgeIterator.Edge.isValid(iter.getSkippedEdge())) {
			expandEdge(iter, false);
		} else {
			// only add if it is not a shortcut
			calcWeight(mainIter);
			addEdge(mainIter.edge());
		}
	}

	@Override
	public void calcWeight(EdgeIterator mainIter) {
		double dist = mainIter.distance();
		distance += dist;
	}

	private void expandEdge(EdgeSkipIterator mainIter, boolean revert) {
		int skippedEdge = mainIter.getSkippedEdge();
		if (!EdgeIterator.Edge.isValid(skippedEdge)) {
			calcWeight(mainIter);
			addEdge(mainIter.edge());
			return;
		}
		int from = mainIter.baseNode(), to = mainIter.node();
		if (revert) {
			int tmp = from;
			from = to;
			to = tmp;
		}

		// 2 things => 2*2=4 sitations
		// - one edge needs to be determined explicitely because we store only one as setSkippedEdge
		// - getEdgeProps could possibly return an empty edge if the shortcuts is available for both directions
		if (reverse) {
			EdgeSkipIterator iter = (EdgeSkipIterator) graph.getEdgeProps(skippedEdge, from);
			if (iter.isEmpty()) {
				iter = (EdgeSkipIterator) graph.getEdgeProps(skippedEdge, to);
				int skippedNode = iter.baseNode();
				expandEdge(iter, false);
				findSkippedEdge(from, skippedNode);
			} else {
				int skippedNode = iter.baseNode();
				findSkippedEdge(skippedNode, to);
				expandEdge(iter, true);
			}
		} else {
			EdgeSkipIterator iter = (EdgeSkipIterator) graph.getEdgeProps(skippedEdge, to);
			if (iter == null) {
				iter = (EdgeSkipIterator) graph.getEdgeProps(skippedEdge, from);
				int skippedNode = iter.baseNode();
				expandEdge(iter, true);
				findSkippedEdge(skippedNode, to);
			} else {
				int skippedNode = iter.baseNode();
				findSkippedEdge(from, skippedNode);
				expandEdge(iter, false);
			}
		}
	}

	private void findSkippedEdge(int from, int to) {
		EdgeSkipIterator iter = (EdgeSkipIterator) graph.getOutgoing(from);
		double lowest = Double.MAX_VALUE;
		int edge = EdgeIterator.NO_EDGE;
		while (iter.next()) {
			if (iter.node() == to && iter.distance() < lowest) {
				lowest = iter.distance();
				edge = iter.edge();
			}
		}
		if (EdgeIterator.Edge.isValid(edge))
			expandEdge((EdgeSkipIterator) graph.getEdgeProps(edge, to), false);
	}

	static int sum(int n) {
		++n;
		int res = 0;
		for (int d = 1; d <= 100_000_000; d *= 10) {
			int blocks = n / (d * 10);
			int lastdigit = n % (d * 10) / d;
			res += blocks * d * 45 + lastdigit * (lastdigit - 1) / 2 * d + n % d * lastdigit;
		}
		return res;
	}

	static int sum2(int n) {
		int res = 0;
		for (int i = 0; i <= n; i++)
			for (char x : ("" + i).toCharArray())
				res += x - '0';
		return res;
	}

	public static void main(String[] args) {
		for (int i = 1; i < 2000; i++) {
			int res1 = sum(i);
			int res2 = sum2(i);
			if (res1 != res2)
				System.out.println(i + " " + res1 + " " + res2);
		}
	}
}
