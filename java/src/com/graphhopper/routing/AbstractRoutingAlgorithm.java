package com.graphhopper.routing;

import com.graphhopper.storage.Edge;
import com.graphhopper.storage.Graph;

/**
 * @author Peter Karich
 */
public abstract class AbstractRoutingAlgorithm implements RoutingAlgorithm {

    protected final Graph graph;

    public AbstractRoutingAlgorithm(Graph graph) {
        this.graph = graph;
    }

    protected void updateShortest(Edge shortestDE, int currLoc) {
    }
}
