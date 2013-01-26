package com.graphhopper.routing;

import com.graphhopper.routing.util.WeightCalculation;

/**
 * Calculates the shortest path from the specified node ids. The implementation
 * does not need to be thread safe.
 *
 * @author Peter Karich,
 */
public interface RoutingAlgorithm {

    /**
     * Calculates the fastest or shortest path.
     *
     * @return the path but check the method found() to make sure if the path is
     * valid.
     */
    Path calcPath(int from, int to);

    /**
     * Changes the used weight calculation (e.g. fastest, shortest)
     */
    RoutingAlgorithm type(WeightCalculation calc);

    /**
     * Resets the current algorithm instance. TODO should we remove it in order
     * to avoid that it is called on different threads!!
     */
    RoutingAlgorithm clear();

    /**
     * @return name of this algorithm
     */
    String name();
}
