package com.graphhopper.storage;

/**
 * For now this is just a helper class to quickly create a GraphStorage.
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
     * Default setGraph is a GraphStorage with an in memory directory and disabled
     * storing on flush. Afterwards you'll need to call GraphStorage.createNew
     * to have a useable object. Better use create.
     */
    GraphStorage build() {
        Directory dir;
        dir = new RAMDirectory();
        GraphStorage graph = new LevelGraphStorage(dir);
        return graph;
    }

    /**
     * Default setGraph is a GraphStorage with an in memory directory and disabled
     * storing on flush.
     */
    public GraphStorage create() {
        return build().createNew(size);
    }
}
