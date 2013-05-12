import java.util.*;

public class LineAlgo {
	static final double eps = 1e-8;

	public static int sign(double a) {
		return Math.abs(a) < eps ? 0 : a < 0 ? -1 : 1;
	}

	public static class Point implements Comparable<Point> {
		public final double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public Point sub(Point b) {
			return new Point(x - b.x, y - b.y);
		}

		public double cross(Point b) {
			return x * b.y - y * b.x;
		}

		public double dot(Point b) {
			return x * b.x + y * b.y;
		}

		public Point rotate(double angle) { // counterclockwise
			return new Point(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
		}

		public int compareTo(Point o) {
			return Double.compare(x, o.x) != 0 ? Double.compare(x, o.x) : Double.compare(y, o.y);
		}
	}

	public static class Line {
		public final double a, b, c;

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

		public Point intersect(Line other) {
			double a1 = a;
			double b1 = b;
			double c1 = c;
			double a2 = other.a;
			double b2 = other.b;
			double c2 = other.c;
			double det = a1 * b2 - a2 * b1;
			if (sign(det) == 0) { // parallel or coincide
				return null;
			}
			double x = -(c1 * b2 - c2 * b1) / det;
			double y = -(a1 * c2 - a2 * c1) / det;
			return new Point(x, y);
		}
	}

	public static double sqr(double x) {
		return x * x;
	}

	public static double quickHypot(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static double det(double a, double b, double c, double d) {
		return a * d - b * c;
	}

	public static double angle(Point a, Point b) {
		return Math.atan2(a.cross(b), a.dot(b));
	}

	public static double angle(Line line) {
		return Math.atan2(-line.a, line.b);
	}

	public static int crossop(Point a, Point b, Point c) {
		return sign(b.sub(a).cross(c.sub(a)));
	}

	public static boolean cw(Point a, Point b, Point c) {
		return crossop(a, b, c) < 0;
	}

	public static boolean ccw(Point a, Point b, Point c) {
		return crossop(a, b, c) > 0;
	}

	public static boolean isMiddle(double a, double m, double b) {
		return Math.abs(m - a) < eps || Math.abs(m - b) < eps || (a < m) != (b < m);
	}

	public static boolean isMiddle(Point a, Point m, Point b) {
		return crossop(a, m, b) == 0 && isMiddle(a.x, m.x, b.x) && isMiddle(a.y, m.y, b.y);
	}

	public static boolean isTouchIntersect(Point a, Point b, Point c, Point d) {
		return isMiddle(a, c, b) || isMiddle(a, d, b) || isMiddle(c, a, d) || isMiddle(c, b, d);
	}

	public static boolean isCrossIntersect(Point a, Point b, Point c, Point d) {
		return crossop(a, b, c) * crossop(a, b, d) < 0 && crossop(c, d, a) * crossop(c, d, b) < 0;
	}

	public static boolean isIntersect(Point a, Point b, Point c, Point d) {
		return isCrossIntersect(a, b, c, d) || isTouchIntersect(a, b, c, d);
	}

	static boolean isIntersect2(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		if (Math.max(x1, x2) < Math.min(x3, x4) - eps || Math.max(x3, x4) < Math.min(x1, x2) - eps
				|| Math.max(y1, y2) < Math.min(y3, y4) - eps || Math.max(y3, y4) < Math.min(y1, y2) - eps) {
			return false;
		}
		double z1 = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
		double z2 = (x4 - x1) * (y2 - y1) - (y4 - y1) * (x2 - x1);
		if (z1 < -eps && z2 < -eps || z1 > eps && z2 > eps) {
			return false;
		}
		double z3 = (x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3);
		double z4 = (x2 - x3) * (y4 - y3) - (y2 - y3) * (x4 - x3);
		if (z3 < -eps && z4 < -eps || z3 > eps && z4 > eps) {
			return false;
		}
		return true;
	}

	public static double signedArea(double[] x, double[] y) {
		int n = x.length;
		double s = 0;
		for (int i = 0; i < n; i++) {
			s += (x[(i + 1) % n] - x[i]) * (y[(i + 1) % n] + y[i]);
		}
		return s / 2;
	}

	public static double pointToLineDistance(Point p, Line line) {
		return Math.abs(line.a * p.x + line.b * p.y + line.c) / quickHypot(line.a, line.b);
	}

	public static double pointToSegmentDistance(Point p, Point a, Point b) {
		double dx = b.x - a.x;
		double dy = b.y - a.y;
		double px = p.x - a.x;
		double py = p.y - a.y;
		double squaredLength = dx * dx + dy * dy;
		double proj = dx * px + dy * py;
		if (proj < 0 || squaredLength < eps * eps) {
			return quickHypot(px, py);
		}
		if (proj > squaredLength) {
			return quickHypot(px - dx, py - dy);
		}
		return quickHypot(px - (double) proj / squaredLength * dx, py - (double) proj / squaredLength * dy);
	}

	public static double pointToSegmentDistance2(Point p, Point a, Point b) {
		double s12 = sqr(a.x - b.x) + sqr(a.y - b.y);
		double s1 = sqr(p.x - a.x) + sqr(p.y - a.y);
		double s2 = sqr(p.x - b.x) + sqr(p.y - b.y);
		if (s12 > eps * eps && Math.abs(s1 - s2) <= s12)
			return Math.abs(a.sub(p).cross(b.sub(p))) / Math.sqrt(s12);
		else
			return Math.sqrt(Math.min(s1, s2));
	}

	public static Point[] convexHull(Point[] p) {
		int n = p.length;
		if (n <= 1)
			return p;
		int k = 0;
		Arrays.sort(p);
		Point[] q = new Point[n * 2];
		for (int i = 0; i < n; q[k++] = p[i++])
			for (; k > 1 && !cw(q[k - 2], q[k - 1], p[i]); --k)
				;
		for (int i = n - 2, t = k; i >= 0; q[k++] = p[i--])
			for (; k > t && !cw(q[k - 2], q[k - 1], p[i]); --k)
				;
		return Arrays.copyOf(q, k - 1 - (q[0].compareTo(q[1]) == 0 ? 1 : 0));
	}

	Point[] convexCut(Point[] poly, Point p1, Point p2) {
		int n = poly.length;
		List<Point> res = new ArrayList<>();
		for (int i = 0, j = n - 1; i < n; j = i++) {
			int d1 = sign(p2.sub(p1).cross(poly[i].sub(p1)));
			int d2 = sign(p2.sub(p1).cross(poly[j].sub(p1)));
			if (d1 >= 0)
				res.add(poly[i]);
			if (d1 * d2 < 0)
				res.add(new Line(p1, p2).intersect(new Line(poly[i], poly[j])));
		}
		return res.toArray(new Point[0]);
	}

	public static enum Location {
		BOUNDARY, INTERIOR, EXTERIOR
	}

	public static Location pointInPolygon(Point p0, Point[] poly) {
		int n = poly.length;
		Point[] p = new Point[n];
		for (int i = 0; i < p.length; i++)
			p[i] = poly[i].sub(p0);
		int cnt = 0;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			if (sign(p[i].y) == 0
					&& (sign(p[i].x) == 0 || sign(p[j].y) == 0 && (sign(p[i].x) < 0) != (sign(p[j].x) < 0)))
				return Location.BOUNDARY;

			if (sign(p[i].y) > 0 != sign(p[j].y) > 0) {
				double det = p[i].cross(p[j]);
				if (sign(det) == 0)
					return Location.BOUNDARY;
				if (sign(det) > 0 != sign(p[j].y - p[i].y) > 0)
					++cnt;
			}
		}
		return cnt % 2 == 0 ? Location.EXTERIOR : Location.INTERIOR;
	}

