package com.graphhopper.routing.util;

import com.graphhopper.storage.LevelGraph;
import com.graphhopper.util.EdgeIterator;

/**
 * Only certain nodes are accepted and therefor the others are filtered out.
 *
 * @author Peter Karich
 */
public class EdgeLevelFilter implements EdgeIterator {

    private EdgeIterator edgeIter;
    protected LevelGraph graph;

    public EdgeLevelFilter(LevelGraph g) {
        graph = g;
    }

    public EdgeIterator doFilter(EdgeIterator iter) {
        this.edgeIter = iter;
        return this;
    }

    @Override public int baseNode() {
        return edgeIter.baseNode();
    }

    @Override public final int node() {
        return edgeIter.node();
    }

    @Override public final double distance() {
        return edgeIter.distance();
    }

    @Override public final int flags() {
        return edgeIter.flags();
    }

    @Override public final boolean next() {
        while (edgeIter.next()) {
            if (!accept())
                continue;
            return true;
        }
        return false;
    }

    public boolean accept() {
        return graph.getLevel(edgeIter.baseNode()) <= graph.getLevel(edgeIter.node());
    }

    @Override public int edge() {
        return edgeIter.edge();
    }

    @Override public boolean isEmpty() {
        return false;
    }

    @Override public void distance(double dist) {
        edgeIter.distance(dist);
    }

    @Override public void flags(int flags) {
        edgeIter.flags(flags);
    }
}
