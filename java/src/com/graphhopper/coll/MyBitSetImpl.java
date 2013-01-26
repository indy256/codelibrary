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

    @Override
    public MyBitSet copyTo(MyBitSet bs) {
        bs.clear();
        if (bs instanceof MyBitSetImpl) {
            ((MyBitSetImpl) bs).or(this);
        } else {
            int len = size();
            bs.ensureCapacity(len);
            for (int index = super.nextSetBit(0); index >= 0;
                    index = super.nextSetBit(index + 1)) {
                bs.add(index);
            }
        }
        return bs;
    }
}
