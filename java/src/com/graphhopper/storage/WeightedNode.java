package com.graphhopper.storage;

/**
 * Helper class used in some Location2IDIndex implementations for findID
 *
 * @author Peter Karich
 */
class WeightedNode implements Comparable<WeightedNode> {

    public int node;
    public double weight;

    WeightedNode(int node, double distance) {
        this.node = node;
        this.weight = distance;
    }

    @Override public int compareTo(WeightedNode o) {
        return Double.compare(weight, o.weight);
    }

    @Override public String toString() {
        return node + " distance is " + weight;
    }
}
