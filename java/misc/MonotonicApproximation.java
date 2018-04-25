package misc;

import java.util.*;

public class MonotonicApproximation {

    // finds array b that minimizes sum |a[i]-b[i]|
    // see http://codeforces.com/blog/entry/47821
    public static long monotonicApproximation(int[] a) {
        int n = a.length;
        int[] t = new int[n];
        SortedMap<Integer, Integer> m = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            Integer v = m.get(a[i]);
            if (v == null) v = 0;
            m.put(a[i], v + 2);
            int last = m.lastKey();
            t[i] = last;
            int count = m.get(last);
            if (count > 1)
                m.put(last, count - 1);
            else
                m.remove(last);
        }
        int[] b = new int[n];
        b[n - 1] = t[n - 1];
        for (int i = n - 2; i >= 0; i--)
            b[i] = Math.min(t[i], b[i + 1]);
        long res = 0;
        for (int i = 0; i < n; i++)
            res += Math.abs(a[i] - b[i]);
        return res;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 10000; step++) {
            int n = rnd.nextInt(100) + 1;
            int[] a = rnd.ints(n, 0, 1000).toArray();
            long res1 = monotonicApproximation(a);
            long res2 = slowMonotonicApproximation(a);
            if (res1 != res2)
                throw new RuntimeException(res1 + " " + res2);
        }
    }

    static long slowMonotonicApproximation(int[] a) {
        int n = a.length;
        int[] b = a.clone();
        Arrays.sort(b);
        long[] cur = new long[n];
        long[] next = new long[n];
        cur[0] = Math.abs(a[0] - b[0]);
        for (int i = 1; i < n; i++)
            cur[i] = Math.min(cur[i - 1], Math.abs(a[0] - b[i]));
        for (int i = 1; i < n; i++) {
            next[0] = Math.abs(a[i] - b[0]) + cur[0];
            for (int j = 1; j < n; j++)
                next[j] = Math.min(next[j - 1], Math.abs(a[i] - b[j]) + cur[j]);
            System.arraycopy(next, 0, cur, 0, n);
        }
        return cur[n - 1];
    }
}