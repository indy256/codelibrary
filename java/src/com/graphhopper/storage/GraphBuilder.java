package com.graphhopper.storage;

/**
 * For now this is just a helper class to quickly create a LevelGraphStorage.
 *
 * @author Peter Karich
 */
public class GraphBuilder {

    private int size = 100;

    public GraphBuilder() {
    }

    /**
     * If true builder will create a LevelGraph
     *
     * @see LevelGraph
     */
    GraphBuilder levelGraph() {
        return this;
    }

    /**
     * Creates a LevelGraphStorage
     */
    public LevelGraphStorage levelGraphCreate() {
        return (LevelGraphStorage) levelGraph().create();
    }

    /**
     * Default setGraph is a LevelGraphStorage with an in memory directory and disabled
     * storing on flush. Afterwards you'll need to call LevelGraphStorage.createNew
     * to have a useable object. Better use create.
     */
    LevelGraphStorage build() {
        Directory dir;
        dir = new RAMDirectory();
        LevelGraphStorage graph = new LevelGraphStorage(dir);
        return graph;
    }

    /**
     * Default setGraph is a LevelGraphStorage with an in memory directory and disabled
     * storing on flush.
     */
    public LevelGraphStorage create() {
        return build().createNew(size);
    }
}
