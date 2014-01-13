import java.util.*;

public class ConvexHull {

	public static Point[] convexHull(Point[] p) {
		int n = p.length;
		if (n <= 1)
			return p;
		Arrays.sort(p);
		Point[] q = new Point[n * 2];
		int cnt = 0;
		for (int i = 0; i < n; q[cnt++] = p[i++])
			for (; cnt > 1 && cross(q[cnt - 2], q[cnt - 1], p[i]) >= 0; --cnt) ;
		for (int i = n - 2, t = cnt; i >= 0; q[cnt++] = p[i--])
			for (; cnt > t && cross(q[cnt - 2], q[cnt - 1], p[i]) >= 0; --cnt) ;
		return Arrays.copyOf(q, cnt - 1 - (q[0].compareTo(q[1]) == 0 ? 1 : 0));
	}

	public static class Point implements Comparable<Point> {
		long x, y;

		public Point(long x, long y) {
			this.x = x;
			this.y = y;
		}

		public int compareTo(Point o) {
			return Long.compare(x, o.x) != 0 ? Long.compare(x, o.x) : Long.compare(y, o.y);
		}
	}

	static long cross(Point a, Point b, Point c) {
		return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
	}

	// Usage example
	public static void main(String[] args) {
		Point[] points = new Point[4];
		points[0] = new Point(0, 0);
		points[1] = new Point(3, 0);
		points[2] = new Point(0, 3);
		points[3] = new Point(1, 1);
		Point[] hull = convexHull(points);
		System.out.println(3 == hull.length);

		Random rnd = new Random(1);
		int n = 1_000_000;
		points = new Point[n];
		for (int i = 0; i < n; i++)
			points[i] = new Point(rnd.nextInt(10_000), rnd.nextInt(10_000));
		System.out.println(convexHull(points).length);
	}
}
