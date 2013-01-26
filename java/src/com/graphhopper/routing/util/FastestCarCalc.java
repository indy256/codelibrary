package com.graphhopper.routing.util;

/**
 * @author Peter Karich
 */
public class FastestCarCalc implements WeightCalculation {

    public final static FastestCarCalc DEFAULT = new FastestCarCalc();

    private FastestCarCalc() {
    }

    @Override
    public long getTime(double distance, int flags) {
        return (long) (distance * 3.6 / CarStreetType.getSpeed(flags));
    }

    @Override
    public double getWeight(double distance, int flags) {
        return distance / CarStreetType.getSpeedPart(flags);
    }

    @Override public double revertWeight(double weight, int flags) {
        return weight * CarStreetType.getSpeedPart(flags);
    }

    @Override public String toString() {
        return "FASTEST";
    }
}
