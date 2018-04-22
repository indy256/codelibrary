package geometry;

import java.util.*;

public class ConvexHull {

    public static Point[] convexHull(Point[] points) {
        Arrays.sort(points, Comparator.<Point>comparingInt(p -> p.x).thenComparingInt(p -> p.y));
        int n = points.length;
        Point[] hull = new Point[n + 1];
        int cnt = 0;
        for (int i = 0; i < 2 * n; i++) {
            int j = i < n ? i : 2 * n - 1 - i;
            while (cnt >= 2 && isNotRightTurn(hull[cnt - 2], hull[cnt - 1], points[j]))
                --cnt;
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
    }

    public static Point[] convexHull2(Point[] p) {
        int n = p.length;
        if (n <= 1)
            return p;
        Arrays.sort(p, Comparator.<Point>comparingInt(point -> point.x).thenComparingInt(point -> point.y));
        Point[] h = new Point[n * 2];
        int cnt = 0;
        for (int i = 0; i < n; h[cnt++] = p[i++])
            while (cnt > 1 && cross(h[cnt - 2], h[cnt - 1], p[i]) >= 0)
                --cnt;
        for (int i = n - 2, t = cnt; i >= 0; h[cnt++] = p[i--])
            while (cnt > t && cross(h[cnt - 2], h[cnt - 1], p[i]) >= 0)
                --cnt;
        return Arrays.copyOf(h, cnt - 1 - (h[0].x == h[1].x && h[0].y == h[1].y ? 1 : 0));
    }

    static long cross(Point a, Point b, Point c) {
        return (long) (b.x - a.x) * (c.y - a.y) - (long) (b.y - a.y) * (c.x - a.x);
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int n = rnd.nextInt(10) + 1;
            Point[] points = new Point[n];
            for (int i = 0; i < n; i++) {
                int range = 10;
                points[i] = new Point(rnd.nextInt(range) - range / 2, rnd.nextInt(range) - range / 2);
            }
            Point[] convexHull = convexHull(points);
            Point[] convexHull2 = convexHull2(points);
            for (int i = 0; i < Math.max(convexHull.length, convexHull2.length); i++)
                if (convexHull[i].x != convexHull2[i].x || convexHull[i].y != convexHull2[i].y)
                    throw new RuntimeException();
            for (int i = 0; i <= convexHull.length; i++) {
                final Point[] hull;
                if (i == 0) {
                    hull = convexHull;
                } else {
                    List<Point> list = new ArrayList<>();
                    Collections.addAll(list, convexHull);
                    list.remove(i - 1);
                    hull = list.toArray(new Point[list.size()]);
                }
                boolean exterior = false;
                for (Point point : points)
                    exterior |= pointInPolygon(point.x, point.y, hull) == -1;
                if (exterior != (i > 0))
                    throw new RuntimeException();
            }
        }
    }

    static int pointInPolygon(int qx, int qy, Point[] points) {
        int n = points.length;
        int cnt = 0;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (points[i].y == qy && (points[i].x == qx || points[j].y == qy && (points[i].x <= qx || points[j].x <= qx) && (points[i].x >= qx || points[j].x >= qx)))
                return 0; // boundary
            if ((points[i].y > qy) != (points[j].y > qy)) {
                long det = (long) (points[i].x - qx) * (points[j].y - qy) - (long) (points[j].x - qx) * (points[i].y - qy);
                if (det == 0)
                    return 0; // boundary
                if ((det > 0) != (points[j].y - points[i].y > 0))
                    ++cnt;
            }
        }
        return cnt % 2 == 0 ? -1 /* exterior */ : 1 /* interior */;
    }
}
