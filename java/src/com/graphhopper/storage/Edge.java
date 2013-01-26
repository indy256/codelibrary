package com.graphhopper.storage;

/**
 * 'Edges' do not exist as separate objects in GraphHopper for the storage as
 * this would be too memory intensive. Look into EdgeIterator and
 * Graph.getEdges(index) instead. But it is used as base class in all algorithms
 * except the native BidirectionalDijkstra.
 *
 * @see EdgeEntry
 * @author Peter Karich,
 */
public class Edge implements Comparable<Edge> {

    public int edge;
    public int endNode;
    public double weight;

    public Edge(int edgeId, int endNode, double distance) {
        this.edge = edgeId;
        this.endNode = endNode;
        this.weight = distance;
    }

    @Override public int compareTo(Edge o) {
        return Double.compare(weight, o.weight);
    }
}
