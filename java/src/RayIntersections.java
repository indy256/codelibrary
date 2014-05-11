public class RayIntersections {
	static final double eps = 1e-9;

	public static class Ray {
		Vector org;
		Vector dir;
	}

	public static class Sphere {
		Vector center;
		double r;
	}

	public static class Plane {
		Vector n;
		double d;
	}

	public static double sqr(double x) {
		return x * x;
	}

	// algebraic solution
	public static double raySphereIntersection(Ray ray, Sphere s) {
		Vector CO = ray.org.sub(s.center);
		double b = ray.dir.dot(CO);
		double c = ray.org.len2() - s.r * s.r;
		double d = b * b - c;

		if (d < eps)
			return Double.NaN;

		d = Math.sqrt(d < 0 ? 0 : d);

		double t1 = -b - d;
		double t2 = -b + d;
		return t2 < eps ? Double.NaN : (t1 > eps ? t1 : t2);
	}

	// geometric solution
	public static double raySphereIntersection2(Ray ray, Sphere s) {
		Vector OC = s.center.sub(ray.org);
		double t0 = OC.dot(ray.dir); // point, closest to center
		double d = s.r * s.r - (OC.len2() - t0 * t0);

		if (d < -eps)
			return Double.NaN;

		d = Math.sqrt(d < 0 ? 0 : d);
		double t1, t2;

		if (t0 < d) { // we are inside
			t1 = t0 + d;
			t2 = t0 - d;
		} else { // we are outside
			t1 = t0 - d;
			t2 = t0 + d;
		}

		if (Math.abs(t1) < eps)
			t1 = t2;

		return t1 > eps ? t1 : Double.NaN;
	}

	// algebraic solution
	public static double rayPlaneIntersection(Ray ray, Plane p) {
		double b = p.n.dot(ray.dir);
		if (Math.abs(b) < eps)
			return Double.NaN;
		double a = -(p.n.dot(ray.org) + p.d);
		double t = a / b;
		return t < eps ? Double.NaN : t;
	}

	public static class Vector {
		double x, y, z;

		public Vector(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public Vector sub(Vector b) {
			return new Vector(x - b.x, y - b.y, z - b.z);
		}

		public Vector add(Vector b) {
			return new Vector(x + b.x, y + b.y, z + b.z);
		}

		public static double det(double a, double b, double c, double d) {
			return a * d - b * c;
		}

		public Vector mul(Vector b) {
			return new Vector(det(y, b.y, z, b.z), -det(x, b.x, z, b.z), det(x, b.x, y, b.y));
		}

		public Vector mul(double b) {
			return new Vector(x * b, y * b, z * b);
		}

		double len2() {
			return x * x + y * y + z * z;
		}

		double len() {
			return Math.sqrt(x * x + y * y + z * z);
		}

		public Vector norm() {
			return len() == 0 ? new Vector(0, 0, 0) : mul(1 / len());
		}

		public double dot(Vector b) {
			return x * b.x + y * b.y + z * b.z;
		}

		public double proj(Vector p) {
			return dot(p) / len();
		}

		@Override
		public String toString() {
			return "Vector2d [x=" + x + ", y=" + y + ", z=" + z + "]";
		}
	}

	// usage example
	public static void main(String[] args) {
		Sphere s = new Sphere();
		s.center = new Vector(0, 0, 0);
		Ray ray = new Ray();
		for (int x1 = -4; x1 <= 4; x1++)
			for (int y1 = -4; y1 <= 4; y1++)
				for (int z1 = -4; z1 <= 4; z1++)
					for (int x2 = -4; x2 <= 4; x2++)
						for (int y2 = -4; y2 <= 4; y2++)
							for (int z2 = -4; z2 <= 4; z2++) {
								if (x2 == 0 && y2 == 0 && z2 == 0)
									continue;
								ray.org = new Vector(x1, y1, z1);
								ray.dir = new Vector(x2, y2, z2).norm();

								for (int r = 0; r <= 4; r++) {
									s.r = r;
									double t1 = raySphereIntersection(ray, s);
									double t2 = raySphereIntersection(ray, s);
									if (!(Double.isNaN(t1) && Double.isNaN(t2) || !(Math.abs(t1 - t2) > eps))) {
										System.out.println(t1 + " " + t2);
									}
								}
							}
	}
}
