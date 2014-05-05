import java.awt.geom.Point2D;

public class AngleAreaOrientation {

	// Returns -1 for clockwise, 0 for straight line, 1 for counterclockwise order
	// Line2D.relativeCCW
	public static int orientation(long ax, long ay, long bx, long by, long cx, long cy) {
		bx -= ax;
		by -= ay;
		cx -= ax;
		cy -= ay;
		long cross = bx * cy - by * cx;
		return cross < 0 ? -1 : cross > 0 ? 1 : 0;
	}

	public static double angleBetween(long ax, long ay, long bx, long by) {
		return Math.atan2(ax * by - ay * bx, ax * bx + ay * by);
		// ? return Math.atan2(by, bx) - Math.atan2(ay, ax);
	}

	public static double angle(long x, long y) {
		return Math.atan2(y, x);
	}

	public static long doubleSignedArea(int[] x, int[] y) {
		int n = x.length;
		long area = 0;
		for (int i = 0, j = n - 1; i < n; j = i++) {
			area += (long) (x[i] - x[j]) * (y[i] + y[j]);
//			area += (long) x[i] * y[j] - (long) x[j] * y[i];
		}
		return area;
	}

	public Point2D.Double rotateCCW(Point2D.Double p, double angle) {
		return new Point2D.Double(p.x * Math.cos(angle) - p.y * Math.sin(angle), p.x * Math.sin(angle) + p.y * Math.cos(angle));
	}

	public static void main(String[] args) {

	}
}
