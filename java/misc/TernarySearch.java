package misc;

import java.util.Random;
import java.util.function.*;

// https://en.wikipedia.org/wiki/Ternary_search
public class TernarySearch {

    // finds maximum of strictly increasing and then strictly decreasing function
    public static double ternarySearch(DoubleUnaryOperator f, double lo, double hi) {
        for (int step = 0; step < 1000; step++) {
            double m1 = lo + (hi - lo) / 3;
            double m2 = hi - (hi - lo) / 3;
            if (f.applyAsDouble(m1) < f.applyAsDouble(m2))
                lo = m1;
            else
                hi = m2;
        }
        return (lo + hi) / 2;
    }

    // finds maximum of strictly increasing and then strictly decreasing function
    public static int ternarySearch(IntUnaryOperator f, int fromInclusive, int toInclusive) {
        int lo = fromInclusive;
        int hi = toInclusive;
        while (hi > lo + 2) {
            int m1 = lo + (hi - lo) / 3;
            int m2 = hi - (hi - lo) / 3;
            if (f.applyAsInt(m1) < f.applyAsInt(m2))
                lo = m1;
            else
                hi = m2;
        }
        int res = lo;
        for (int i = lo + 1; i <= hi; i++)
            if (f.applyAsInt(res) < f.applyAsInt(i))
                res = i;
        return res;
    }

    public static int ternarySearch2(IntUnaryOperator f, int fromInclusive, int toInclusive) {
        int lo = fromInclusive - 1;
        int hi = toInclusive;
        while (hi - lo > 1) {
            int mid = (lo + hi) >>> 1;
            if (f.applyAsInt(mid) < f.applyAsInt(mid + 1)) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    // random tests
    public static void main(String[] args) {
        System.out.println(ternarySearch((DoubleUnaryOperator) x -> -(x - 2) * (x - 2), -10, 10));

        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(20) + 1;
            int p = rnd.nextInt(n);
            int[] a = new int[n];
            final int range = 10;
            a[p] = rnd.nextInt(range);
            for (int i = p - 1; i >= 0; i--)
                a[i] = a[i + 1] - rnd.nextInt(range) - 1;
            for (int i = p + 1; i < n; i++)
                a[i] = a[i - 1] - rnd.nextInt(range) - 1;
            int res1 = ternarySearch((IntUnaryOperator) i -> a[i], 0, a.length - 1);
            int res2 = ternarySearch2(i -> a[i], 0, a.length - 1);
            if (p != res1 || p != res2)
                throw new RuntimeException();
        }
    }
}
