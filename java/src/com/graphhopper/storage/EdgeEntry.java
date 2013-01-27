package com.graphhopper.storage;

/**
 * This class is used to create the shortest-path-tree from linked entities.
 *
 * @author Peter Karich,
 */
public class EdgeEntry extends Edge {

    public EdgeEntry parent;

    public EdgeEntry(int edgeId, int endNode, double distance) {
        super(edgeId, endNode, distance);
    }
}
