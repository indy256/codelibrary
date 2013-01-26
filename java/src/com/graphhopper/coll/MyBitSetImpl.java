package com.graphhopper.coll;

import java.util.BitSet;

/**
 * @author Peter Karich
 */
public class MyBitSetImpl extends BitSet implements MyBitSet {

    public MyBitSetImpl() {
    }

    public MyBitSetImpl(int nbits) {
        super(nbits);
    }

    @Override
    public boolean contains(int index) {
        return super.get(index);
    }

    @Override
    public void add(int index) {
        super.set(index);
    }

    @Override
    public int cardinality() {
        return super.cardinality();
    }

    @Override
    public void ensureCapacity(int size) {
    }

    @Override
    public int next(int index) {
        return super.nextSetBit(index);
    }
}
