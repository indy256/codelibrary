package com.graphhopper.coll;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * A priority queue implemented by a treemap to allow fast key update. Or should we use a standard
 * b-tree?
 *
 * @author Peter Karich
 */
public class MySortedCollection {

    private int size;
	private TreeMap<Integer, TIntHashSet> map = new TreeMap<>();

    void remove(int key, int value) {
        TIntHashSet set = map.get(value);
        if (set == null || !set.remove(key))
            throw new IllegalStateException("cannot remove key " + key + " with value " + value
                    + " - did you insert " + key + "," + value + " before?");
        size--;
        if (set.isEmpty())
            map.remove(value);
    }

    public void update(int key, int oldValue, int value) {
        remove(key, oldValue);
        insert(key, value);
    }

    public void insert(int key, int value) {
        TIntHashSet set = map.get(value);
		int slidingMeanValue = 20;
		if (set == null)
            map.put(value, set = new TIntHashSet(slidingMeanValue));
//        else
//            slidingMeanValue = Math.max(5, (slidingMeanValue + set.size()) / 2);
        if (!set.add(key))
            throw new IllegalStateException("use update if you want to update " + key);
        size++;
    }

    public int peekValue() {
        if (size == 0)
            throw new IllegalStateException("collection is already empty!?");
        Entry<Integer, TIntHashSet> e = map.firstEntry();
        if (e.getValue().isEmpty())
            throw new IllegalStateException("internal set is already empty!?");
        return map.firstEntry().getKey();
    }

    /**
     * @return removes the smallest entry (key and value) from this collection
     */
    public int pollKey() {
        size--;
        if (size < 0)
            throw new IllegalStateException("collection is already empty!?");
        Entry<Integer, TIntHashSet> e = map.firstEntry();
        TIntHashSet set = e.getValue();
        TIntIterator iter = set.iterator();
        if (set.isEmpty())
            throw new IllegalStateException("internal set is already empty!?");
        int val = iter.next();
        iter.remove();
        if (set.isEmpty())
            map.remove(e.getKey());
        return val;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
