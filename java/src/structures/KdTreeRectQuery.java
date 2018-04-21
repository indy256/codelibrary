package structures;

import java.util.Random;

public class KdTreeRectQuery {

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int[] tx;
    int[] ty;
    int[] minx, miny, maxx, maxy;
    int[] count;

    public KdTreeRectQuery(Point[] points) {
        int n = points.length;
        tx = new int[n];
        ty = new int[n];
        minx = new int[n];
        miny = new int[n];
        maxx = new int[n];
        maxy = new int[n];
        count = new int[n];
        build(0, n, true, points);
    }

    void build(int low, int high, boolean divX, Point[] points) {
        if (low >= high)
            return;
        int mid = (low + high) >>> 1;
        nth_element(points, low, high, mid, divX);

        tx[mid] = points[mid].x;
        ty[mid] = points[mid].y;
        count[mid] = high - low;

        minx[mid] = Integer.MAX_VALUE;
        miny[mid] = Integer.MAX_VALUE;
        maxx[mid] = Integer.MIN_VALUE;
        maxy[mid] = Integer.MIN_VALUE;
        for (int i = low; i < high; i++) {
            minx[mid] = Math.min(minx[mid], points[i].x);
            miny[mid] = Math.min(miny[mid], points[i].y);
            maxx[mid] = Math.max(maxx[mid], points[i].x);
            maxy[mid] = Math.max(maxy[mid], points[i].y);
        }

        build(low, mid, !divX, points);
        build(mid + 1, high, !divX, points);
    }

    static final Random rnd = new Random(1);

    // See: http://www.cplusplus.com/reference/algorithm/nth_element
    static void nth_element(Point[] a, int low, int high, int n, boolean divX) {
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

    static int partition(Point[] a, int fromInclusive, int toExclusive, int separatorIndex, boolean divX) {
        int i = fromInclusive;
        int j = toExclusive - 1;
        if (i >= j) return j;
        double separator = divX ? a[separatorIndex].x : a[separatorIndex].y;
        swap(a, i++, separatorIndex);
        while (i <= j) {
            while (i <= j && (divX ? a[i].x : a[i].y) < separator)
                ++i;
            while (i <= j && (divX ? a[j].x : a[j].y) > separator)
                --j;
            if (i >= j)
                break;
            swap(a, i++, j--);
        }
        swap(a, j, fromInclusive);
        return j;
    }

    static void swap(Point[] a, int i, int j) {
        Point t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    // number of points in [x1,x2] x [y1,y2]
    public int count(int x1, int y1, int x2, int y2) {
        return count(0, tx.length, x1, y1, x2, y2);
    }

    int count(int low, int high, int x1, int y1, int x2, int y2) {
        if (low >= high)
            return 0;
        int mid = (low + high) >>> 1;

        int ax = minx[mid];
        int ay = miny[mid];
        int bx = maxx[mid];
        int by = maxy[mid];

        if (ax > x2 || x1 > bx || ay > y2 || y1 > by)
            return 0;
        if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2)
            return count[mid];

        int res = 0;
        res += count(low, mid, x1, y1, x2, y2);
        res += count(mid + 1, high, x1, y1, x2, y2);
        if (x1 <= tx[mid] && tx[mid] <= x2 && y1 <= ty[mid] && ty[mid] <= y2)
            ++res;
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int[] x = {0, 10, 0, 10};
        int[] y = {0, 10, 10, 0};

        Point[] points = new Point[x.length];
        for (int i = 0; i < points.length; i++)
            points[i] = new Point(x[i], y[i]);

        KdTreeRectQuery kdTree = new KdTreeRectQuery(points);
        int count = kdTree.count(0, 0, 10, 10);
        System.out.println(4 == count);
    }
}
