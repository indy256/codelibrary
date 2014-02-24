import java.awt.geom.Line2D;
import java.util.Random;

public class SegmentsIntersection {

	public static boolean isCrossIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
		long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
		long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
		long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
		long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
		return (z1 < 0 || z2 < 0) && (z1 > 0 || z2 > 0) && (z3 < 0 || z4 < 0) && (z3 > 0 || z4 > 0);
	}

	public static boolean isCrossOrTouchIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
		if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
				|| Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
			return false;
		long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
		long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
		long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
		long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
		return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
	}

	// random tests
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000_000; step++) {
			int r = 10;
			int x1 = rnd.nextInt(r) - r / 2;
			int y1 = rnd.nextInt(r) - r / 2;
			int x2 = rnd.nextInt(r) - r / 2;
			int y2 = rnd.nextInt(r) - r / 2;
			int x3 = rnd.nextInt(r) - r / 2;
			int y3 = rnd.nextInt(r) - r / 2;
			int x4 = rnd.nextInt(r) - r / 2;
			int y4 = rnd.nextInt(r) - r / 2;

			boolean crossIntersect = isCrossIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
			boolean crossOrTouchIntersect = isCrossOrTouchIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
			boolean crossOrTouchIntersect2 = Line2D.linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4);
			crossOrTouchIntersect2 &= !(x1 == x2 && y1 == y2 && x3 == x4 && y3 == y4 && (x1 != x3 || y1 != y3)); // applying fix

			if (crossOrTouchIntersect != crossOrTouchIntersect2 || crossIntersect && !crossOrTouchIntersect)
				throw new RuntimeException();
		}
	}
}
