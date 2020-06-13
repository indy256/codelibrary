package test.geometry;

import geometry.PolygonCircleIntersection;
import java.util.Random;

public class PolygonCircleIntersectionTest {
    // random tests
    public static void main(String[] args) {
        for (int step = 0; step < 1000; step++) {
            System.out.println(step);
            int n = rnd.nextInt(10) + 3;
            int range = 10;
            int[][] xy = getRandomPolygon(n, range, range);
            int[] x = xy[0];
            int[] y = xy[1];
            int r = rnd.nextInt(10) + 1;
            double res1 = PolygonCircleIntersection.polygonCircleIntersection(x, y, r);
            double res2 = polygonCircleIntersectionMonteCarlo(x, y, r);
            if (!(Math.abs(res1 - res2) < 0.1))
                throw new RuntimeException();
        }
    }

    static double polygonCircleIntersectionMonteCarlo(int[] x, int[] y, int r) {
        int count = 0;
        int hits = 0;
        int minx = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxy = Integer.MIN_VALUE;
        for (int i = 0; i < x.length; i++) {
            minx = Math.min(minx, x[i]);
            maxx = Math.max(maxx, x[i]);
            miny = Math.min(miny, y[i]);
            maxy = Math.max(maxy, y[i]);
        }
        for (double px = minx; px <= maxx; px += 0.005)
            for (double py = miny; py <= maxy; py += 0.005) {
                ++count;
                if (pointInPolygon(px, py, x, y) >= 0 && px * px + py * py <= r * r)
                    ++hits;
            }
        return (double) hits / count * ((maxx - minx) * (maxy - miny));
    }

    static int pointInPolygon(double qx, double qy, int[] x, int[] y) {
        int n = x.length;
        int cnt = 0;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (y[i] == qy && (x[i] == qx || y[j] == qy && (x[i] <= qx || x[j] <= qx) && (x[i] >= qx || x[j] >= qx)))
                return 0; // boundary
            if ((y[i] > qy) != (y[j] > qy)) {
                double det = (x[i] - qx) * (y[j] - qy) - (x[j] - qx) * (y[i] - qy);
                if (det == 0)
                    return 0; // boundary
                if ((det > 0) != (y[j] - y[i] > 0))
                    ++cnt;
            }
        }
        return cnt % 2 == 0 ? -1 /* exterior */ : 1 /* interior */;
    }

    static Random rnd = new Random(1);

    static int[][] getRandomPolygon(int n, int maxWidth, int maxHeight) {
        int[] x = new int[n];
        int[] y = new int[n];
        int[] p = new int[n];
        while (true) {
            for (int i = 0; i < n; i++) {
                x[i] = rnd.nextInt(maxWidth);
                y[i] = rnd.nextInt(maxHeight);
                p[i] = i;
            }
            for (boolean improved = true; improved;) {
                improved = false;
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int[] p1 = p.clone();
                        reverse(p1, i, j);
                        if (len(x, y, p) > len(x, y, p1)) {
                            p = p1;
                            improved = true;
                        }
                    }
                }
            }
            int[] tx = x.clone();
            int[] ty = y.clone();
            for (int i = 0; i < n; i++) {
                x[i] = tx[p[i]];
                y[i] = ty[p[i]];
            }
            boolean ok = true;
            for (int i = 0; i < n; i++) {
                long x1 = x[(i - 1 + n) % n] - x[i];
                long y1 = y[(i - 1 + n) % n] - y[i];
                long x2 = x[(i + 1) % n] - x[i];
                long y2 = y[(i + 1) % n] - y[i];
                ok &= x1 * y2 - x2 * y1 != 0 || x1 * x2 + y1 * y2 <= 0;
            }
            for (int i2 = 0, i1 = p.length - 1; i2 < p.length; i1 = i2++)
                for (int j2 = 0, j1 = p.length - 1; j2 < p.length; j1 = j2++)
                    ok &= i1 == j1 || i1 == j2 || i2 == j1
                        || !isCrossOrTouchIntersect(x[i1], y[i1], x[i2], y[i2], x[j1], y[j1], x[j2], y[j2]);
            if (ok)
                return new int[][] {x, y};
        }
    }

    // http://en.wikipedia.org/wiki/2-opt
    static void reverse(int[] p, int i, int j) {
        int n = p.length;
        // reverse order from i to j
        while (i != j) {
            int t = p[j];
            p[j] = p[i];
            p[i] = t;
            i = (i + 1) % n;
            if (i == j)
                break;
            j = (j - 1 + n) % n;
        }
    }

    static double len(int[] x, int[] y, int[] p) {
        double res = 0;
        for (int i = 0, j = p.length - 1; i < p.length; j = i++) {
            double dx = x[p[i]] - x[p[j]];
            double dy = y[p[i]] - y[p[j]];
            res += Math.sqrt(dx * dx + dy * dy);
        }
        return res;
    }

    static boolean isCrossOrTouchIntersect(long x1, long y1, long x2, long y2, long x3, long y3, long x4, long y4) {
        if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
            || Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
            return false;
        long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
        long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
        return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
    }
}
