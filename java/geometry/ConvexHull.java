package geometry;

import java.util.*;

public class ConvexHull {
    public static Point[] convexHull(Point[] points) {
        Arrays.sort(points, Comparator.<Point>comparingInt(p -> p.x).thenComparingInt(p -> p.y));
        int n = points.length;
        Point[] hull = new Point[n + 1];
        int cnt = 0;
        for (int i = 0; i < 2 * n - 1; i++) {
            int j = i < n ? i : 2 * n - 2 - i;
            while (cnt >= 2 && isNotRightTurn(hull[cnt - 2], hull[cnt - 1], points[j])) --cnt;
            hull[cnt++] = points[j];
        }
        return Arrays.copyOf(hull, cnt - 1);
    }

    static boolean isNotRightTurn(Point a, Point b, Point c) {
        long cross = (long) (a.x - b.x) * (c.y - b.y) - (long) (a.y - b.y) * (c.x - b.x);
        long dot = (long) (a.x - b.x) * (c.x - b.x) + (long) (a.y - b.y) * (c.y - b.y);
        return cross < 0 || cross == 0 && dot <= 0;
    }

    public static class Point {
        public final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{"
                + "x=" + x + ", y=" + y + '}';
        }
    }

    // Usage example
    public static void main(String[] args) {
        Point[] points = {new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(0, 0)};
        Point[] convexHull = convexHull(points);
        System.out.println(Arrays.toString(convexHull));
    }
}
