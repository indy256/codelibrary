package com.graphhopper.routing.util;

/**
 * Specifies how the best route is calculated. E.g. the fastest or shortest route.
 *
 * @author Peter Karich
 */
public interface WeightCalculation {

    /**
     * @return time in seconds for the specified edge
     */
    long getTime(double distance, int flags);

    /**
     * @return the calculated weight with the specified velocity
     */
    double getWeight(double distance, int flags);

    /**
     * @return distance from specified weight
     */
    double revertWeight(double weight, int flags);
}
