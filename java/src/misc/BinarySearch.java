package misc;

import java.util.*;
import java.util.function.*;

public class BinarySearch {

    // 000[1]11
    // warning: overflows in lines 1-4
    // invariant: f[lo] == false, f[hi] == true
    public static int binarySearchFirstTrueSimple(IntPredicate f, int fromInclusive, int toInclusive) {
        int lo = fromInclusive - 1;
        int hi = toInclusive + 1;
        while (hi - lo > 1) {
            int mid = (lo + hi) / 2;
            if (!f.test(mid)) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    // 000[1]11
    // correct binary search
    public static int binarySearchFirstTrue(IntPredicate f, int fromInclusive, int toExclusive) {
        int lo = fromInclusive;
        int hi = toExclusive;
        while (lo < hi) {
            // int mid = lo + ((hi - lo) >>> 1);
            int mid = (lo & hi) + ((lo ^ hi) >> 1);
            if (!f.test(mid)) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    public static double binarySearch(DoublePredicate f, double lo, double hi) {
        for (int step = 0; step < 1000; step++) {
            double mid = (lo + hi) / 2;
            if (!f.test(mid)) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        return hi;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 100_000; step++) {
            int n = rnd.nextInt(20);
            boolean[] b = new boolean[n];
            int firstTrue = rnd.nextInt(n + 1);
            Arrays.fill(b, firstTrue, n, true);
            int res1 = binarySearchFirstTrueSimple(i -> b[i], 0, n - 1);
            int res2 = binarySearchFirstTrue(i -> b[i], 0, n);
            if (res1 != firstTrue || res1 != res2)
                throw new RuntimeException();
        }

        System.out.println(Math.sqrt(2) == binarySearch(x -> x * x >= 2, 0, 2));
    }
}
