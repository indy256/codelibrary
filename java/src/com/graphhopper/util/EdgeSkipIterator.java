package com.graphhopper.util;

import com.graphhopper.storage.LevelGraph;

/**
 * Support for skipped edge
 *
 * @see LevelGraph
 * @author Peter Karich
 */
public interface EdgeSkipIterator extends EdgeIterator {

    int getSkippedEdge();

    void setSkippedEdge(int edgeId);
}
