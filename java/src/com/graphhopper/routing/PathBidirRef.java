package com.graphhopper.routing;

import com.graphhopper.storage.Edge;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;

/**
 * This class creates a DijkstraPath from two Edge's resulting from a
 * BidirectionalDijkstra
 *
 * @author Peter Karich,
 */
public class PathBidirRef extends Path {

    protected Edge edgeTo;
    private boolean switchWrapper = false;

    public PathBidirRef(Graph g) {
        super(g);
        distance = INIT_VALUE;
    }

    public PathBidirRef switchToFrom(boolean b) {
        switchWrapper = b;
        return this;
    }

    /**
     * Extracts path from two shortest-path-tree
     */
    @Override
    public Path extract() {
        distance = 0;
        if (edge == null || edgeTo == null)
            return this;

        if (switchWrapper) {
            Edge ee = edge;
            edge = edgeTo;
            edgeTo = ee;
        }

        Edge currEdge = edge;
        while (EdgeIterator.Edge.isValid(currEdge.edge)) {
            processWeight(currEdge.edge, currEdge.endNode);
            currEdge = currEdge.parent;
        }
        fromNode(currEdge.endNode);
        reverseOrder();
        currEdge = edgeTo;
        int tmpEdge = currEdge.edge;
        while (EdgeIterator.Edge.isValid(tmpEdge)) {
            currEdge = currEdge.parent;
            processWeight(tmpEdge, currEdge.endNode);
            tmpEdge = currEdge.edge;
        }
        return found(true);
    }
}
