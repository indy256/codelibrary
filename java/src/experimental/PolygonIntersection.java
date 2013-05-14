package experimental;

import java.util.*;

public class PolygonIntersection {
	static final double eps = 1e-9;

	public static int cmp(double a, double b) {
		return Math.abs(a - b) < eps ? 0 : a < b ? -1 : 1;
	}

	static class Point {
		double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Object o) {
			return cmp(x - ((Point) o).x, 0) == 0 && cmp(y - ((Point) o).y, 0) == 0;
		}
	}

	public static class Line {
		double a, b, c;

		public Line(Point p1, Point p2) {
			a = +(p1.y - p2.y);
			b = -(p1.x - p2.x);
			c = p1.x * p2.y - p2.x * p1.y;
		}
	}

	public static double cross(double ax, double ay, double bx, double by, double cx, double cy) {
		return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
	}

	public static double cross(Point a, Point b, Point c) {
		return cross(a.x, a.y, b.x, b.y, c.x, c.y);
	}

	public static int crossop(Point a, Point b, Point c) {
		return cmp(cross(a, b, c), 0);
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

	public static Point getIntersection(Line A, Line B) {
		double a1 = A.a;
		double b1 = A.b;
		double c1 = A.c;
		double a2 = B.a;
		double b2 = B.b;
		double c2 = B.c;
		double det = a1 * b2 - a2 * b1;
		if (cmp(det, 0) == 0) {
			return null;
		}
		double x = -(c1 * b2 - c2 * b1) / det;
		double y = -(a1 * c2 - a2 * c1) / det;
		return new Point(x, y);
	}

	static boolean pointInPolygon(Point q, Point[] p) {
		double a = 0;
		for (int i = 0, j = p.length - 1; i < p.length; j = i++) {
			if (isMiddle(p[i], q, p[j]))
				return true;
			a += angle(p[i], q, p[j]);
		}
		return Math.abs(a) > eps;
	}

	static double angle(Point a, Point p, Point b) {
		double x1 = a.x - p.x;
		double y1 = a.y - p.y;
		double x2 = b.x - p.x;
		double y2 = b.y - p.y;
		return Math.atan2(x1 * y2 - y1 * x2, x1 * x2 + y1 * y2);
	}

	Point[] poly_intersect(Point[] P, Point[] Q) {
		int n = P.length;
		int m = Q.length;
		int a = 0, b = 0, aa = 0, ba = 0, inflag = 0;
		List<Point> R = new ArrayList<>();
		while ((aa < n || ba < m) && aa < 2 * n && ba < 2 * m) {
			Point p1 = P[a], p2 = P[(a + 1) % n], q1 = Q[b], q2 = Q[(b + 1) % m];
			Point A = new Point(p2.x - p1.x, p2.y - p1.y);
			Point B = new Point(q2.x - q1.x, q2.y - q1.y);
			int cross = cmp(A.x * B.y - A.y * B.x, 0);
			int ha = crossop(p1, p2, q2);
			int hb = crossop(q1, q2, p2);
			if (cross == 0 && cross(q1, p1, p2) == 0 && cmp(A.x * B.x + A.y * B.y, 0) < 0) {
				if (isMiddle(p1, q1, p2))
					R.add(q1);
				if (isMiddle(p1, q2, p2))
					R.add(q2);
				if (isMiddle(q1, p1, q2))
					R.add(p1);
				if (isMiddle(q1, p2, q2))
					R.add(p2);
				if (R.size() < 2)
					return new Point[0];
				inflag = 1;
				break;
			} else if (cross != 0 && isIntersect(p1, p2, q1, q2)) {
				if (inflag == 0)
					aa = ba = 0;
				R.add(getIntersection(new Line(p1, p2), new Line(q1, q2)));
				inflag = (hb > 0) ? 1 : -1;
			}
			if (cross == 0 && hb < 0 && ha < 0)
				return R.toArray(new Point[0]);
			boolean t = cross == 0 && hb == 0 && ha == 0;
			if (t ? (inflag == 1) : (cross >= 0) ? (ha <= 0) : (hb > 0)) {
				if (inflag == -1)
					R.add(q2);
				ba++;
				b++;
				b %= m;
			} else {
				if (inflag == 1)
					R.add(p2);
				aa++;
				a++;
				a %= n;
			}
		}
		if (inflag == 0) {
			if (pointInPolygon(P[0], Q))
				return P;
			if (pointInPolygon(Q[0], P))
				return Q;
		}
		List<Point> U = new ArrayList<>();
		for (int i = 0; i < R.size(); i++)
			if (i == 0 || !R.get(i).equals(R.get(i - 1)))
				U.add(R.get(i));
		if (U.size() > 1 && U.get(0).equals(U.get(U.size() - 1)))
			U.remove(U.size() - 1);
		return U.toArray(new Point[0]);
	}

	public static void main(String[] args) {

	}

}