	public static enum Position {
		LEFT, RIGHT, BEHIND, BEYOND, ORIGIN, DESTINATION, BETWEEN
	}

	public static Position classify(Point a, Point b, Point p) {
		int s = crossop(a, b, p);
		if (s > 0) {
			return Position.LEFT;
		}
		if (s < 0) {
			return Position.RIGHT;
		}
		if (sign(p.x - a.x) == 0 && sign(p.y - a.y) == 0) {
			return Position.ORIGIN;
		}
		if (sign(p.x - b.x) == 0 && sign(p.y - b.y) == 0) {
			return Position.DESTINATION;
		}
		// if (ax * bx < 0 || ay * by < 0) {
		if (isMiddle(a, b, p)) {
			return Position.BEYOND;
		}
		// if (ax * ax + ay * ay < bx * bx + by * by) {
		if (isMiddle(b, a, p)) {
			return Position.BEHIND;
		}
		return Position.BETWEEN;
	}

	// Usage example
	public static void main(String[] args) {
		for (int x1 = -4; x1 <= 4; x1++)
			for (int y1 = -4; y1 <= 4; y1++)
				for (int x2 = -4; x2 <= 4; x2++)
					for (int y2 = -4; y2 <= 4; y2++)
						for (int x3 = -4; x3 <= 4; x3++)
							for (int y3 = -4; y3 <= 4; y3++) {
								Point p1 = new Point(x1, y1);
								Point p2 = new Point(x2, y2);
								Point p3 = new Point(x3, y3);
								Position res1 = classify(p1, p2, p3);
								// Position res2 = classify2(p1, p2, p3);
								// if (res1 != res2) {
								// System.out.println(res1 + " " + res2);
								// }
								double d1 = pointToSegmentDistance(p3, p1, p2);
								double d2 = pointToSegmentDistance2(p3, p1, p2);
								if (!(Math.abs(d1 - d2) < 1e-9)) {
									System.out.println(d1 + " " + d2);
								}
								for (int x4 = -4; x4 <= 4; x4++)
									for (int y4 = -4; y4 <= 4; y4++) {
										Point p4 = new Point(x4, y4);
										boolean b1 = isIntersect(p1, p2, p3, p4);
										boolean b2 = isIntersect2(x1, y1, x2, y2, x3, y3, x4, y4);
										if (b1 != b2) {
											System.out.println(b1 + " " + b2);
										}
									}
							}
	}
}
