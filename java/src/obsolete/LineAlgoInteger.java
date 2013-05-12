package obsolete;

import java.awt.geom.Point2D;

public class LineAlgoInteger {
	public static long sqr(long x) {
		return x * x;
	}

	static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	public static double quickHypot(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static class Point {
		long x, y;

		public Point(long x, long y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Line {
		long a, b, c;

		void norm() {
			long g = gcd(c, gcd(a, b));
			a /= g;
			b /= g;
			c /= g;
			if (a < 0 || a == 0 && b < 0) {
				a = -a;
				b = -b;
				c = -c;
			}
		}

		public Line(long a, long b, long c) {
			this.a = a;
			this.b = b;
			this.c = c;
			norm();
		}

		public Line(Point p1, Point p2) {
			a = +(p1.y - p2.y);
			b = -(p1.x - p2.x);
			c = p1.x * p2.y - p2.x * p1.y;
			norm();
		}

		public boolean parallel(Line line) {
			return det(a, b, line.a, line.b) == 0;
		}

		public boolean equivalent(Line line) {
			return det(a, b, line.a, line.b) == 0 && det(a, c, line.a, line.c) == 0 && det(b, c, line.b, line.c) == 0;
		}

		public double angle() {
			return Math.atan2(-a, b);
		}
	}

	public static long det(long a, long b, long c, long d) {
		return a * d - b * c;
	}

	public static long dot(Point a, Point b) {
		return a.x * b.x + a.y * b.y;
	}

	public static long cross(long ax, long ay, long bx, long by, long cx, long cy) {
		return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
	}

	public static long cross(Point a, Point b, Point c) {
		return cross(a.x, a.y, b.x, b.y, c.x, c.y);
	}

	public static int crossop(Point a, Point b, Point c) {
		long x = cross(a, b, c);
		if (x == 0)
			return 0;
		return x < 0 ? -1 : 1;
	}

	public static boolean cw(Point a, Point b, Point c) {
		return cross(a, b, c) < 0;
	}

	public static boolean ccw(Point a, Point b, Point c) {
		return cross(a, b, c) > 0;
	}

	public static boolean isMiddle(long a, long m, long b) {
		return m == a || m == b || (a < m) != (b < m);
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

	static boolean isIntersect2(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
		if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
				|| Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2)) {
			return false;
		}
		long z1 = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
		long z2 = (x4 - x1) * (y2 - y1) - (y4 - y1) * (x2 - x1);
		if (z1 < 0 && z2 < 0 || z1 > 0 && z2 > 0) {
			return false;
		}
		long z3 = (x1 - x3) * (y4 - y3) - (y1 - y3) * (x4 - x3);
		long z4 = (x2 - x3) * (y4 - y3) - (y2 - y3) * (x4 - x3);
		if (z3 < 0 && z4 < 0 || z3 > 0 && z4 > 0) {
			return false;
		}
		return true;
	}

	public static long doubleSignedArea(long[] x, long[] y) {
		int n = x.length;
		long s = 0;
		for (int i = 0; i < n; i++) {
			s += (x[(i + 1) % n] - x[i]) * (y[(i + 1) % n] + y[i]);
		}
		return s;
	}

	public static Point2D.Double getIntersection(Line A, Line B) {
		long a1 = A.a;
		long b1 = A.b;
		long c1 = A.c;
		long a2 = B.a;
		long b2 = B.b;
		long c2 = B.c;
		long det = a1 * b2 - a2 * b1;
		if (det == 0) {
			return null;
		}
		double x = -(c1 * b2 - c2 * b1) / (double) det;
		double y = -(a1 * c2 - a2 * c1) / (double) det;
		return new Point2D.Double(x, y);
	}

	public static double pointToLineDistance(Point p, Line line) {
		return Math.abs(line.a * p.x + line.b * p.y + line.c) / quickHypot(line.a, line.b);
	}

	public static double pointToSegmentDistance(Point p, Point a, Point b) {
		long dx = b.x - a.x;
		long dy = b.y - a.y;
		long px = p.x - a.x;
		long py = p.y - a.y;
		long squaredLength = dx * dx + dy * dy;
		long proj = dx * px + dy * py;
		if (proj < 0 || squaredLength == 0) {
			return quickHypot(px, py);
		}
		if (proj > squaredLength) {
			return quickHypot(px - dx, py - dy);
		}
		return quickHypot(px - (double) proj / squaredLength * dx, py - (double) proj / squaredLength * dy);
	}

	public static double pointToSegmentDistance2(Point p, Point a, Point b) {
		long s12 = sqr(a.x - b.x) + sqr(a.y - b.y);
		long s1 = sqr(p.x - a.x) + sqr(p.y - a.y);
		long s2 = sqr(p.x - b.x) + sqr(p.y - b.y);
		if (s12 != 0 && Math.abs(s1 - s2) <= s12)
			return Math.abs(cross(p, a, b)) / Math.sqrt(s12);
		else
			return Math.sqrt(Math.min(s1, s2));
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
		if (p.x == a.x && p.y == a.y) {
			return Position.ORIGIN;
		}
		if (p.x == b.x && p.y == b.y) {
			return Position.DESTINATION;
		}
		if (isMiddle(a, b, p)) {
			return Position.BEYOND;
		}
		if (isMiddle(b, a, p)) {
			return Position.BEHIND;
		}
		return Position.BETWEEN;
	}

	static Position classify2(Point a, Point b, Point p) {
		long ax = b.x - a.x;
		long ay = b.y - a.y;
		long bx = p.x - a.x;
		long by = p.y - a.y;
		long s = ax * by - ay * bx;
		if (s > 0) {
			return Position.LEFT;
		}
		if (s < 0) {
			return Position.RIGHT;
		}
		if (p.x == a.x && p.y == a.y) {
			return Position.ORIGIN;
		}
		if (p.x == b.x && p.y == b.y) {
			return Position.DESTINATION;
		}
		if (ax * bx < 0 || ay * by < 0) {
			return Position.BEHIND;
		}
		if (ax * ax + ay * ay < bx * bx + by * by) {
			return Position.BEYOND;
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
								Position res2 = classify2(p1, p2, p3);
								if (res1 != res2) {
									System.out.println(res1 + " " + res2);
								}
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
