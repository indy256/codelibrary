package com.graphhopper.routing.util;

import com.graphhopper.storage.Graph;

/**
 * @author Peter Karich
 */
public abstract class AbstractAlgoPreparation<T extends AlgorithmPreparation> implements AlgorithmPreparation {

    protected Graph _graph;
    private boolean prepared = false;

    @Override public AlgorithmPreparation graph(Graph g) {
        _graph = g;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override public T doWork() {
        if (prepared)
            throw new IllegalStateException("Call doWork only once!");
        prepared = true;
        // no operation
        return (T) this;
    }
}
