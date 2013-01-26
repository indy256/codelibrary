package com.graphhopper.storage;

/**
 * This class is used to create the shortest-path-tree from linked entities.
 *
 * @author Peter Karich,
 */
public class EdgeEntry extends Edge implements Cloneable {

    public EdgeEntry parent;

    public EdgeEntry(int edgeId, int endNode, double distance) {
        super(edgeId, endNode, distance);
    }

    @Override
    public EdgeEntry clone() {
        return new EdgeEntry(edge, endNode, weight);
    }
}
