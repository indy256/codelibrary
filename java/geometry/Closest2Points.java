package geometry;

import java.util.*;

public class Closest2Points {

    public static class Point {
        long x, y;

        public Point(long x, long y) {
            this.x = x;
            this.y = y;
        }
    }

    public static final Comparator<Point> CMP_X = Comparator.<Point>comparingLong(p -> p.x).thenComparingLong(p -> p.y);
    public static final Comparator<Point> CMP_Y = Comparator.comparingLong(p -> p.y);

    public static Point[] findClosestPair(Point[] points) {
        Point[] result = new Point[2];
        Arrays.sort(points, CMP_X);
        rec(points, 0, points.length - 1, result, Long.MAX_VALUE);
        return result;
    }

    static long rec(Point[] points, int l, int r, Point[] result, long mindist) {
        if (l == r)
            return Long.MAX_VALUE;
        int mid = (l + r) >> 1;
        long midx = points[mid].x;
        long d1 = rec(points, l, mid, result, mindist);
        mindist = Math.min(mindist, d1);
        long d2 = rec(points, mid + 1, r, result, mindist);
        mindist = Math.min(mindist, d2);
        Arrays.sort(points, l, r + 1, CMP_Y);
        int[] t = new int[r - l + 1];
        int size = 0;
        for (int i = l; i <= r; i++)
            if (Math.abs(points[i].x - midx) < mindist)
                t[size++] = i;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Point a = points[t[i]];
                Point b = points[t[j]];
                if (b.y - a.y >= mindist)
                    break;
                long dist = dist(a, b);
                if (mindist > dist) {
                    mindist = dist;
                    result[0] = a;
                    result[1] = b;
                }
            }
        }
        return mindist;
    }

    static long dist(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        return dx * dx + dy * dy;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random();
        for (int step = 0; step < 10_000; step++) {
            int n = 100;
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                points[i] = new Point(rnd.nextInt(1000) - 500, rnd.nextInt(1000) - 500);
            }
            Point[] p = findClosestPair(points);
            long res1 = dist(p[0], p[1]);
            long res2 = slowClosestPair(points);
            if (res1 != res2)
                throw new RuntimeException(res1 + " " + res2);
        }
    }

    static long slowClosestPair(Point[] points) {
        long res = Long.MAX_VALUE;
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                res = Math.min(res, dist(points[i], points[j]));
        return res;
    }
}
