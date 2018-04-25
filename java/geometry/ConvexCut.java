package geometry;

import java.util.*;

public class ConvexCut {

    public static class Point {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    // cuts right part of convex polygon (returns left part)
    public static Point[] convexCut(Point[] poly, Point p1, Point p2) {
        int n = poly.length;
        List<Point> res = new ArrayList<>();
        for (int i = 0, j = n - 1; i < n; j = i++) {
            int d1 = orientation(p1.x, p1.y, p2.x, p2.y, poly[j].x, poly[j].y);
            int d2 = orientation(p1.x, p1.y, p2.x, p2.y, poly[i].x, poly[i].y);
            if (d1 >= 0)
                res.add(poly[j]);
            if (d1 * d2 < 0)
                res.add(getLinesIntersection(p1.x, p1.y, p2.x, p2.y, poly[j].x, poly[j].y, poly[i].x, poly[i].y));
        }
        return res.toArray(new Point[res.size()]);
    }

    static int orientation(double ax, double ay, double bx, double by, double cx, double cy) {
        bx -= ax;
        by -= ay;
        cx -= ax;
        cy -= ay;
        double cross = bx * cy - by * cx;
        double EPS = 1e-9;
        return cross < -EPS ? -1 : cross > EPS ? 1 : 0;
    }

    static Point getLinesIntersection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double a1 = y2 - y1;
        double b1 = x1 - x2;
        double c1 = -(x1 * y2 - x2 * y1);
        double a2 = y4 - y3;
        double b2 = x3 - x4;
        double c2 = -(x3 * y4 - x4 * y3);
        double det = a1 * b2 - a2 * b1;
        double x = -(c1 * b2 - c2 * b1) / det;
        double y = -(a1 * c2 - a2 * c1) / det;
        return new Point(x, y);
    }

    public static void main(String[] args) {
    }
}
