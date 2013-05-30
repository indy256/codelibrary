import java.util.*;

public class CircleGeometry {
	
	static final double EPS = 1e-10;

	public static double sqr(double x) {
		return x * x;
	}

	public static double fastHypot(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static class Point {
		public double x, y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class Circle {
		public double r, x, y;

		public Circle(double x, double y, double r) {
			this.x = x;
			this.y = y;
			this.r = r;
		}

		public boolean contains(Point p) {
			return fastHypot(p.x - x, p.y - y) < r + EPS;
		}
	}

	public static Circle getCircumCircle(Point a, Point b) {
		double x = (a.x + b.x) / 2.;
		double y = (a.y + b.y) / 2.;
		double r = fastHypot(a.x - x, a.y - y);
		return new Circle(x, y, r);
	}

	public static Circle getCircumCircle(Point a, Point b, Point c) {
		double Bx = b.x - a.x;
		double By = b.y - a.y;
		double Cx = c.x - a.x;
		double Cy = c.y - a.y;
		double d = 2 * (Bx * Cy - By * Cx);
		double z1 = Bx * Bx + By * By;
		double z2 = Cx * Cx + Cy * Cy;
		double cx = Cy * z1 - By * z2;
		double cy = Bx * z2 - Cx * z1;
		double x = cx / d;
		double y = cy / d;
		double r = fastHypot(x, y);
		return new Circle(x + a.x, y + a.y, r);
	}

	public static class Line {
		final double a, b, c;

		public Line(double a, double b, double c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}

		public Line(Point p1, Point p2) {
			a = +(p1.y - p2.y);
			b = -(p1.x - p2.x);
			c = p1.x * p2.y - p2.x * p1.y;
		}
	}

	// geometric solution
	public static List<Point> circleLineIntersection(Circle circle, Line line) {
		double a = line.a;
		double b = line.b;
		double c = line.c + circle.x * a + circle.y * b;
		double r = circle.r;
		double aabb = a * a + b * b;
		List<Point> res = new ArrayList<>();
		double d = c * c / aabb - r * r;
		if (d > EPS)
			return res;
		double x0 = -a * c / aabb;
		double y0 = -b * c / aabb;
		if (d > -EPS) {
			res.add(new Point(x0 + circle.x, y0 + circle.y));
			return res;
		}
		d /= -aabb;
		double k = Math.sqrt(d < 0 ? 0 : d);
		res.add(new Point(x0 + k * b + circle.x, y0 - k * a + circle.y));
		res.add(new Point(x0 - k * b + circle.x, y0 + k * a + circle.y));
		return res;
	}

	// algebraic solution
	public static List<Point> circleLineIntersection2(Circle circle, Line line) {
		double a = line.a;
		double b = line.b;
		double c = line.c;
		double CX = circle.x, CY = circle.y;
		double R = circle.r;
		// ax+by+c=0
		// (by+c+aCX)^2+(ay-aCY)^2=(aR)^2
		boolean swap = false;
		if (Math.abs(a) < Math.abs(b)) {
			swap = true;
			double t = a;
			a = b;
			b = t;
			t = CX;
			CX = CY;
			CY = t;
		}
		List<Point> res = new ArrayList<>();
		double A = a * a + b * b;
		double B = 2.0 * b * (c + a * CX) - 2.0 * a * a * CY;
		double C = sqr(c + a * CX) + a * a * (CY * CY - R * R);
		double d = B * B - 4 * A * C;
		if (d < -EPS)
			return res;
		d = Math.sqrt(d < 0 ? 0 : d);
		double y1 = (-B + d) / (2 * A);
		double x1 = (-c - b * y1) / a;
		double y2 = (-B - d) / (2 * A);
		double x2 = (-c - b * y2) / a;
		if (swap) {
			double t = x1;
			x1 = y1;
			y1 = t;
			t = x2;
			x2 = y2;
			y2 = t;
		}
		res.add(new Point(x1, y1));
		if (d > EPS)
			res.add(new Point(x2, y2));
		return res;
	}

	public static List<Point> circleCircleIntersection(Circle c1, Circle c2) {
		if (fastHypot(c1.x - c2.x, c1.y - c2.y) < EPS) {
			if (Math.abs(c1.r - c2.r) < EPS) {
				// infinity intersection points
				return null;
			}
			return new ArrayList<>();
		}
		double dx = c2.x - c1.x;
		double dy = c2.y - c1.y;
		double A = -2 * dx;
		double B = -2 * dy;
		double C = dx * dx + dy * dy + c1.r * c1.r - c2.r * c2.r;
		List<Point> res = circleLineIntersection(new Circle(0, 0, c1.r), new Line(A, B, C));
		for (Point point : res) {
			point.x += c1.x;
			point.y += c1.y;
		}
		return res;
	}

	public static double circleCircleIntersectionArea(Circle c1, Circle c2) {
		double r = Math.min(c1.r, c2.r);
		double R = Math.max(c1.r, c2.r);
		double d = fastHypot(c1.x - c2.x, c1.y - c2.y);
		if (d < R - r + EPS) {
			return Math.PI * r * r;
		}
		if (d > R + r - EPS) {
			return 0;
		}
		double area = r * r * Math.acos((d * d + r * r - R * R) / 2 / d / r) + R * R
				* Math.acos((d * d + R * R - r * r) / 2 / d / R) - 0.5
				* Math.sqrt((-d + r + R) * (d + r - R) * (d - r + R) * (d + r + R));
		return area;
	}

	static Circle minEnclosingCircleWith2Points(List<Point> points, Point q1, Point q2) {
		Circle circle = getCircumCircle(q1, q2);
		for (int i = 0; i < points.size(); i++) {
			if (!circle.contains(points.get(i))) {
				circle = getCircumCircle(q1, q2, points.get(i));
			}
		}
		return circle;
	}

	static Circle minEnclosingCircleWith1Point(List<Point> pointsList, Point q) {
		List<Point> points = new ArrayList<>(pointsList);
		Collections.shuffle(points);
		Circle circle = getCircumCircle(points.get(0), q);
		for (int i = 1; i < points.size(); i++) {
			if (!circle.contains(points.get(i))) {
				circle = minEnclosingCircleWith2Points(points.subList(0, i), points.get(i), q);
			}
		}
		return circle;
	}

	// min enclosing circle in O(n) on average
	public static Circle minEnclosingCircle(Point[] pointsArray) {
		if (pointsArray.length == 0) {
			return new Circle(0, 0, 0);
		}
		if (pointsArray.length == 1) {
			return new Circle(pointsArray[0].x, pointsArray[0].y, 0);
		}
		List<Point> points = Arrays.asList(pointsArray);
		Collections.shuffle(points);
		Circle circle = getCircumCircle(points.get(0), points.get(1));
		for (int i = 2; i < points.size(); i++) {
			if (!circle.contains(points.get(i))) {
				circle = minEnclosingCircleWith1Point(points.subList(0, i), points.get(i));
			}
		}
		return circle;
	}

	// Usage example
	public static void main(String[] args) {
	}
}
