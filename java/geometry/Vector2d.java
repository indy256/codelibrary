package geometry;

public class Vector2d {
    double x, y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d conj() {
        return new Vector2d(x, -y);
    }

    public Vector2d sub(Vector2d b) {
        return new Vector2d(x - b.x, y - b.y);
    }

    public Vector2d add(Vector2d b) {
        return new Vector2d(x + b.x, y + b.y);
    }

    public Vector2d mul(Vector2d b) {
        return new Vector2d(x * b.x - y * b.y, x * b.y + y * b.x);
    }

    public Vector2d div(Vector2d b) {
        return this.mul(b.conj()).mul(1 / b.len2());
    }

    public Vector2d mul(double b) {
        return new Vector2d(x * b, y * b);
    }

    double len2() {
        return x * x + y * y;
    }

    double len() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2d norm() {
        return len() == 0 ? new Vector2d(0, 0) : mul(1 / len());
    }

    public double cross(Vector2d b) {
        return x * b.y - y * b.x;
    }

    public double dot(Vector2d b) {
        return x * b.x + y * b.y;
    }

    public Vector2d rot() {
        return new Vector2d(-y, x);
    }

    public double proj(Vector2d p) {
        return dot(p) / len();
    }

    public static Vector2d polar(double r, double theta) {
        return new Vector2d(r * Math.cos(theta), r * Math.sin(theta));
    }

    public static Vector2d exp(Vector2d a) {
        return polar(Math.exp(a.x), a.y);
    }

    public Vector2d rotate(Vector2d p, double angle) {
        return p.sub(this).mul(exp(new Vector2d(0, angle))).add(this);
    }

    Vector2d rotate2(Vector2d p, double angle) {
        p = p.sub(this);
        double cs = Math.cos(angle);
        double sn = Math.sin(angle);
        return new Vector2d(p.x * cs - p.y * sn, p.x * sn + p.y * cs).add(this);
    }

    public Vector2d reflect(Vector2d p, Vector2d q) {
        Vector2d s = q.sub(p);
        return this.sub(p).div(s).conj().mul(s).add(p);
    }

    @Override
    public String toString() {
        return "Vector2d [x=" + x + ", y=" + y + "]";
    }

    // Usage example
    public static void main(String[] args) {
        Vector2d u = new Vector2d(0, 0);
        Vector2d v = new Vector2d(1, 0);
        Vector2d a = u.rotate(v, Math.PI * 1.0);
        Vector2d b = v.rot().rot();
        System.out.println(a);
        System.out.println(b);
    }
}
