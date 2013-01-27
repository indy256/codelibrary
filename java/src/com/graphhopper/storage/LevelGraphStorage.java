package com.graphhopper.storage;

import com.graphhopper.util.EdgeSkipIterator;

/**
 * A Graph necessary for shortcut algorithms like Contraction Hierarchies. This
 * class enables the storage to hold the level of a node and a shortcut edge per
 * edge.
 *
 * @see GraphBuilder
 * @author Peter Karich
 */
public class LevelGraphStorage extends GraphStorage implements LevelGraph {

    private final int I_SKIP_EDGE;
    private final int I_LEVEL;

    public LevelGraphStorage(Directory dir) {
        super(dir);
        I_SKIP_EDGE = nextEdgeEntryIndex();
        I_LEVEL = nextNodeEntryIndex();
        initNodeAndEdgeEntrySize();
    }

}
