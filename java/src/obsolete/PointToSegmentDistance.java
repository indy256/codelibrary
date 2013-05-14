package obsolete;

public class PointToSegmentDistance {
	public static double pointToSegmentDistance(double x, double y, double x1, double y1, double x2, double y2) {
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
		double d = pointToSegmentDistance(0, 0, -1, 1, 1, 1);
		System.out.println(d == 1.0);
	}
}
