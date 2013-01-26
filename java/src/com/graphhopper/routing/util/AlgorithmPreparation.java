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
     * Prepares the underlying setGraph to be used by a specialized algorithm.
     */
    AlgorithmPreparation doWork();

    AlgorithmPreparation setGraph(Graph g);

    RoutingAlgorithm createAlgo();
}
