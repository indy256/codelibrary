public class PointInPolygon {

	public static Location pointInPolygon(Point p0, Point[] poly) {
		int n = poly.length;
		Point[] p = new Point[n];
		for (int i = 0; i < p.length; i++)
			p[i] = poly[i].minus(p0);
		int cnt = 0;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			if (p[i].x == 0 && p[i].y == 0
					|| p[i].y == 0 && p[j].y == 0 && (p[i].x < 0) != (p[j].x < 0))
				return Location.BOUNDARY;
			if ((p[i].y > 0) != (p[j].y > 0)) {
				double det = p[i].cross(p[j]);
				if (det == 0)
					return Location.BOUNDARY;
				if ((det > 0) != (p[j].y - p[i].y > 0))
					++cnt;
			}
		}
		return cnt % 2 == 0 ? Location.EXTERIOR : Location.INTERIOR;
	}

	public static enum Location {
		INTERIOR, EXTERIOR, BOUNDARY
	}

	public static class Point {
		long x, y;

		public Point(long x, long y) {
			this.x = x;
			this.y = y;
		}

		public Point minus(Point b) {
			return new Point(x - b.x, y - b.y);
		}

		public long cross(Point b) {
			return x * b.y - y * b.x;
		}
	}

	// Usage example
	public static void main(String[] args) {
		Point[] poly = new Point[4];
		poly[0] = new Point(0, 0);
		poly[1] = new Point(3, 0);
		poly[2] = new Point(3, 3);
		poly[3] = new Point(0, 3);

		System.out.println(Location.INTERIOR == pointInPolygon(new Point(1, 1), poly));
		System.out.println(Location.EXTERIOR == pointInPolygon(new Point(4, 3), poly));
		System.out.println(Location.BOUNDARY == pointInPolygon(new Point(3, 3), poly));
		System.out.println(Location.BOUNDARY == pointInPolygon(new Point(0, 2), poly));
	}
}
