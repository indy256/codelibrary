package geometry;

import java.util.*;

public class CircleOperations {

    static final double EPS = 1e-10;

    public static double fastHypot(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public static class Point {
        double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Circle {
        double x, y, r;

        public Circle(double x, double y, double r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        public boolean contains(Point p) {
            return fastHypot(p.x - x, p.y - y) < r + EPS;
        }
    }

    public static class Line {
        double a, b, c;

        public Line(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public Line(Point p1, Point p2) {
            a = +(p1.y - p2.y);
            b = -(p1.x - p2.x);
            c = p1.x * p2.y - p2.x * p1.y;
        }
    }

    // geometric solution
    public static Point[] circleLineIntersection(Circle circle, Line line) {
        double a = line.a;
        double b = line.b;
        double c = line.c + circle.x * a + circle.y * b;
        double r = circle.r;
        double aabb = a * a + b * b;
        double d = c * c / aabb - r * r;
        if (d > EPS)
            return new Point[0];
        double x0 = -a * c / aabb;
        double y0 = -b * c / aabb;
        if (d > -EPS)
            return new Point[]{new Point(x0 + circle.x, y0 + circle.y)};
        d /= -aabb;
        double k = Math.sqrt(d < 0 ? 0 : d);
        return new Point[]{
                new Point(x0 + k * b + circle.x, y0 - k * a + circle.y),
                new Point(x0 - k * b + circle.x, y0 + k * a + circle.y)};
    }

    // algebraic solution
    public static Point[] circleLineIntersection2(Circle circle, Line line) {
        return Math.abs(line.a) >= Math.abs(line.b)
                ? intersection(line.a, line.b, line.c, circle.x, circle.y, circle.r, false)
                : intersection(line.b, line.a, line.c, circle.y, circle.x, circle.r, true);
    }

    static Point[] intersection(double a, double b, double c, double CX, double CY, double R, boolean swap) {
        // ax+by+c=0
        // (by+c+aCX)^2+(ay-aCY)^2=(aR)^2
        double A = a * a + b * b;
        double B = 2.0 * b * (c + a * CX) - 2.0 * a * a * CY;
        double C = (c + a * CX) * (c + a * CX) + a * a * (CY * CY - R * R);
        double d = B * B - 4 * A * C;
        if (d < -EPS)
            return new Point[0];
        d = Math.sqrt(d < 0 ? 0 : d);
        double y1 = (-B + d) / (2 * A);
        double x1 = (-c - b * y1) / a;
        double y2 = (-B - d) / (2 * A);
        double x2 = (-c - b * y2) / a;
        return swap ? d > EPS ? new Point[]{new Point(y1, x1), new Point(y2, x2)} : new Point[]{new Point(y1, x1)}
                : d > EPS ? new Point[]{new Point(x1, y1), new Point(x2, y2)} : new Point[]{new Point(x1, y1)};
    }

    public static Point[] circleCircleIntersection(Circle c1, Circle c2) {
        if (fastHypot(c1.x - c2.x, c1.y - c2.y) < EPS) {
            if (Math.abs(c1.r - c2.r) < EPS)
                return null; // infinity intersection points
            return new Point[0];
        }
        double dx = c2.x - c1.x;
        double dy = c2.y - c1.y;
        double A = -2 * dx;
        double B = -2 * dy;
        double C = dx * dx + dy * dy + c1.r * c1.r - c2.r * c2.r;
        Point[] res = circleLineIntersection(new Circle(0, 0, c1.r), new Line(A, B, C));
        for (Point point : res) {
            point.x += c1.x;
            point.y += c1.y;
        }
        return res;
    }

    public static double circleCircleIntersectionArea(Circle c1, Circle c2) {
        double r = Math.min(c1.r, c2.r);
        double R = Math.max(c1.r, c2.r);
        double d = fastHypot(c1.x - c2.x, c1.y - c2.y);
        if (d < R - r + EPS)
            return Math.PI * r * r;
        if (d > R + r - EPS)
            return 0;
        double area = r * r * Math.acos((d * d + r * r - R * R) / 2 / d / r) + R * R
                * Math.acos((d * d + R * R - r * r) / 2 / d / R) - 0.5
                * Math.sqrt((-d + r + R) * (d + r - R) * (d - r + R) * (d + r + R));
        return area;
    }

    public static Line[] tangents(Circle a, Circle b) {
        List<Line> lines = new ArrayList<>();
        for (int i = -1; i <= 1; i += 2)
            for (int j = -1; j <= 1; j += 2)
                tangents(new Point(b.x - a.x, b.y - a.y), a.r * i, b.r * j, lines);
        for (Line line : lines)
            line.c -= line.a * a.x + line.b * a.y;
        return lines.toArray(new Line[lines.size()]);
    }

    static void tangents(Point center2, double r1, double r2, List<Line> lines) {
        double r = r2 - r1;
        double z = center2.x * center2.x + center2.y * center2.y;
        double d = z - r * r;
        if (d < -EPS) return;
        d = Math.sqrt(d < 0 ? 0 : d);
        lines.add(new Line((center2.x * r + center2.y * d) / z, (center2.y * r - center2.x * d) / z, r1));
    }

    // min enclosing circle in O(n) on average
    public static Circle minEnclosingCircle(Point[] points) {
        if (points.length == 0)
            return new Circle(0, 0, 0);
        if (points.length == 1)
            return new Circle(points[0].x, points[0].y, 0);
        Collections.shuffle(Arrays.asList(points));
        Circle circle = getCircumCircle(points[0], points[1]);
        for (int i = 2; i < points.length; i++) {
            if (!circle.contains(points[i])) {
                circle = getCircumCircle(points[0], points[i]);
                for (int j = 1; j < i; j++) {
                    if (!circle.contains(points[j])) {
                        circle = getCircumCircle(points[j], points[i]);
                        for (int k = 0; k < j; k++) {
                            if (!circle.contains(points[k])) {
                                circle = getCircumCircle(points[i], points[j], points[k]);
                            }
                        }
                    }
                }
            }
        }
        return circle;
    }

    public static Circle getCircumCircle(Point a, Point b) {
        double x = (a.x + b.x) / 2.;
        double y = (a.y + b.y) / 2.;
        double r = fastHypot(a.x - x, a.y - y);
        return new Circle(x, y, r);
    }

    public static Circle getCircumCircle(Point a, Point b, Point c) {
        double Bx = b.x - a.x;
        double By = b.y - a.y;
        double Cx = c.x - a.x;
        double Cy = c.y - a.y;
        double d = 2 * (Bx * Cy - By * Cx);
        if (Math.abs(d) < EPS)
            return getCircumCircle(new Point(Math.min(a.x, Math.min(b.x, c.x)), Math.min(a.y, Math.min(b.y, c.y))),
                    new Point(Math.max(a.x, Math.max(b.x, c.x)), Math.max(a.y, Math.max(b.y, c.y))));
        double z1 = Bx * Bx + By * By;
        double z2 = Cx * Cx + Cy * Cy;
        double cx = Cy * z1 - By * z2;
        double cy = Bx * z2 - Cx * z1;
        double x = cx / d;
        double y = cy / d;
        double r = fastHypot(x, y);
        return new Circle(x + a.x, y + a.y, r);
    }

    // Usage example
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int range = 10;
            int x = rnd.nextInt(range) - range / 2;
            int y = rnd.nextInt(range) - range / 2;
            int r = rnd.nextInt(range);

            int x1 = rnd.nextInt(range) - range / 2;
            int y1 = rnd.nextInt(range) - range / 2;
            int x2 = rnd.nextInt(range) - range / 2;
            int y2 = rnd.nextInt(range) - range / 2;
            if (x1 == x2 && y1 == y2)
                continue;

            Point[] p1 = circleLineIntersection(new Circle(x, y, r), new Line(new Point(x1, y1), new Point(x2, y2)));
            Point[] p2 = circleLineIntersection2(new Circle(x, y, r), new Line(new Point(x1, y1), new Point(x2, y2)));

            if (p1.length != p2.length || p1.length == 1 && !eq(p1[0], p2[0])
                    || p1.length == 2 && !(eq(p1[0], p2[0]) && eq(p1[1], p2[1]) || eq(p1[0], p2[1]) && eq(p1[1], p2[0])))
                throw new RuntimeException();
        }
    }

    static boolean eq(Point p1, Point p2) {
        return !(fastHypot(p1.x - p2.x, p1.y - p2.y) > 1e-9);
    }
}
