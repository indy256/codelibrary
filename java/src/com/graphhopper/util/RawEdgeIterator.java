package com.graphhopper.util;

/**
 * Used as return value for Graph.getAllEdges. Different to EdgeIterator as we
 * don't have an access direction (where we could say 'base' versus 'adjacent'
 * node).
 *
 * @see com.graphhopper.storage.Graph
 * @author Peter Karich
 */
public interface RawEdgeIterator {

    boolean next();

    int edge();
}
