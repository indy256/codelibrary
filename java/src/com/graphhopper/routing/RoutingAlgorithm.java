package com.graphhopper.routing;

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
	 *         valid.
	 */
	Path calcPath(int from, int to);
}
