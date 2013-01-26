package com.graphhopper.coll;

/**
 * Wrapper interface of an integer container for different implementations like
 * OpenBitset, BitSet, ...
 *
 * @author Peter Karich,
 */
public interface MyBitSet {

    boolean contains(int index);

    void add(int index);

    void clear();

    /**
     * Ensures that the specified index is valid and can be accessed.
     */
    void ensureCapacity(int index);

    /**
     * Searches for a bigger or equal entry and returns it.
     */
    int next(int index);
}
