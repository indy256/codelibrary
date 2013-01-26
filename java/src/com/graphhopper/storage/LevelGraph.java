package com.graphhopper.storage;

import com.graphhopper.util.EdgeSkipIterator;
import com.graphhopper.util.RawEdgeIterator;

/**
 * Extended setGraph interface which supports storing and retrieving the level for a node.
 *
 * @author Peter Karich,
 */
public interface LevelGraph extends Graph {

    void setLevel(int index, int level);

    int getLevel(int index);

    // override to use EdgeSkipIterator
    @Override
    EdgeSkipIterator edge(int a, int b, double distance, int flags);

    @Override
    EdgeSkipIterator edge(int a, int b, double distance, boolean bothDirections);

    @Override
    EdgeSkipIterator getEdgeProps(int edgeId, int endNode);

    @Override
    EdgeSkipIterator getEdges(int nodeId);

    @Override
    EdgeSkipIterator getIncoming(int nodeId);

    @Override
    EdgeSkipIterator getOutgoing(int nodeId);

    @Override
    RawEdgeIterator allEdges();
}
