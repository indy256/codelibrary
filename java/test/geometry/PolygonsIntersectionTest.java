package test.geometry;

import geometry.PolygonsIntersection;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.*;
import java.util.List;

public class PolygonsIntersectionTest {
    // random test
    public static void main(String[] args) {
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(20) + 3;
            int range = 10;
            PolygonsIntersection.Point[] polygon1 = convert(getRandomPolygon(n, range, range));
            PolygonsIntersection.Point[] polygon2 = convert(getRandomPolygon(n, range, range));

            double res1 = PolygonsIntersection.overlap(polygon1, polygon2);
            double res2 = overlap2(polygon1, polygon2);

            if (Math.abs(res1 - res2) > 1e-9)
                throw new RuntimeException();
        }
    }

    static PolygonsIntersection.Point[] convert(int[][] xy) {
        PolygonsIntersection.Point[] polygon = new PolygonsIntersection.Point[xy[0].length];
        for (int i = 0; i < xy[0].length; i++) {
            polygon[i] = new PolygonsIntersection.Point(xy[0][i], xy[1][i]);
        }
        return polygon;
    }

    static double overlap2(PolygonsIntersection.Point[] points1, PolygonsIntersection.Point[] points2) {
        Area a = new Area(getPolygon(points1));
        a.intersect(new Area(getPolygon(points2)));
        double pt[] = new double[6];
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        double area = 0;
        for (PathIterator p = a.getPathIterator(null); !p.isDone(); p.next()) {
            if (p.currentSegment(pt) == PathIterator.SEG_CLOSE) {
                double cur = 0;
                for (int i = 0, j = x.size() - 1; i < x.size(); j = i++)
                    cur += x.get(i) * y.get(j) - x.get(j) * y.get(i);
                x.clear();
                y.clear();
                area += Math.abs(cur / 2);
            } else {
                x.add(pt[0]);
                y.add(pt[1]);
            }
        }
        return area;
    }

    static Polygon getPolygon(PolygonsIntersection.Point[] points) {
        Polygon polygon = new Polygon();
        for (PolygonsIntersection.Point point : points) polygon.addPoint((int) point.x, (int) point.y);
        return polygon;
    }

    static Random rnd = new Random(1);

    static int[][] getRandomPolygon(int n, int maxWidth, int maxHeight) {
        int[] x = new int[n];
        int[] y = new int[n];
        int[] p = new int[n];
        while (true) {
            for (int i = 0; i < n; i++) {
                x[i] = rnd.nextInt(maxWidth);
                y[i] = rnd.nextInt(maxHeight);
                p[i] = i;
            }
            for (boolean improved = true; improved;) {
                improved = false;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int[] p1 = p.clone();
                        reverse(p1, i, j);
                        if (len(x, y, p) > len(x, y, p1)) {
                            p = p1;
                            improved = true;
                        }
                    }
                }
            }
            int[] tx = x.clone();
            int[] ty = y.clone();
            for (int i = 0; i < n; i++) {
                x[i] = tx[p[i]];
                y[i] = ty[p[i]];
            }
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                long x1 = x[(i - 1 + n) % n] - x[i];
                long y1 = y[(i - 1 + n) % n] - y[i];
                long x2 = x[(i + 1) % n] - x[i];
                long y2 = y[(i + 1) % n] - y[i];
                ok &= x1 * y2 - x2 * y1 != 0 || x1 * x2 + y1 * y2 <= 0;
            }
            for (int i2 = 0, i1 = p.length - 1; i2 < p.length; i1 = i2++)
                for (int j2 = 0, j1 = p.length - 1; j2 < p.length; j1 = j2++)
                    ok &= i1 == j1 || i1 == j2 || i2 == j1
                        || !isCrossOrTouchIntersect(x[i1], y[i1], x[i2], y[i2], x[j1], y[j1], x[j2], y[j2]);
            if (ok)
                return new int[][] {x, y};
        }
    }

    // http://en.wikipedia.org/wiki/2-opt
    static void reverse(int[] p, int i, int j) {
        int n = p.length;
        // reverse order from i to j
        while (i != j) {
            int t = p[j];
            p[j] = p[i];
            p[i] = t;
            i = (i + 1) % n;
            if (i == j)
                break;
            j = (j - 1 + n) % n;
        }
    }

    static double len(int[] x, int[] y, int[] p) {
        double res = 0;
        for (int i = 0, j = p.length - 1; i < p.length; j = i++) {
            double dx = x[p[i]] - x[p[j]];
            double dy = y[p[i]] - y[p[j]];
            res += Math.sqrt(dx * dx + dy * dy);
        }
        return res;
    }

    static boolean isCrossOrTouchIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
        if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
            || Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
            return false;
        long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
        long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
        return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
    }
}
