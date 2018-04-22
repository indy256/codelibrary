package backtracking;

import java.util.Random;

public class MisWeighted {

    public static int mis(long[] g, long unused, int[] weights) {
        if (unused == 0)
            return 0;
        int v = -1;
        for (int u = Long.numberOfTrailingZeros(unused); u < g.length; u += Long.numberOfTrailingZeros(unused >> (u + 1)) + 1)
            if (v == -1 || Long.bitCount(g[v] & unused) > Long.bitCount(g[u] & unused))
                v = u;
        int res = 0;
        long nv = g[v] & unused;
        for (int y = Long.numberOfTrailingZeros(nv); y < g.length; y += Long.numberOfTrailingZeros(nv >> (y + 1)) + 1)
            res = Math.max(res, weights[y] + mis(g, unused & ~g[y], weights));
        return res;
    }

    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(16) + 1;
            long[] g = new long[n];
            int[] weights = new int[n];
            for (int i = 0; i < n; i++) {
                weights[i] = rnd.nextInt(1000);
                // for convenience of mis()
                g[i] |= 1L << i;
            }
            for (int i = 0; i < n; i++)
                for (int j = 0; j < i; j++)
                    if (rnd.nextBoolean()) {
                        g[i] |= 1L << j;
                        g[j] |= 1L << i;
                    }
            int res1 = mis(g, (1L << n) - 1, weights);
            int res2 = misSlow(g, weights);
            if (res1 != res2)
                throw new RuntimeException();
        }
    }

    static int misSlow(long[] g, int[] weights) {
        int res = 0;
        int n = g.length;
        for (int set = 0; set < 1 << n; set++) {
            boolean ok = true;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < i; j++)
                    ok &= (set & (1 << i)) == 0 || (set & (1 << j)) == 0 || (g[i] & (1L << j)) == 0;
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
