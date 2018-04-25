package geometry;

public class LineGeometry {

    static final double EPS = 1e-10;

    public static int sign(double a) {
        return a < -EPS ? -1 : a > EPS ? 1 : 0;
    }

    public static class Point implements Comparable<Point> {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point minus(Point b) {
            return new Point(x - b.x, y - b.y);
        }

        public double cross(Point b) {
            return x * b.y - y * b.x;
        }

        public double dot(Point b) {
            return x * b.x + y * b.y;
        }

        public Point rotateCCW(double angle) {
            return new Point(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
        }

        @Override
        public int compareTo(Point o) {
            // return Double.compare(Math.atan2(y, x), Math.atan2(o.y, o.x));
            return Double.compare(x, o.x) != 0 ? Double.compare(x, o.x) : Double.compare(y, o.y);
        }
    }

    public static class Line {
        public double a, b, c;

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

        public Point intersect(Line line) {
            double d = a * line.b - line.a * b;
            if (sign(d) == 0) {
                return null;
            }
            double x = -(c * line.b - line.c * b) / d;
            double y = -(a * line.c - line.a * c) / d;
            return new Point(x, y);
        }
    }

    // Returns -1 for clockwise, 0 for straight line, 1 for counterclockwise order
    public static int orientation(Point a, Point b, Point c) {
        Point AB = b.minus(a);
        Point AC = c.minus(a);
        return sign(AB.cross(AC));
    }

    public static boolean cw(Point a, Point b, Point c) {
        return orientation(a, b, c) < 0;
    }

    public static boolean ccw(Point a, Point b, Point c) {
        return orientation(a, b, c) > 0;
    }

    public static boolean isCrossIntersect(Point a, Point b, Point c, Point d) {
        return orientation(a, b, c) * orientation(a, b, d) < 0 && orientation(c, d, a) * orientation(c, d, b) < 0;
    }

    public static boolean isCrossOrTouchIntersect(Point a, Point b, Point c, Point d) {
        if (Math.max(a.x, b.x) < Math.min(c.x, d.x) - EPS || Math.max(c.x, d.x) < Math.min(a.x, b.x) - EPS
                || Math.max(a.y, b.y) < Math.min(c.y, d.y) - EPS || Math.max(c.y, d.y) < Math.min(a.y, b.y) - EPS) {
            return false;
        }
        return orientation(a, b, c) * orientation(a, b, d) <= 0 && orientation(c, d, a) * orientation(c, d, b) <= 0;
    }

    public static double pointToLineDistance(Point p, Line line) {
        return Math.abs(line.a * p.x + line.b * p.y + line.c) / fastHypot(line.a, line.b);
    }

    public static double fastHypot(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public static double angleBetween(Point a, Point b) {
        return Math.atan2(a.cross(b), a.dot(b));
    }

    public static double angle(Line line) {
        return Math.atan2(-line.a, line.b);
    }

    // Usage example
    public static void main(String[] args) {
    }
}
