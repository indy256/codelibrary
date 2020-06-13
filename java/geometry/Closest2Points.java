package geometry;

import java.util.*;

public class Closest2Points {
    public static class Point {
        final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Find closest pair in O(n*log^2(n))
    public static Point[] findClosestPair(Point[] points) {
        Point[] result = new Point[2];
        Arrays.sort(points, Comparator.comparingInt(p -> p.x));
        rec(points, 0, points.length - 1, result, Long.MAX_VALUE);
        return result;
    }

    static long rec(Point[] points, int l, int r, Point[] result, long mindist2) {
        if (l == r)
            return Long.MAX_VALUE;
        int mid = (l + r) >> 1;
        int midx = points[mid].x;
        long d1 = rec(points, l, mid, result, mindist2);
        mindist2 = Math.min(mindist2, d1);
        long d2 = rec(points, mid + 1, r, result, mindist2);
        mindist2 = Math.min(mindist2, d2);
        Arrays.sort(points, l, r + 1, Comparator.comparingInt(p -> p.y));
        int[] t = new int[r - l + 1];
        int size = 0;
        for (int i = l; i <= r; i++)
            if ((long) (points[i].x - midx) * (points[i].x - midx) < mindist2)
                t[size++] = i;
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                Point a = points[t[i]];
                Point b = points[t[j]];
                if ((long) (b.y - a.y) * (b.y - a.y) >= mindist2)
                    break;
                long dist2 = dist2(a, b);
                if (mindist2 > dist2) {
                    mindist2 = dist2;
                    result[0] = a;
                    result[1] = b;
                }
            }
        }
        return mindist2;
    }

    static long dist2(Point a, Point b) {
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
            long res1 = dist2(p[0], p[1]);
            long res2 = slowClosestPair(points);
            if (res1 != res2)
                throw new RuntimeException(res1 + " " + res2);
        }
    }

    static long slowClosestPair(Point[] points) {
        long res = Long.MAX_VALUE;
        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++) res = Math.min(res, dist2(points[i], points[j]));
        return res;
    }
}
