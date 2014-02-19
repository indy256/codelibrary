public class PointToSegmentDistance {

	public static double pointToSegmentDistance(int x, int y, int x1, int y1, int x2, int y2) {
		long dx = x2 - x1;
		long dy = y2 - y1;
		long px = x - x1;
		long py = y - y1;
		long squaredLength = dx * dx + dy * dy;
		long dotProduct = dx * px + dy * py;
		if (dotProduct <= 0 || squaredLength == 0)
			return Math.hypot(px, py);
		if (dotProduct >= squaredLength)
			return Math.hypot(px - dx, py - dy);
		double q = (double) dotProduct / squaredLength;
		return Math.hypot(px - q * dx, py - q * dy);
	}

	// Usage example
	public static void main(String[] args) {
		double d = pointToSegmentDistance(0, 0, -1, 1, 1, 1);
		System.out.println(d == 1.0);
	}
}
