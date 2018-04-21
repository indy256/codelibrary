package geometry;

import java.awt.geom.Line2D;
import java.util.Random;

public class PointToSegmentDistance {

    public static double pointToSegmentDistance(int x, int y, int x1, int y1, int x2, int y2) {
        long dx = x2 - x1;
        long dy = y2 - y1;
        long px = x - x1;
        long py = y - y1;
        long squaredLength = dx * dx + dy * dy;
        long dotProduct = dx * px + dy * py;
        if (dotProduct <= 0 || squaredLength == 0)
            return fastHypot(px, py);
        if (dotProduct >= squaredLength)
            return fastHypot(px - dx, py - dy);
        double q = (double) dotProduct / squaredLength;
        return fastHypot(px - q * dx, py - q * dy);
    }

    static double fastHypot(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    // Line2D.ptLineDist
    public static double pointToLineDistance(long x, long y, long a, long b, long c) {
        return Math.abs(a * x + b * y + c) / fastHypot(a, b);
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000_000; step++) {
            int r = 10;
            int x = rnd.nextInt(r) - r / 2;
            int y = rnd.nextInt(r) - r / 2;
            int x1 = rnd.nextInt(r) - r / 2;
            int y1 = rnd.nextInt(r) - r / 2;
            int x2 = rnd.nextInt(r) - r / 2;
            int y2 = rnd.nextInt(r) - r / 2;
            double res1 = pointToSegmentDistance(x, y, x1, y1, x2, y2);
            double res2 = Line2D.ptSegDist(x1, y1, x2, y2, x, y);
            if (!(Math.abs(res1 - res2) < 1e-9))
                throw new RuntimeException();
        }
    }
}
