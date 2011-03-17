public class Vector {
	double x, y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector conj() {
		return new Vector(x, -y);
	}

	public Vector sub(Vector b) {
		return new Vector(x - b.x, y - b.y);
	}

	public Vector add(Vector b) {
		return new Vector(x + b.x, y + b.y);
	}

	public Vector mul(Vector b) {
		return new Vector(x * b.x - y * b.y, x * b.y + y * b.x);
	}

	public Vector div(Vector b) {
		return this.mul(b.conj()).mul(1 / b.len2());
	}

	public Vector mul(double b) {
		return new Vector(x * b, y * b);
	}

	double len2() {
		return x * x + y * y;
	}

	double len() {
		return Math.sqrt(x * x + y * y);
	}

	public Vector norm() {
		return len() == 0 ? new Vector(0, 0) : mul(1 / len());
	}

	public double cross(Vector b) {
		return x * b.y - y * b.x;
	}

	public double dot(Vector b) {
		return x * b.x + y * b.y;
	}

	public Vector rot() {
		return new Vector(-y, x);
	}

	public double proj(Vector p) {
		return dot(p) / len();
	}

	public static Vector polar(double r, double theta) {
		return new Vector(r * Math.cos(theta), r * Math.sin(theta));
	}

	public static Vector exp(Vector a) {
		return polar(Math.exp(a.x), a.y);
	}

	public Vector rotate(Vector p, double angle) {
		return p.sub(this).mul(exp(new Vector(0, angle))).add(this);
	}

	Vector rotate2(Vector p, double angle) {
		p = p.sub(this);
		double cs = Math.cos(angle);
		double sn = Math.sin(angle);
		return new Vector(p.x * cs - p.y * sn, p.x * sn + p.y * cs).add(this);
	}

	public Vector reflect(Vector p, Vector q) {
		Vector s = q.sub(p);
		return this.sub(p).div(s).conj().mul(s).add(p);
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + "]";
	}

	// Usage example
	public static void main(String[] args) {
		Vector u = new Vector(0, 0);
		Vector v = new Vector(1, 0);
		Vector a = u.rotate(v, Math.PI * 1.0);
		Vector b = v.rot().rot();
		System.out.println(a);
		System.out.println(b);
	}
}
