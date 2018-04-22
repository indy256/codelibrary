package geometry;

import java.awt.geom.*;
import java.util.*;

public class AngleAreaOrientationSortRotationPerpendicular {

    // pay attention to case ax==0 && ay==0 or bx==0 && by == 0
    public static double angleBetween(long ax, long ay, long bx, long by) {
        double a = Math.atan2(ax * by - ay * bx, ax * bx + ay * by);
        return a < 0 ? a + 2 * Math.PI : a;
    }

    // pay attention to case ax==0 && ay==0 or bx==0 && by == 0
    public static double angleBetween2(long ax, long ay, long bx, long by) {
        double a = Math.atan2(by, bx) - Math.atan2(ay, ax);
        return a < 0 ? a + 2 * Math.PI : a;
    }

    public static long doubleSignedArea(int[] x, int[] y) {
        int n = x.length;
        long area = 0;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            area += (long) (x[i] - x[j]) * (y[i] + y[j]);
//			area += (long) x[i] * y[j] - (long) x[j] * y[i];
        }
        return area;
    }

    // Returns -1 for clockwise, 0 for straight line, 1 for counterclockwise orientation
    public static int orientation(long ax, long ay, long bx, long by, long cx, long cy) {
        bx -= ax;
        by -= ay;
        cx -= ax;
        cy -= ay;
        return Long.signum(bx * cy - by * cx);
    }

    public static boolean isMiddle(long a, long m, long b) {
        return Math.min(a, b) <= m && m <= Math.max(a, b);
    }

    public static boolean isMiddle(long ax, long ay, long mx, long my, long bx, long by) {
        return orientation(ax, ay, mx, my, bx, by) == 0 && isMiddle(ax, mx, bx) && isMiddle(ay, my, by);
    }

    public static class Point implements Comparable<Point> {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            boolean up1 = y > 0 || (y == 0 && x >= 0);
            boolean up2 = o.y > 0 || (o.y == 0 && o.x >= 0);
            if (up1 != up2) return up1 ? -1 : 1;
            int cmp = Long.signum((long) o.x * y - (long) o.y * x);
            if (cmp != 0) return cmp;
            return Long.compare((long) x * x + (long) y * y, (long) o.x * o.x + (long) o.y * o.y);
            //return Double.compare(Math.atan2(y, x), Math.atan2(o.y, o.x));
        }

        @Override
        public String toString() {
            return "(" + x + ',' + y + ')';
        }
    }

    public Point2D.Double rotateCCW(Point2D.Double p, double angle) {
        return new Point2D.Double(p.x * Math.cos(angle) - p.y * Math.sin(angle), p.x * Math.sin(angle) + p.y * Math.cos(angle));
    }

    public Line perpendicular(Line line, long x, long y) {
        return new Line(-line.b, line.a, line.b * x - line.a * y);
    }

    public static class Line {
        long a, b, c;

        public Line(long a, long b, long c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        int range = 100;
        for (int step = 0; step < 100_000; step++) {
            long ax = rnd.nextInt(range) - range / 2;
            long ay = rnd.nextInt(range) - range / 2;
            long bx = rnd.nextInt(range) - range / 2;
            long by = rnd.nextInt(range) - range / 2;
            long cx = rnd.nextInt(range) - range / 2;
            long cy = rnd.nextInt(range) - range / 2;
            int orientation1 = orientation(ax, ay, bx, by, cx, cy);
            int orientation2 = -Line2D.relativeCCW(ax, ay, bx, by, cx, cy);
            if (orientation1 == 0)
                continue;
            if (orientation1 != orientation2)
                throw new RuntimeException();
            if (ax == 0 && ay == 0 || bx == 0 && by == 0)
                continue;
            double res1 = angleBetween(ax, ay, bx, by);
            double res2 = angleBetween2(ax, ay, bx, by);
            if (!(Math.abs(res1 - res2) < 1e-9))
                throw new RuntimeException();
        }
        Point[] points = new Point[]{new Point(1, 1), new Point(1, -1), new Point(0, 0)};
        Arrays.sort(points);
        System.out.println(Arrays.toString(points));
    }
}
