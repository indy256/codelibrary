package backtracking;

import java.util.Random;

// https://en.wikipedia.org/wiki/Bron–Kerbosch_algorithm
public class BronKerbosh {

    public static int maximumCliqueWeight(long[] g, long cur, long allowed, long forbidden, int[] weights) {
        if (allowed == 0 && forbidden == 0) {
            int res = 0;
            for (int u = Long.numberOfTrailingZeros(cur); u < g.length; u += Long.numberOfTrailingZeros(cur >> (u + 1)) + 1)
                res += weights[u];
            return res;
        }
        int res = -1;
        int pivot = Long.numberOfTrailingZeros(allowed | forbidden); // todo: select pivot to minimize bitCount(z)
        long z = allowed & ~g[pivot];
        for (int u = Long.numberOfTrailingZeros(z); u < g.length; u += Long.numberOfTrailingZeros(z >> (u + 1)) + 1) {
            res = Math.max(res, maximumCliqueWeight(g, cur | (1L << u), allowed & g[u], forbidden & g[u], weights));
            allowed ^= 1L << u;
            forbidden |= 1L << u;
        }
        return res;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(16) + 1;
            long[] g = new long[n];
            int[] weights = rnd.ints(n, 0, 1000).toArray();
            for (int i = 0; i < n; i++)
                for (int j = 0; j < i; j++)
                    if (rnd.nextBoolean()) {
                        g[i] |= 1L << j;
                        g[j] |= 1L << i;
                    }
            int res1 = maximumCliqueWeight(g, 0, (1L << n) - 1, 0, weights);
            int res2 = maximumCliqueWeightSlow(g, weights);
            if (res1 != res2)
                throw new RuntimeException();
        }
    }

    static int maximumCliqueWeightSlow(long[] g, int[] weights) {
        int res = 0;
        int n = g.length;
        for (int set = 0; set < 1 << n; set++) {
            boolean ok = true;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < i; j++)
                    ok &= (set & (1 << i)) == 0 || (set & (1 << j)) == 0 || (g[i] & (1 << j)) != 0;
            if (ok) {
                int cur = 0;
                for (int i = 0; i < n; i++)
                    if ((set & (1 << i)) != 0)
                        cur += weights[i];
                res = Math.max(res, cur);
            }
        }
        return res;
    }
}
