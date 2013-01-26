package com.graphhopper.routing.util;

/**
 * @author Peter Karich
 */
public class ShortestCarCalc implements WeightCalculation {

    public final static ShortestCarCalc DEFAULT = new ShortestCarCalc();

    @Override
    public long getTime(double distance, int flags) {
        return (long) (distance * 3.6 / CarStreetType.getSpeed(flags));
    }

    @Override public double getWeight(double distance, int flags) {
        return distance;
    }

    @Override public double revertWeight(double weight, int flags) {
        return weight;
    }

    @Override public String toString() {
        return "SHORTEST";
    }
}
