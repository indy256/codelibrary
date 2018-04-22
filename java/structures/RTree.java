package structures;

import java.util.*;

// https://en.wikipedia.org/wiki/R-tree
public class RTree {

    public static class Segment {
        final int x1, y1, x2, y2;

        public Segment(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    final int[] x1, y1, x2, y2;
    final int[] minx, maxx, miny, maxy;

    public RTree(Segment[] segments) {
        int n = segments.length;
        x1 = new int[n];
        y1 = new int[n];
        x2 = new int[n];
        y2 = new int[n];
        minx = new int[n];
        maxx = new int[n];
        miny = new int[n];
        maxy = new int[n];
        Arrays.fill(minx, Integer.MAX_VALUE);
        Arrays.fill(maxx, Integer.MIN_VALUE);
        Arrays.fill(miny, Integer.MAX_VALUE);
        Arrays.fill(maxy, Integer.MIN_VALUE);
        build(0, n, true, segments);
    }

    void build(int low, int high, boolean divX, Segment[] segments) {
        if (low >= high)
            return;
        int mid = (low + high) >>> 1;
        nth_element(segments, low, high, mid, divX);

        x1[mid] = segments[mid].x1;
        y1[mid] = segments[mid].y1;
        x2[mid] = segments[mid].x2;
        y2[mid] = segments[mid].y2;

        for (int i = low; i < high; i++) {
            minx[mid] = Math.min(minx[mid], Math.min(segments[i].x1, segments[i].x2));
            miny[mid] = Math.min(miny[mid], Math.min(segments[i].y1, segments[i].y2));
            maxx[mid] = Math.max(maxx[mid], Math.max(segments[i].x1, segments[i].x2));
            maxy[mid] = Math.max(maxy[mid], Math.max(segments[i].y1, segments[i].y2));
        }

        build(low, mid, !divX, segments);
        build(mid + 1, high, !divX, segments);
    }

    static final Random rnd = new Random(1);

    // See: http://www.cplusplus.com/reference/algorithm/nth_element
    static void nth_element(Segment[] a, int low, int high, int n, boolean divX) {
        while (true) {
            int k = partition(a, low, high, low + rnd.nextInt(high - low), divX);
            if (n < k)
                high = k;
            else if (n > k)
                low = k + 1;
            else
                return;
        }
    }

    static int partition(Segment[] a, int fromInclusive, int toExclusive, int separatorIndex, boolean divX) {
        int i = fromInclusive;
        int j = toExclusive - 1;
        if (i >= j) return j;
        int separator = divX ? a[separatorIndex].x1 + a[separatorIndex].x2 : a[separatorIndex].y1 + a[separatorIndex].y2;
        swap(a, i++, separatorIndex);
        while (i <= j) {
            while (i <= j && (divX ? a[i].x1 + a[i].x2 : a[i].y1 + a[i].y2) < separator)
                ++i;
            while (i <= j && (divX ? a[j].x1 + a[j].x2 : a[j].y1 + a[j].y2) > separator)
                --j;
            if (i >= j)
                break;
            swap(a, i++, j--);
        }
        swap(a, j, fromInclusive);
        return j;
    }

    static void swap(Segment[] a, int i, int j) {
        Segment t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    double bestDist;
    int bestNode;

    public int findNearestNeighbour(int x, int y) {
        bestDist = Double.POSITIVE_INFINITY;
        findNearestNeighbour(0, x1.length, x, y, true);
        return bestNode;
    }

    void findNearestNeighbour(int low, int high, int x, int y, boolean divX) {
        if (low >= high)
            return;
        int mid = (low + high) >>> 1;
        double distance = pointToSegmentSquaredDistance(x, y, x1[mid], y1[mid], x2[mid], y2[mid]);
        if (bestDist > distance) {
            bestDist = distance;
            bestNode = mid;
        }

        long delta = divX ? 2 * x - x1[mid] - x2[mid] : 2 * y - y1[mid] - y2[mid];

        if (delta <= 0) {
            findNearestNeighbour(low, mid, x, y, !divX);
            if (mid + 1 < high) {
                int mid1 = (mid + 1 + high) >>> 1;
                long dist = divX ? getDist(x, minx[mid1], maxx[mid1]) : getDist(y, miny[mid1], maxy[mid1]);
                if (dist * dist < bestDist)
                    findNearestNeighbour(mid + 1, high, x, y, !divX);
            }
        } else {
            findNearestNeighbour(mid + 1, high, x, y, !divX);
            if (low < mid) {
                int mid1 = (low + mid) >>> 1;
                long dist = divX ? getDist(x, minx[mid1], maxx[mid1]) : getDist(y, miny[mid1], maxy[mid1]);
                if (dist * dist < bestDist)
                    findNearestNeighbour(low, mid, x, y, !divX);
            }
        }
    }

    static int getDist(int v, int min, int max) {
        if (v <= min)
            return min - v;
        if (v >= max)
            return v - max;
        return 0;
    }

    static double pointToSegmentSquaredDistance(int x, int y, int x1, int y1, int x2, int y2) {
        long dx = x2 - x1;
        long dy = y2 - y1;
        long px = x - x1;
        long py = y - y1;
        long squaredLength = dx * dx + dy * dy;
        long dotProduct = dx * px + dy * py;
        if (dotProduct <= 0 || squaredLength == 0)
            return px * px + py * py;
        if (dotProduct >= squaredLength)
            return (px - dx) * (px - dx) + (py - dy) * (py - dy);
        double q = (double) dotProduct / squaredLength;
        return (px - q * dx) * (px - q * dx) + (py - q * dy) * (py - q * dy);
    }

    // random test
    public static void main(String[] args) {
        for (int step = 0; step < 100_000; step++) {
            int qx = rnd.nextInt(1000) - 500;
            int qy = rnd.nextInt(1000) - 500;
            int n = rnd.nextInt(100) + 1;
            Segment[] segments = new Segment[n];
            double minDist = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                int x1 = rnd.nextInt(1000) - 500;
                int y1 = rnd.nextInt(1000) - 500;
                int x2 = x1 + rnd.nextInt(10);
                int y2 = y1 + rnd.nextInt(10);
                segments[i] = new Segment(x1, y1, x2, y2);
                minDist = Math.min(minDist, pointToSegmentSquaredDistance(qx, qy, x1, y1, x2, y2));
            }
            RTree rTree = new RTree(segments);
            int index = rTree.findNearestNeighbour(qx, qy);
            Segment s = segments[index];
            if (minDist != rTree.bestDist || !(Math.abs(pointToSegmentSquaredDistance(qx, qy, s.x1, s.y1, s.x2, s.y2) - minDist) < 1e-9))
                throw new RuntimeException();
        }
    }
}
