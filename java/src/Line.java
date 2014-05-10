public class Line {

	public static class Point {
		public double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	double a, b, c;

	static double fastHypot(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public Line(double a, double b, double c) {
		double d = fastHypot(a, b);
		this.a = a / d;
		this.b = b / d;
		this.c = c / d;
	}

	public Line(Point p1, Point p2) {
		a = +(p1.y - p2.y);
		b = -(p1.x - p2.x);
		c = p1.x * p2.y - p2.x * p1.y;
	}

	public Line perpendicular(Point point) {
		return new Line(-b, a, b * point.x - a * point.y);
	}
}
