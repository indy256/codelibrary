package com.graphhopper.routing;

import com.graphhopper.routing.util.ShortestCarCalc;
import com.graphhopper.routing.util.WeightCalculation;
import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;

/**
 * @author Peter Karich
 */
public abstract class AbstractRoutingAlgorithm implements RoutingAlgorithm {

    protected Graph graph;
    protected WeightCalculation weightCalc = ShortestCarCalc.DEFAULT;

    public AbstractRoutingAlgorithm(Graph graph) {
        this.graph = graph;
    }

    protected void updateShortest(EdgeEntry shortestDE, int currLoc) {
    }
}
