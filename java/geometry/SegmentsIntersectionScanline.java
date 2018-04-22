package geometry;

import java.util.*;

public class SegmentsIntersectionScanline {

    public static Segment[] findIntersection(Segment[] s) {
        int n = s.length;
        Event[] events = new Event[n * 2];
        for (int i = 0, cnt = 0; i < n; ++i) {
            events[cnt++] = new Event(s[i].x1, s[i].y1, 1, s[i]);
            events[cnt++] = new Event(s[i].x2, s[i].y2, -1, s[i]);
        }
        Arrays.sort(events, eventComparator);
        NavigableSet<Segment> set = new TreeSet<>(segmentComparator);

        for (Event event : events) {
            Segment cur = event.segment;
            if (event.type == 1) {
                Segment floor = set.floor(cur);
                if (floor != null && isCrossOrTouchIntersect(cur, floor))
                    return new Segment[]{cur, floor};
                Segment ceiling = set.ceiling(cur);
                if (ceiling != null && isCrossOrTouchIntersect(cur, ceiling))
                    return new Segment[]{cur, ceiling};
                set.add(cur);
            } else {
                Segment lower = set.lower(cur);
                Segment higher = set.higher(cur);
                if (lower != null && higher != null && isCrossOrTouchIntersect(lower, higher))
                    return new Segment[]{lower, higher};
                set.remove(cur);
            }
        }
        return null;
    }

    public static class Segment {
        final int x1, y1, x2, y2;

        public Segment(int x1, int y1, int x2, int y2) {
            if (x1 < x2 || x1 == x2 && y1 < y2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            } else {
                this.x1 = x2;
                this.y1 = y2;
                this.x2 = x1;
                this.y2 = y1;
            }
        }
    }

    static final Comparator<Segment> segmentComparator = (a, b) -> {
        if (a.x1 < b.x1) {
            long v = cross(a.x1, a.y1, a.x2, a.y2, b.x1, b.y1);
            if (v != 0)
                return v > 0 ? -1 : 1;
        } else if (a.x1 > b.x1) {
            long v = cross(b.x1, b.y1, b.x2, b.y2, a.x1, a.y1);
            if (v != 0)
                return v < 0 ? -1 : 1;
        }
        return Integer.compare(a.y1, b.y1);
    };

    static class Event {
        final int x, y;
        final int type;
        final Segment segment;

        public Event(int x, int y, int type, Segment segment) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.segment = segment;
        }
    }

    static final Comparator<Event> eventComparator =
            Comparator.<Event>comparingInt(e -> e.x).thenComparingInt(e -> -e.type).thenComparingInt(e -> e.y);

    static long cross(long ax, long ay, long bx, long by, long cx, long cy) {
        return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
    }

    static boolean isCrossOrTouchIntersect(Segment s1, Segment s2) {
        long x1 = s1.x1;
        long y1 = s1.y1;
        long x2 = s1.x2;
        long y2 = s1.y2;
        long x3 = s2.x1;
        long y3 = s2.y1;
        long x4 = s2.x2;
        long y4 = s2.y2;
        if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2)
                || Math.max(y1, y2) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2))
            return false;
        long z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        long z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1);
        long z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3);
        long z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3);
        return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0);
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random();
        for (int step = 0; step < 100_000; step++) {
            int n = rnd.nextInt(100);
            Segment[] s = new Segment[n];
            for (int i = 0; i < n; i++) {
                int r = 20;
                int x1 = rnd.nextInt(r) - r / 2;
                int y1 = rnd.nextInt(r) - r / 2;
                int x2 = rnd.nextInt(r) - r / 2;
                int y2 = rnd.nextInt(r) - r / 2;
                s[i] = new Segment(x1, y1, x2, y2);
            }
            Segment[] intersection = findIntersection(s);
            boolean hasIntersection = hasIntersection(s);
            if (intersection == null == hasIntersection)
                throw new RuntimeException();
        }
    }

    static boolean hasIntersection(Segment[] s) {
        for (int i = 0; i < s.length; i++)
            for (int j = i + 1; j < s.length; j++)
                if (isCrossOrTouchIntersect(s[i], s[j]))
                    return true;
        return false;
    }
}
