package misc;

import java.util.*;

public class MonotonicApproximation {
    // returns minimum sum |a[i]-b[i]| where b is non-strictly increasing array
    // see http://codeforces.com/blog/entry/47821
    public static long monotonicApproximation(int[] a) {
        int n = a.length;
        long res = 0;
        PriorityQueue<Integer> q = new PriorityQueue<>(Comparator.reverseOrder());
        q.add(a[0]);
        for (int i = 1; i < n; i++) {
            int x = a[i];
            q.add(x);
            if (q.peek() > x) {
                res += q.peek() - x;
                q.remove();
                q.add(x);
            }
        }
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
        for (int i = 1; i < n; i++) cur[i] = Math.min(cur[i - 1], Math.abs(a[0] - b[i]));
        for (int i = 1; i < n; i++) {
            next[0] = Math.abs(a[i] - b[0]) + cur[0];
            for (int j = 1; j < n; j++) next[j] = Math.min(next[j - 1], Math.abs(a[i] - b[j]) + cur[j]);
            System.arraycopy(next, 0, cur, 0, n);
        }
        return cur[n - 1];
    }
}
