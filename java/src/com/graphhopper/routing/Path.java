package com.graphhopper.routing;

import com.graphhopper.storage.EdgeEntry;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

/**
 * Stores the nodes for the found path of an algorithm. It additionally needs
 * the edgeIds to make edge determination faster and less complex as there could
 * be several edges (u,v) especially for graphs with shortcuts.
 *
 * @author Peter Karich,
 */
public class Path {

    protected final static double INIT_VALUE = Double.MAX_VALUE;
    protected Graph graph;
    protected double weight;
    protected double distance;
    protected boolean found;
    // we go upwards (via EdgeEntry.parent) from the goal node to the origin node
    protected boolean reverse = true;
    protected EdgeEntry edgeEntry;
    private int fromNode = EdgeIterator.NO_EDGE;
    private TIntList edgeIds;

    Path() {
        this(null);
    }

    public Path(Graph graph) {
        this.graph = graph;
        this.edgeIds = new TIntArrayList();
    }

    public Path edgeEntry(EdgeEntry edgeEntry) {
        this.edgeEntry = edgeEntry;
        return this;
    }

    protected void addEdge(int edge) {
        edgeIds.add(edge);
    }

    /**
     * We need to remember fromNode explicitely as its not saved in one edgeId
     * of edgeIds.
     */
    protected Path fromNode(int node) {
        fromNode = node;
        return this;
    }

    /**
     * @return the first node of this Path.
     */
    public int fromNode() {
        if (!EdgeIterator.Edge.isValid(fromNode))
            throw new IllegalStateException("Call extract() before retrieving fromNode");
        return fromNode;
    }

    public boolean found() {
        return found;
    }

    public Path found(boolean found) {
        this.found = found;
        return this;
    }

    void reverseOrder() {
        reverse = !reverse;
        edgeIds.reverse();
    }

    /**
     * @return distance in meter
     */
    public double distance() {
        return distance;
    }

    /**
     * The final weight which is the sum from the weights of the used edges.
     */
    public double weight() {
        return weight;
    }

    /**
     * Extracts the Path from the shortest-path-tree determined by edgeEntry.
     */
    public Path extract() {
        EdgeEntry goalEdge = edgeEntry;
        while (EdgeIterator.Edge.isValid(goalEdge.edge)) {
            processWeight(goalEdge.edge, goalEdge.endNode);
            goalEdge = goalEdge.parent;
        }

        fromNode(goalEdge.endNode);
        reverseOrder();
        return found(true);
    }

    /**
     * Calls calcWeight and adds the edgeId.
     */
    protected void processWeight(int edgeId, int endNode) {
        calcWeight(graph.getEdgeProps(edgeId, endNode));
        addEdge(edgeId);
    }

    /**
     * This method calculates not only the weight but also the distance in
     * kilometer for the specified edge.
     */
    public void calcWeight(EdgeIterator iter) {
        double dist = iter.distance();
        int fl = iter.flags();
        weight += dist;
        distance += dist;
    }

    /**
     * Used in combination with forEveryEdge.
     */
    public static interface EdgeVisitor {

        void next(EdgeIterator iter);
    }

    /**
     * Iterates over all edges in this path and calls the visitor for it.
     */
    public void forEveryEdge(EdgeVisitor visitor) {
        int tmpNode = fromNode();
        int len = edgeIds.size();
        for (int i = 0; i < len; i++) {
            EdgeIterator iter = graph.getEdgeProps(edgeIds.get(i), tmpNode);
            if (iter.isEmpty())
                throw new IllegalStateException("Edge " + edgeIds.get(i)
                        + " was empty when requested with node " + tmpNode
                        + ", edgeIndex:" + i + ", edges:" + edgeIds.size());
            tmpNode = iter.baseNode();
            visitor.next(iter);
        }
    }

    /**
     * @return the uncached node indices of the tower nodes in this path.
     */
    public TIntList calcNodes() {
        final TIntArrayList nodes = new TIntArrayList(edgeIds.size() + 1);
        if (edgeIds.isEmpty())
            return nodes;

        int tmpNode = fromNode();
        nodes.add(tmpNode);
        forEveryEdge(new EdgeVisitor() {
            @Override public void next(EdgeIterator iter) {
                nodes.add(iter.baseNode());
            }
        });
        return nodes;
    }

    @Override public String toString() {
        return "weight:" + weight() + ", edges:" + edgeIds.size();
    }
}
