package geometry;

import java.util.*;

public class PolygonsIntersection {
    public static double overlap(Point[] polygon1, Point[] polygon2) {
        Point[][] polygons = {polygon1, polygon2};
        Set<Double> xs = new TreeSet<>();
        for (Point point : polygon1) xs.add(point.x);
        for (Point point : polygon2) xs.add(point.x);

        for (int i1 = 0, j1 = polygon1.length - 1; i1 < polygon1.length; j1 = i1++) {
            for (int i2 = 0, j2 = polygon2.length - 1; i2 < polygon2.length; j2 = i2++) {
                Point intersection = getSegmentsIntersection(polygon1[i1], polygon1[j1], polygon2[i2], polygon2[j2]);
                if (intersection != null) {
                    xs.add(intersection.x);
                }
            }
        }

        Double[] xsa = xs.toArray(new Double[xs.size()]);
        double res = 0.0;
        for (int k = 0; k + 1 < xsa.length; k++) {
            double x = (xsa[k] + xsa[k + 1]) * 0.5;
            Point sweep0 = new Point(x, 0);
            Point sweep1 = new Point(x, 1);
            List<Event> events = new ArrayList<>();

            for (int p = 0; p < 2; p++) {
                Point[] polygon = polygons[p];
                double area = 0;
                for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++)
                    area += (polygon[j].x - polygon[i].x) * (polygon[j].y + polygon[i].y);
                for (int j = 0, i = polygon.length - 1; j < polygon.length; i = j++) {
                    Point intersection = getLinesIntersection(polygon[j], polygon[i], sweep0, sweep1);
                    if (intersection != null) {
                        double y = intersection.y;
                        double x0 = polygon[i].x;
                        double x1 = polygon[j].x;
                        if (x0 < x && x1 > x) {
                            events.add(new Event(y, (int) Math.signum(area) * (1 << p)));
                        } else if (x0 > x && x1 < x) {
                            events.add(new Event(y, -(int) Math.signum(area) * (1 << p)));
                        }
                    }
                }
            }

            Collections.sort(events);

            double a = 0.0;
            int mask = 0;
            for (int j = 0; j < events.size(); j++) {
                if (mask == 3)
                    a += events.get(j).y - events.get(j - 1).y;
                mask += events.get(j).maskDelta;
            }

            res += a * (xsa[k + 1] - xsa[k]);
        }
        return res;
    }

    public static class Point {
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Event implements Comparable<Event> {
        public double y;
        public int maskDelta;

        public Event(double y, int maskDelta) {
            this.y = y;
            this.maskDelta = maskDelta;
        }

        @Override
        public int compareTo(Event o) {
            if (y != o.y)
                return Double.compare(y, o.y);
            return Integer.compare(maskDelta, o.maskDelta);
        }
    }

    static final double eps = 1e-9;

    static Point getLinesIntersection(Point p1, Point p2, Point p3, Point p4) {
        double a1 = p2.y - p1.y;
        double b1 = p1.x - p2.x;
        double c1 = -(p1.x * p2.y - p2.x * p1.y);
        double a2 = p4.y - p3.y;
        double b2 = p3.x - p4.x;
        double c2 = -(p3.x * p4.y - p4.x * p3.y);
        double det = a1 * b2 - a2 * b1;
        if (Math.abs(det) < eps)
            return null;
        double x = -(c1 * b2 - c2 * b1) / det;
        double y = -(a1 * c2 - a2 * c1) / det;
        return new Point(x, y);
    }

    static Point getSegmentsIntersection(Point p1, Point p2, Point p3, Point p4) {
        if (!isCrossIntersect(p1, p2, p3, p4))
            return null;
        return getLinesIntersection(p1, p2, p3, p4);
    }

    static boolean isCrossIntersect(Point p1, Point p2, Point p3, Point p4) {
        double z1 = (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
        double z2 = (p2.x - p1.x) * (p4.y - p1.y) - (p2.y - p1.y) * (p4.x - p1.x);
        double z3 = (p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x);
        double z4 = (p4.x - p3.x) * (p2.y - p3.y) - (p4.y - p3.y) * (p2.x - p3.x);
        return (z1 < -eps || z2 < -eps) && (z1 > eps || z2 > eps) && (z3 < -eps || z4 < -eps) && (z3 > eps || z4 > eps);
    }
}
