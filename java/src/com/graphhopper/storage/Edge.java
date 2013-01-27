package com.graphhopper.storage;

/**
 * This class is used to create the shortest-path-tree from linked entities.
 *
 * @author Peter Karich,
 */
public class Edge implements Comparable<Edge> {

	public int edge;
	public int endNode;
	public double weight;
	public Edge parent;

	public Edge(int edgeId, int endNode, double distance) {
		this.edge = edgeId;
		this.endNode = endNode;
		this.weight = distance;
	}

	@Override public int compareTo(Edge o) {
		return Double.compare(weight, o.weight);
	}
}
