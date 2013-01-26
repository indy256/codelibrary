package com.graphhopper.storage;

import java.util.Arrays;

/**
 * This is an in-memory data structure but with the possibility to be stored on
 * flush().
 *
 * @author Peter Karich
 */
public class RAMDataAccess extends AbstractDataAccess {

    private int[][] segments = new int[0][];
    private transient int segmentSizeIntsPower;
    private transient int indexDivisor;

    RAMDataAccess(String name, String location, boolean store) {
        super(name, location);
    }

    @Override
    public void createNew(long bytes) {
        if (segments.length > 0)
            throw new IllegalThreadStateException("already created");

        // initialize transient values
        segmentSize(segmentSizeInBytes);
        ensureCapacity(Math.max(10 * 4, bytes));
    }

    @Override
    public void ensureCapacity(long bytes) {
        long cap = capacity();
        long todoBytes = bytes - cap;
        if (todoBytes <= 0)
            return;

        int segmentsToCreate = (int) (todoBytes / segmentSizeInBytes);
        if (todoBytes % segmentSizeInBytes != 0)
            segmentsToCreate++;

        try {
            int[][] newSegs = Arrays.copyOf(segments, segments.length + segmentsToCreate);
            for (int i = segments.length; i < newSegs.length; i++) {
                newSegs[i] = new int[1 << segmentSizeIntsPower];
            }
            segments = newSegs;
        } catch (OutOfMemoryError err) {
            throw new OutOfMemoryError(err.getMessage() + " - problem when allocating new memory. Current bytes "
                    + cap + " requested new bytes:" + todoBytes + ", segmentSizeIntsPower:" + segmentSizeIntsPower);
        }
    }

    @Override
    public void setInt(long longIndex, int value) {
        int bufferIndex = (int) (longIndex >>> segmentSizeIntsPower);
        int index = (int) (longIndex & indexDivisor);
        segments[bufferIndex][index] = value;
    }

    @Override
    public int getInt(long longIndex) {
        int bufferIndex = (int) (longIndex >>> segmentSizeIntsPower);
        int index = (int) (longIndex & indexDivisor);
        return segments[bufferIndex][index];
    }

    @Override
    public long capacity() {
        return segments() * segmentSizeInBytes;
    }

    @Override
    public int segments() {
        return segments.length;
    }

    @Override
    public DataAccess segmentSize(int bytes) {
        super.segmentSize(bytes);
        segmentSizeIntsPower = (int) (Math.log(segmentSizeInBytes / 4) / Math.log(2));
        indexDivisor = segmentSizeInBytes / 4 - 1;
        return this;
    }
}
