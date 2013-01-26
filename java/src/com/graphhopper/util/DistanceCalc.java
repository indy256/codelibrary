/*
 *  Licensed to Peter Karich under one or more contributor license 
 *  agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  Peter Karich licenses this file to you under the Apache License, 
 *  Version 2.0 (the "License"); you may not use this file except 
 *  in compliance with the License. You may obtain a copy of the 
 *  License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.util;

import com.graphhopper.util.shapes.BBox;
import static java.lang.Math.*;

/**
 * Calculates the distance of two points or one point and an edge on earth. Allow subclasses to
 * implement less or more precise calculations (so: do not use static methods!).
 * 
 * http://en.wikipedia.org/wiki/Haversine_formula
 *
 * @author Peter Karich,
 */
public class DistanceCalc {

    /**
     * mean radius of the earth
     */
    public final static double R = 6371000; // m

    /**
     * Circumference of the earth
     */
    public final static double C = 2 * PI * R;

    /**
     * Calculates distance of (from, to) in meter.
     *
     * http://en.wikipedia.org/wiki/Haversine_formula a = sin²(Δlat/2) +
     * cos(lat1).cos(lat2).sin²(Δlong/2) c = 2.atan2(√a, √(1−a)) d = R.c
     */
    public double calcDist(double fromLat, double fromLon, double toLat, double toLon) {
        double dLat = toRadians(toLat - fromLat);
        double dLon = toRadians(toLon - fromLon);
        double normedDist = sin(dLat / 2) * sin(dLat / 2)
                + cos(toRadians(fromLat)) * cos(toRadians(toLat)) * sin(dLon / 2) * sin(dLon / 2);
        return R * 2 * asin(sqrt(normedDist));
    }

    /**
     * in meter
     */
    public double calcNormalizedDist(double dist) {
        double tmp = sin(dist / 2 / R);
        return tmp * tmp;
    }

    /**
     * Calculates in normalized meter
     */
    public double calcNormalizedDist(double fromLat, double fromLon, double toLat, double toLon) {
        double dLat = toRadians(toLat - fromLat);
        double dLon = toRadians(toLon - fromLon);
        return sin(dLat / 2) * sin(dLat / 2)
                + cos(toRadians(fromLat)) * cos(toRadians(toLat)) * sin(dLon / 2) * sin(dLon / 2);
    }


    /**
     * Circumference of the earth at different latitudes (breitengrad)
     */
    public double calcCircumference(double lat) {
        return 2 * PI * R * cos(toRadians(lat));
    }

    public BBox createBBox(double lat, double lon, double radiusInMeter) {
        if (radiusInMeter <= 0)
            throw new IllegalArgumentException("Distance must not be zero or negative! " + radiusInMeter + " lat,lon:" + lat + "," + lon);

        // length of a circle at specified lat / dist
        double dLon = (360 / (calcCircumference(lat) / radiusInMeter));

        // length of a circle is independent of the longitude
        double dLat = (360 / (DistanceCalc.C / radiusInMeter));

        // Now return bounding box in coordinates
        return new BBox(lon - dLon, lon + dLon, lat - dLat, lat + dLat);
    }

    @Override
    public String toString() {
        return "EXACT";
    }
}
