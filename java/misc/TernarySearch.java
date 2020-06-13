package misc;

import java.util.Random;
import java.util.function.*;

// https://en.wikipedia.org/wiki/Ternary_search
// Finds the smallest i in [a, b] that maximizes f(i), assuming that f(a) < ... < f(i) ≥ ··· ≥ f(b)
public class TernarySearch {
    public static int ternarySearch(IntUnaryOperator f, int fromInclusive, int toInclusive) {
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

    public static int ternarySearch2(IntUnaryOperator f, int fromInclusive, int toInclusive) {
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

    public static double ternarySearchDouble(DoubleUnaryOperator f, double lo, double hi) {
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

    // random tests
    public static void main(String[] args) {
        System.out.println(ternarySearchDouble(x -> - (x - 2) * (x - 2), -10, 10));

        Random rnd = new Random(1);
        for (int step = 0; step < 10_000; step++) {
            int n = rnd.nextInt(20) + 1;
            int p = rnd.nextInt(n);
            int[] a = new int[n];
            final int range = 10;
            a[p] = rnd.nextInt(range);
            for (int i = p - 1; i >= 0; i--) a[i] = a[i + 1] - rnd.nextInt(range) - 1;
            for (int i = p + 1; i < n; i++) a[i] = a[i - 1] - rnd.nextInt(range);
            int res = ternarySearch(i -> a[i], 0, a.length - 1);
            int res2 = ternarySearch2(i -> a[i], 0, a.length - 1);
            if (p != res || p != res2)
                throw new RuntimeException();
        }
    }
}
