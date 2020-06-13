package geometry;

import java.awt.geom.Point2D;
import java.util.*;

public class PolygonCircleIntersection {
    static final double eps = 1e-9;

    public static double polygonCircleIntersection(int[] x, int[] y, int r) {
        double area = 0;
        for (int i = 0, j = x.length - 1; i < x.length; j = i++) {
            double cur = circleTriangleIntersection(r, x[j], y[j], x[i], y[i]);
            area += Math.signum(x[j] * y[i] - x[i] * y[j]) * cur;
        }
        return Math.abs(area);
    }

    static double circleTriangleIntersection(double r, int x1, int y1, int x2, int y2) {
        List<Point2D.Double> points = new ArrayList<>();
        points.add(new Point2D.Double(x1, y1));
        for (Point2D.Double p : circleLineIntersection(r, y2 - y1, x1 - x2, -(x1 * y2 - x2 * y1)))
            if (isMiddle(p.x, p.y, x1, y1, x2, y2))
                points.add(p);
        points.add(new Point2D.Double(x2, y2));
        double area = 0;
        for (int i = 0; i + 1 < points.size(); i++) {
            Point2D.Double p1 = points.get(i);
            Point2D.Double p2 = points.get(i + 1);
            if (p1.x * p1.x + p1.y * p1.y > r * r + eps || p2.x * p2.x + p2.y * p2.y > r * r + eps)
                area += angleBetween(p1.x, p1.y, p2.x, p2.y) / 2 * r * r;
            else
                area += Math.abs(p1.x * p2.y - p2.x * p1.y) / 2;
        }
        return area;
    }

    static double angleBetween(double ax, double ay, double bx, double by) {
        double a = Math.abs(Math.atan2(by, bx) - Math.atan2(ay, ax));
        return Math.min(a, 2 * Math.PI - a);
    }

    static Point2D.Double[] circleLineIntersection(double r, double a, double b, double c) {
        double aabb = a * a + b * b;
        double d = c * c / aabb - r * r;
        if (d > eps)
            return new Point2D.Double[0];
        double x0 = -a * c / aabb;
        double y0 = -b * c / aabb;
        if (d > -eps)
            return new Point2D.Double[] {new Point2D.Double(x0, y0)};
        d /= -aabb;
        double k = Math.sqrt(d < 0 ? 0 : d);
        return new Point2D.Double[] {
            new Point2D.Double(x0 + k * b, y0 - k * a), new Point2D.Double(x0 - k * b, y0 + k * a)};
    }

    static boolean isMiddle(double x, double y, double x1, double y1, double x2, double y2) {
        return (x < x1 + eps || x < x2 + eps) && (x > x1 - eps || x > x2 - eps) && (y < y1 + eps || y < y2 + eps)
            && (y > y1 - eps || y > y2 - eps);
    }
}
