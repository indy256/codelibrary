package structures;

import java.util.Random;

// https://en.wikipedia.org/wiki/Metric_tree
public class MetricTree {

    int[] x;
    int[] y;

    public MetricTree(int[] x, int[] y) {
        this.x = x;
        this.y = y;
        build(0, x.length);
    }

    static final Random rnd = new Random(1);

    void build(int low, int high) {
        if (high - low <= 2)
            return;
        swap(low + rnd.nextInt(high - low), low);
        int mid = (low + 1 + high) >>> 1;
        nth_element(low + 1, high, mid);

        build(low + 1, mid);
        build(mid + 1, high);
    }

    // See http://www.cplusplus.com/reference/algorithm/nth_element
    void nth_element(int low, int high, int n) {
        int center = low - 1;
        while (true) {
            int k = partition(center, low, high, low + rnd.nextInt(high - low));
            if (n < k)
                high = k;
            else if (n > k)
                low = k + 1;
            else
                return;
        }
    }

    int partition(int center, int fromInclusive, int toExclusive, int separatorIndex) {
        int i = fromInclusive;
        int j = toExclusive - 1;
        if (i >= j) return j;
        long separator = dist2(x[center], y[center], x[separatorIndex], y[separatorIndex]);
        swap(i++, separatorIndex);
        while (i <= j) {
            while (i <= j && (dist2(x[center], y[center], x[i], y[i]) < separator))
                ++i;
            while (i <= j && (dist2(x[center], y[center], x[j], y[j]) > separator))
                --j;
            if (i >= j)
                break;
            swap(i++, j--);
        }
        swap(j, fromInclusive);
        return j;
    }

    static long dist2(int x1, int y1, int x2, int y2) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        return (long) dx * dx + (long) dy * dy;
    }

    void swap(int i, int j) {
        int t = x[i];
        x[i] = x[j];
        x[j] = t;
        t = y[i];
        y[i] = y[j];
        y[j] = t;
    }

    long bestDist;
    int bestNode;

    public int findNearestNeighbour(int px, int py) {
        bestDist = Long.MAX_VALUE;
        findNearestNeighbour(0, x.length, px, py);
        return bestNode;
    }

    void findNearestNeighbour(int low, int high, int px, int py) {
        if (high - low <= 0)
            return;
        long d2 = dist2(px, py, x[low], y[low]);
        if (bestDist > d2) {
            bestDist = d2;
            bestNode = low;
        }

        if (high - low <= 1)
            return;

        int mid = (low + 1 + high) >>> 1;
        long dist2 = dist2(px, py, x[mid], y[mid]);
        if (bestDist > dist2) {
            bestDist = dist2;
            bestNode = mid;
        }

        double R = Math.sqrt(dist2(x[low], y[low], x[mid], y[mid]));
        double r = Math.sqrt(bestDist);
        double d = Math.sqrt(d2);

        if (d <= R + r) {
            findNearestNeighbour(low + 1, mid, px, py);
        }
        if (d >= R - r) {
            findNearestNeighbour(mid + 1, high, px, py);
        }
    }

    // random test
    public static void main(String[] args) {
        for (int step = 0; step < 100_000; step++) {
            int qx = rnd.nextInt(100) - 50;
            int qy = rnd.nextInt(100) - 50;
            int n = rnd.nextInt(100) + 1;
            int[] x = new int[n];
            int[] y = new int[n];
            long minDist = Long.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                x[i] = rnd.nextInt(100) - 50;
                y[i] = rnd.nextInt(100) - 50;
                minDist = Math.min(minDist, dist2(qx, qy, x[i], y[i]));
            }
            MetricTree metricTree = new MetricTree(x, y);
            int index = metricTree.findNearestNeighbour(qx, qy);
            if (minDist != metricTree.bestDist || dist2(qx, qy, x[index], y[index]) != minDist)
                throw new RuntimeException();
        }
    }
}
