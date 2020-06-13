package test.matchings;

import graphs.matchings.MinBipartiteWeightedMatchingHungarian;
import java.util.*;

public class HungarianTest {
    // random test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int max = 9;
            int n = rnd.nextInt(max) + 1;
            int m = n + rnd.nextInt(max + 1 - n);
            int[][] a = new int[n + 1][m + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= m; j++) {
                    a[i][j] = rnd.nextInt(100_000) - 50_000;
                }
            }
            int res1 = MinBipartiteWeightedMatchingHungarian.minWeightPerfectMatching(a);
            int res2 = minWeightPerfectMatchingSlow(a);
            if (res1 != res2) {
                System.err.println(res1 + " " + res2);
            }
        }
    }

    static int minWeightPerfectMatchingSlow(int[][] a) {
        int n = a.length - 1;
        int m = a[0].length - 1;
        int[] dp = new int[1 << (n + m)];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        int res = Integer.MAX_VALUE;
        for (int mask = 0; mask < dp.length; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    for (int j = 0; j < m; j++) {
                        if ((mask & (1 << (j + n))) == 0) {
                            dp[mask | (1 << i) | (1 << (j + n))] =
                                Math.min(dp[mask | (1 << i) | (1 << (j + n))], dp[mask] + a[i + 1][j + 1]);
                            if (Integer.bitCount(mask & ((1 << n) - 1)) == n - 1)
                                res = Math.min(res, dp[mask | (1 << i) | (1 << (j + n))]);
                        }
                    }
                    break;
                }
            }
        }
        return res;
    }

    static int minimumWeightPerfectMatchingSlow2(int[][] a) {
        int n = a.length - 1;
        int m = a[0].length - 1;
        int res = Integer.MAX_VALUE;
        int[] p = new int[n];
        for (int i = 0; i < n; i++) p[i] = i;
        do {
            int cur = 0;
            for (int i = 0; i < p.length; i++) cur += a[i + 1][p[i] + 1];
            res = Math.min(res, cur);
        } while (nextArrangement(p, m));
        return res;
    }

    static boolean nextArrangement(int[] p, int n) {
        boolean[] used = new boolean[n];
        for (int x : p) {
            used[x] = true;
        }
        int m = p.length;
        for (int i = m - 1; i >= 0; i--) {
            used[p[i]] = false;
            for (int j = p[i] + 1; j < n; j++) {
                if (!used[j]) {
                    p[i++] = j;
                    used[j] = true;
                    for (int k = 0; k < n && i < m; k++) {
                        if (!used[k]) {
                            p[i++] = k;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
