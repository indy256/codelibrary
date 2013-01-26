package com.graphhopper.util;

/**
 * Iterates through all edges of one node. Avoids object creation in-between via
 * direct access methods.
 *
 * Usage:
 * <pre>
 * // calls to iter.node(), distance() without next() will cause undefined behaviour
 * EdgeIterator iter = graph.getOutgoing(nodeId);
 * // or similar
 * EdgeIterator iter = graph.getIncoming(nodeId);
 * while(iter.next()) {
 *   int baseNodeId = iter.baseNode(); // equal to nodeId
 *   int adjacentNodeId = iter.node();
 *   ...
 * }
 *
 * @author Peter Karich
 */
public interface EdgeIterator {

    /**
     * To be called to go to the next edge
     */
    boolean next();

    /**
     * @return the edge id of the current edge. Do not make any assumptions
     * about the concrete values, except that for an implemention it is
     * recommended that they'll be contiguous.
     */
    int edge();

    /**
     * If you retrieve edges via "edgeIterator = graph.getEdges(nodeId)" then
     * the returned node is identical to nodeId. Often only used instead of
     * nodeId for convenience reasons. Do not confuse this with <i>source</i>
     * node of a directed edge.
     *
     * @return the requested node itself
     * @see EdgeIterator
     */
    int baseNode();

    /**
     * @return the node id of the adjacent node (to baseNode) for the current
     * edge.
     * @see EdgeIterator
     */
    int node();

    /**
     * @return the distance of the current edge edge
     */
    double distance();

    void distance(double dist);

    int flags();

    void flags(int flags);

    /**
     * @return true if no data is available where we could iterate over
     */
    boolean isEmpty();
    /**
     * integer value to indicate if an edge is valid or not which then would be
     * initialized with this value
     */
    public static final int NO_EDGE = -1;

    static class Edge {

        public static boolean isValid(int edgeId) {
            return edgeId > NO_EDGE;
        }
    }
}
