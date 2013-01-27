package com.graphhopper.routing.ch;

import com.graphhopper.routing.PathBidirRef;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.EdgeSkipIterator;

/**
 * Recursively unpack shortcuts.
 *
 * @see PrepareContractionHierarchies
 * @author Peter Karich,
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
        if (EdgeIterator.Edge.isValid(iter.skippedEdge())) {
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
        int skippedEdge = mainIter.skippedEdge();
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
        // - one edge needs to be determined explicitely because we store only one as skippedEdge
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
            if (iter.isEmpty()) {
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
}
