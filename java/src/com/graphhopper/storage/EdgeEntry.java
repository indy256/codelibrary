package com.graphhopper.storage;

/**
 * This class is used to create the shortest-path-tree from linked entities.
 *
 * @author Peter Karich,
 */
public class EdgeEntry implements Comparable<EdgeEntry> {

	public int edge;
	public int endNode;
	public double weight;
	public EdgeEntry parent;

	public EdgeEntry(int edgeId, int endNode, double distance) {
		this.edge = edgeId;
		this.endNode = endNode;
		this.weight = distance;
	}

	@Override public int compareTo(EdgeEntry o) {
		return Double.compare(weight, o.weight);
	}
}
