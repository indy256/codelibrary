import java.util.*;

public class ConvexHull {

	static class Point implements Comparable<Point> {
		long x, y;

		public Point(long x, long y) {
			this.x = x;
			this.y = y;
		}

		public int compareTo(Point o) {
			int comp = Long.signum(x - o.x);
			if (comp != 0)
				return comp;
			return Long.signum(y - o.y);
		}
	}

	static boolean cw(Point a, Point b, Point c) {
		return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x) < 0;
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
		Point[] res = new Point[k - 1 - (q[0].compareTo(q[1]) == 0 ? 1 : 0)];
		System.arraycopy(q, 0, res, 0, res.length);
		return res;
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
	}
}
