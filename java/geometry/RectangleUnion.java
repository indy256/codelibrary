package geometry;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class RectangleUnion {

    static class CoverageTree {
        int[] count;
        int[] len;
        int[] y;

        public CoverageTree(int[] y) {
            count = new int[4 * y.length];
            len = new int[4 * y.length];
            this.y = y;
        }

        public void update(int from, int to, int delta) {
            update(from, to, delta, 0, 0, y.length - 1);
        }

        void update(int from, int to, int delta, int root, int left, int right) {
            if (from == left && to == right) {
                count[root] += delta;
            } else {
                int mid = (left + right) >> 1;
                if (from <= mid)
                    update(from, Math.min(to, mid), delta, 2 * root + 1, left, mid);
                if (to > mid)
                    update(Math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right);
            }
            len[root] = count[root] != 0 ? y[right + 1] - y[left] : right > left ? len[2 * root + 1] + len[2 * root + 2] : 0;
        }
    }

    public static long unionArea(Rectangle[] rectangles) {
        int n = rectangles.length;
        long[] events = new long[2 * n];
        int[] y = new int[2 * n];
        for (int i = 0; i < n; i++) {
            Rectangle rect = rectangles[i];
            int x1 = rect.x;
            int x2 = rect.x + rect.width;
            int y1 = rect.y;
            int y2 = rect.y + rect.height;
            events[2 * i] = ((long) x1 << 32) + i;
            events[2 * i + 1] = ((long) x2 << 32) + (~i & 0xFFFF_FFFFL);
            y[2 * i] = y1;
            y[2 * i + 1] = y2;
        }
        Arrays.sort(events);
        Arrays.sort(y);
        CoverageTree t = new CoverageTree(y);
        long area = 0;
        int lastX = (int) (events[0] >>> 32);
        for (long event : events) {
            int i = (int) (event & 0xFFFF_FFFFL);
            boolean in = i >= 0;
            if (!in)
                i = ~i;
            int x = (int) (event >>> 32);
            int dx = x - lastX;
            int dy = t.len[0];
            area += (long) dx * dy;
            lastX = x;

            int y1 = rectangles[i].y;
            int y2 = rectangles[i].y + rectangles[i].height;
            int from = Arrays.binarySearch(y, y1);
            int to = Arrays.binarySearch(y, y2) - 1;

            t.update(from, to, in ? 1 : -1);
        }

        return area;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(100) + 1;
            Rectangle[] rectangles = new Rectangle[n];
            for (int i = 0; i < n; i++) {
                int range = 100;
                int x = rnd.nextInt(range) - range / 2;
                int y = rnd.nextInt(range) - range / 2;
                int width = rnd.nextInt(range) + 1;
                int height = rnd.nextInt(range) + 1;
                rectangles[i] = new Rectangle(x, y, width, height);
            }
            long res1 = unionArea(rectangles);

            Area area = new Area();
            for (Rectangle rectangle : rectangles)
                area.add(new Area(rectangle));
            List<Double> x = new ArrayList<>();
            List<Double> y = new ArrayList<>();
            double res2 = 0;
            double[] coords = new double[6];
            for (PathIterator pi = area.getPathIterator(null); !pi.isDone(); pi.next()) {
                int type = pi.currentSegment(coords);
                if (type == PathIterator.SEG_CLOSE) {
                    for (int i = 0, j = x.size() - 1; i < x.size(); j = i++)
                        res2 += x.get(i) * y.get(j) - x.get(j) * y.get(i);
                    x.clear();
                    y.clear();
                } else {
                    x.add(coords[0]);
                    y.add(coords[1]);
                }
            }
            res2 = Math.abs(res2) / 2;

            if (!(Math.abs(res1 - res2) < 1e-9))
                throw new RuntimeException();
        }
    }
}
