import java.util.*;

public class ConvexHull {

	public static Point[] convexHull(Point[] p) {
		int n = p.length;
		if (n <= 1)
			return p;
		Arrays.sort(p, (a, b) -> Integer.compare(a.x, b.x) != 0 ? Integer.compare(a.x, b.x) : Integer.compare(a.y, b.y));
		Point[] q = new Point[n * 2];
		int cnt = 0;
		for (int i = 0; i < n; q[cnt++] = p[i++])
			for (; cnt > 1 && cross(q[cnt - 2], q[cnt - 1], p[i]) >= 0; --cnt) ;
		for (int i = n - 2, t = cnt; i >= 0; q[cnt++] = p[i--])
			for (; cnt > t && cross(q[cnt - 2], q[cnt - 1], p[i]) >= 0; --cnt) ;
		return Arrays.copyOf(q, cnt - 1 - (q[0].x == q[1].x && q[0].y == q[1].y ? 1 : 0));
	}

	public static class Point {
		final int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	static long cross(Point a, Point b, Point c) {
		return (long) (b.x - a.x) * (c.y - a.y) - (long) (b.y - a.y) * (c.x - a.x);
	}

	// random test
	public static void main(String[] args) {
		Random rnd = new Random(1);
		for (int step = 0; step < 1000_000; step++) {
			int n = rnd.nextInt(10) + 1;
			Point[] points = new Point[n];
			for (int i = 0; i < n; i++) {
				int rangle = 10;
				points[i] = new Point(rnd.nextInt(rangle) - rangle / 2, rnd.nextInt(rangle) - rangle / 2);
			}
			Point[] convexHull = convexHull(points);
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
