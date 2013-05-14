package test;

import java.util.*;

public class PointInPolygon2 {

	static class Point {
		long x, y;

		public Point(long x, long y) {
			this.x = x;
			this.y = y;
		}
	}

	public static enum Location {
		BOUNDARY, INTERIOR, EXTERIOR
	}

	public static Location pointInPolygon(Point[] p, long x, long y) {
		int cnt = 0;
		int n = p.length;
		for (int i = 0; i < n; i++) {
			if (x == p[i].x && y == p[i].y)
				return Location.BOUNDARY;
			int j = (i + 1) % n;
			if (p[i].y == p[j].y) {
				if (y == p[i].y && (p[i].x < x) != (p[j].x < x))
					return Location.BOUNDARY;
				continue;
			}
			long ymin = Math.min(p[i].y, p[j].y);
			long ymax = Math.max(p[i].y, p[j].y);
			if (y == ymax && (p[i].y == ymax && p[i].x > x || p[j].y == ymax && p[j].x > x))
				++cnt;
			if (y > ymin && y < ymax) {
				long cross = (x - p[i].x) * (p[j].y - p[i].y) - (p[j].x - p[i].x) * (y - p[i].y);
				if (cross == 0)
					return Location.BOUNDARY;
				if (p[j].y - p[i].y > 0 != cross > 0)
					++cnt;
			}
		}
		return cnt % 2 == 0 ? Location.EXTERIOR : Location.INTERIOR;
	}

	public static Location pointInPolygon2(Point[] p, long x0, long y0) {
		int n = p.length;
		long[] x = new long[n], y = new long[n];
		for (int i = 0; i < p.length; i++) {
			x[i] = p[i].x - x0;
			y[i] = p[i].y - y0;
		}
		int cnt = 0;
		for (int i = 0; i < n; i++) {
			int j = (i + 1) % n;
			if (x[i] == 0 && y[i] == 0)
				return Location.BOUNDARY;
			
			if (y[i] == 0 && y[j] == 0 && (x[i] < 0) != (x[j] < 0))
				return Location.BOUNDARY;

			if (y[i] > 0 == y[j] <= 0) {
				long det = x[i] * y[j] - x[j] * y[i];
				if (det == 0)
					return Location.BOUNDARY;
				if (det > 0 != y[j] - y[i] > 0)
					++cnt;
			}
		}
		return cnt % 2 == 0 ? Location.EXTERIOR : Location.INTERIOR;
	}

	public static Location pointInPolygon3(Point[] p, long x0, long y0) {
		int n = p.length;
		long[] x = new long[n], y = new long[n];
		for (int i = 0; i < p.length; i++) {
			x[i] = p[i].x - x0;
			y[i] = p[i].y - y0;
		}
		int rcnt = 0, lcnt = 0;
		for (int i = 0; i < n; i++) {
			int j = (i + 1) % n;
			if (y[i] == 0 && y[j] == 0 && (x[i] == 0 || x[j] == 0 || (x[i] < 0) != (x[j] < 0)))
				return Location.BOUNDARY;
			boolean rstrad = (y[i] > 0) != (y[j] > 0);
			boolean lstrad = (y[i] < 0) != (y[j] < 0);
			if (rstrad || lstrad) {
				long det = x[i] * y[j] - x[j] * y[i];
				if (det == 0)
					return Location.BOUNDARY;
				boolean f = (det > 0) == (y[j] - y[i] > 0);
				if (rstrad && f)
					++rcnt;
				if (lstrad && !f)
					++lcnt;
			}
		}
		if ((rcnt - lcnt) % 2 != 0)
			return Location.BOUNDARY;
		return rcnt % 2 == 0 ? Location.EXTERIOR : Location.INTERIOR;
	}

	static Location pointInPolygon4(Point[] p, long x, long y) {
		Random rnd = new Random(1);
		long x2 = rnd.nextInt(1000) + 1000;
		long y2 = rnd.nextInt(1000) + 1000;
		int n = p.length;
		int cnt = 0;
		for (int i = 0; i < n; i++) {
			int j = (i + 1) % n;
			if (isMiddle(p[i], new Point(x, y), p[j]))
				return Location.BOUNDARY;
			if (isIntersect(x, y, x2, y2, p[i].x, p[i].y, p[j].x, p[j].y))
				++cnt;
		}
		return cnt % 2 == 0 ? Location.EXTERIOR : Location.INTERIOR;
	}

	static long cross(long ax, long ay, long bx, long by, long cx, long cy) {
		return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
	}

	static boolean isMiddle(long a, long m, long b) {
		return m == a || m == b || (a < m) != (b < m);
	}

	static boolean isMiddle(Point a, Point m, Point b) {
		return cross(a.x, a.y, m.x, m.y, b.x, b.y) == 0 && isMiddle(a.x, m.x, b.x) && isMiddle(a.y, m.y, b.y);
	}

	static boolean isIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
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

	// usage example
	public static void main(String[] args) {
		for (int x1 = -1; x1 <= 4; x1++)
			for (int y1 = -1; y1 <= 4; y1++)
				for (int x2 = -1; x2 <= 4; x2++) {
					System.err.println(x1 + " " + y1 + " " + x2);
					for (int y2 = -1; y2 <= 4; y2++)
						for (int x3 = -1; x3 <= 4; x3++)
							for (int y3 = -1; y3 <= 4; y3++)
								for (int x4 = -1; x4 <= 1; x4++)
									for (int y4 = -2; y4 <= 0; y4++)
										for (int x5 = 0; x5 <= 1; x5++)
											for (int y5 = 1; y5 <= 2; y5++)
												for (int x6 = -1; x6 <= 1; x6++)
													for (int y6 = -1; y6 <= 1; y6++)
														for (int x7 = -1; x7 <= 1; x7++)
															for (int y7 = -1; y7 <= 1; y7++)
																for (int x = -1; x <= 1; x++)
																	for (int y = -1; y <= 1; y++) {
																		Point p1 = new Point(x1, y1);
																		Point p2 = new Point(x2, y2);
																		Point p3 = new Point(x3, y3);
																		Point p4 = new Point(x4, y4);
																		Point p5 = new Point(x5, y5);
																		Point p6 = new Point(x6, y6);
																		Point p7 = new Point(x7, y7);

																		Point[] poly = new Point[] { p1, p2, p3, p4,
																				p5, p6, p7 };

																		Location res1 = pointInPolygon(poly, x, y);
																		Location res2 = pointInPolygon2(poly, x, y);

																		if (res1 != res2) {
																			System.out.println(res1 + " " + res2);
																		}
																	}
				}
	}
}
