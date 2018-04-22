package dp;

import java.util.*;

public class MatrixChainMultiply {

    public static int solveIterative(int[] s) {
        int n = s.length - 1;
        int[][] p = new int[n][n];
        int[][] m = new int[n][n];
        for (int len = 2; len <= n; len++) {
            for (int a = 0; a + len <= n; a++) {
                int b = a + len - 1;
                m[a][b] = Integer.MAX_VALUE;
                for (int c = a; c < b; c++) {
                    int v = m[a][c] + m[c + 1][b] + s[a] * s[c + 1] * s[b + 1];
                    if (m[a][b] > v) {
                        m[a][b] = v;
                        p[a][b] = c;
                    }
                }
            }
        }
        return m[0][n - 1];
    }

    public static int solveRecursive(int[] s) {
        int n = s.length - 1;
        int[][] cache = new int[n][n];
        for (int[] x : cache)
            Arrays.fill(x, INF);
        int[][] p = new int[n][n];
        return rec(0, n - 1, s, p, cache);
    }

    static final int INF = Integer.MAX_VALUE / 3;

    static int rec(int i, int j, int[] s, int[][] p, int[][] cache) {
        if (i == j)
            return 0;
        int res = cache[i][j];
        if (res != INF)
            return res;
        for (int k = i; k < j; k++) {
            int v = rec(i, k, s, p, cache) + rec(k + 1, j, s, p, cache) + s[i] * s[k + 1] * s[j + 1];
            if (res > v) {
                res = v;
                p[i][j] = k;
            }
        }
        return cache[i][j] = res;
    }

    // test
    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(6) + 2;
            int[] s = rnd.ints(n, 1, 11).toArray();
            int res1 = solveIterative(s);
            int res2 = solveRecursive(s);
            if (res1 != res2) {
                System.out.println(res1 + " " + res2);
                System.out.println(Arrays.toString(s));
            }
        }
    }
}
