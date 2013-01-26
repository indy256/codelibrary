package com.graphhopper.routing.util;

import com.graphhopper.routing.RoutingAlgorithm;
import com.graphhopper.storage.Graph;

/**
 * Holds an algorithm which can be prepared and created.
 *
 * @author Peter Karich
 */
public interface AlgorithmPreparation {

    /**
     * Prepares the underlying graph to be used by a specialized algorithm.
     */
    AlgorithmPreparation doWork();

    AlgorithmPreparation graph(Graph g);

    RoutingAlgorithm createAlgo();
}
