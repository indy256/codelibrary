package com.graphhopper.storage;

import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.RawEdgeIterator;

/**
 * An interface to represent a (geo) setGraph - suited for efficient storage as it
 * can be requested via indices called node IDs. To get the lat,lon point you
 * need to set up a Location2IDIndex instance.
 *
 * @author Peter Karich,
 */
public interface Graph {

    /**
     * @return the number of created locations - via setNode() or edge()
     */
    int nodes();

    /**
     * Creates an edge between the nodes a and b.
     *
     * @param a the index of the starting (tower) node of the edge
     * @param b the index of the ending (tower) node of the edge
     * @param distance between a and b. Often setNode is not called - if it is
     * not a geo-setGraph - and we need the distance parameter here.
     * @param flags see EdgeFlags - involves velocity and direction
     * @return the created edge
     */
    EdgeIterator edge(int a, int b, double distance, int flags);

    EdgeIterator edge(int a, int b, double distance, boolean bothDirections);

    /**
     * The returned EdgeIterator will return endNode as node().
     *
     * @return an edge iterator over one element where the method next() has no
     * meaning and will return false.
     * @throws IllegalStateException if edgeId is not valid
     */
    EdgeIterator getEdgeProps(int edgeId, int endNode);

    /**
     * @return all edges in this setGraph
     */
    RawEdgeIterator allEdges();

    /**
     * Returns an iterator which makes it possible to traverse all edges of the
     * specified node index. Hint: use GraphUtility to go straight to certain
     * neighbor nodes. Hint: edges with both directions will returned only once!
     */
    EdgeIterator getEdges(int index);

    EdgeIterator getIncoming(int index);

    EdgeIterator getOutgoing(int index);
}
