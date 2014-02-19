public class PointToSegmentDistance2 {

	public static double pointToSegmentDistance(Point p, Point a, Point b) {
		double ab = sqr(a.x - b.x) + sqr(a.y - b.y);
		double pa = sqr(p.x - a.x) + sqr(p.y - a.y);
		double pb = sqr(p.x - b.x) + sqr(p.y - b.y);
		if (ab > EPS && Math.abs(pa - pb) <= ab)
			return Math.abs(a.minus(p).cross(b.minus(p))) / Math.sqrt(ab);
		else
			return Math.sqrt(Math.min(pa, pb));
	}

	static final double EPS = 1e-10;

	public static class Point {
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
	}

	static double sqr(double x) {
		return x * x;
	}

	public static double pointToSegmentDistance2(double x, double y, double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double px = x - x1;
		double py = y - y1;
		double squaredLength = dx * dx + dy * dy;
		double k = dx * px + dy * py;
		if (k < 0 || squaredLength < 1e-12) {
			return Math.hypot(px, py);
		}
		if (k > squaredLength) {
			return Math.hypot(px - dx, py - dy);
		}
		k /= squaredLength;
		return Math.hypot(px - k * dx, py - k * dy);
	}

	// Usage example
	public static void main(String[] args) {
		double d = pointToSegmentDistance2(0, 0, -1, 1, 1, 1);
		System.out.println(d == 1.0);
	}
}
