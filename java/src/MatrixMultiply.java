import java.util.*;

public class MatrixMultiply {

    static final int INF = Integer.MAX_VALUE / 3;

    public static int solveIterative(int[] s) {
        int n = s.length - 1;
        int[][] p = new int[n][n];
        int[][] m = new int[n][n];
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i < n - (len - 1); i++) {
                int j = i + len - 1;
                m[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int v = m[i][k] + m[k + 1][j] + s[i] * s[k + 1] * s[j + 1];
                    if (m[i][j] > v) {
                        m[i][j] = v;
                        p[i][j] = k;
                    }
                }
            }
        }
        return m[0][n - 1];
    }

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

    public static int solveRecursive(int[] s) {
        int n = s.length - 1;
        int[][] cache = new int[n][n];
        for (int[] x : cache)
            Arrays.fill(x, INF);
        int[][] p = new int[n][n];
        return rec(0, n - 1, s, p, cache);
    }

    public static void main(String[] args) {
        Random rnd = new Random(1);
        for (int step = 0; step < 1000; step++) {
            int n = rnd.nextInt(6) + 2;
            int[] s = new int[n];
            for (int i = 0; i < n; i++) {
                s[i] = rnd.nextInt(10) + 1;
            }
            int res1 = solveIterative(s);
            int res2 = solveRecursive(s);
            if (res1 != res2) {
                System.out.println(res1 + " " + res2);
                System.out.println(Arrays.toString(s));
            }
        }
    }
}
