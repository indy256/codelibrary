package experimental;

import java.util.*;

public class KdTreePointQuery2 {

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int[] tx;
    int[] ty;

    public KdTreePointQuery2(Point[] points) {
        int n = points.length;
        tx = new int[n];
        ty = new int[n];
        build(0, n, true, points);
    }

    void build(int low, int high, boolean divX, Point[] points) {
        if (low >= high)
            return;
        int mid = (low + high) >> 1;
        nth_element(points, low, high, mid - low, divX);

        tx[mid] = points[mid].x;
        ty[mid] = points[mid].y;

        build(low, mid, !divX, points);
        build(mid + 1, high, !divX, points);
    }

    // analog of C++ nth_element()
    static int nth_element(Point[] a, int low, int high, int n, boolean divX) {
        if (low == high - 1)
            return low;
        int q = randomizedPartition(a, low, high, divX);
        int k = q - low;
        if (n < k)
            return nth_element(a, low, q, n, divX);
        if (n > k)
            return nth_element(a, q + 1, high, n - k - 1, divX);
        return q;
    }

    static final Random rnd = new Random();

    static int randomizedPartition(Point[] a, int low, int high, boolean divX) {
        swap(a, low + rnd.nextInt(high - low), high - 1);
        int v = divX ? a[high - 1].x : a[high - 1].y;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (divX && a[j].x <= v || !divX && a[j].y <= v) {
                ++i;
                swap(a, i, j);
            }
        }
        return i;
    }

    static void swap(Point[] a, int i, int j) {
        Point t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    long bestDist;
    int bestNode;

    public int findNearestNeighbour(int x, int y) {
        bestDist = Long.MAX_VALUE;
        findNearestNeighbour(0, tx.length, x, y, x, y, true);
        return bestNode;
    }

    void findNearestNeighbour(int low, int high, int x, int y, int lastx, int lasty, boolean divX) {
        if (low >= high)
            return;
        int mid = (low + high) >> 1;
        int dx = x - tx[mid];
        int dy = y - ty[mid];
        long dxdx = (long) dx * dx;
        long dydy = (long) dy * dy;
        long d = dxdx + dydy;
        if (bestDist > d) {
            bestDist = d;
            bestNode = mid;
        }
        int delta;
        long delta2;
        int nlastx;
        int nlasty;
        if (divX) {
            delta = dx;
            long sy = y - lasty;
            delta2 = dxdx + sy * sy;
            nlastx = tx[mid];
            nlasty = lasty;
        } else {
            delta = dy;
            long sx = x - lastx;
            delta2 = dydy + sx * sx;
            nlastx = lastx;
            nlasty = ty[mid];
        }

        int l1 = low;
        int h1 = mid;
        int l2 = mid + 1;
        int h2 = high;
        if (delta > 0) {
            int t = l1;
            l1 = l2;
            l2 = t;
            t = h1;
            h1 = h2;
            h2 = t;
        }
        findNearestNeighbour(l1, h1, x, y, lastx, lasty, !divX);

        if (delta2 < bestDist)
            findNearestNeighbour(l2, h2, x, y, nlastx, nlasty, !divX);
    }

    // Usage example
    public static void main(String[] args) {
        int[] x = { 0, 10, 0, 10 };
        int[] y = { 0, 10, 10, 0 };

        Point[] points = new Point[x.length];
        for (int i = 0; i < points.length; i++)
            points[i] = new Point(x[i], y[i]);

        KdTreePointQuery2 kdTree = new KdTreePointQuery2(points);
        int res = kdTree.findNearestNeighbour(6, 3);
        System.out.println(points[3] == points[res]);
    }
}
