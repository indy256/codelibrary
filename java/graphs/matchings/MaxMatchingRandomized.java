package graphs.matchings;

import java.util.Random;

public class MaxMatchingRandomized {

    static final int MOD = 29989;

    static int pow(int a, int b) {
        int res = 1;
        for (; b > 0; b >>= 1) {
            if ((b & 1) != 0)
                res = res * a % MOD;
            a = a * a % MOD;
        }
        return res;
    }

    static int rank(int[][] a) {
        int r = 0;
        int n = a.length;
        for (int j = 0; j < n; j++) {
            int k;
            for (k = r; k < n && a[k][j] == 0; k++)
                ;
            if (k == n)
                continue;

            int[] t = a[r];
            a[r] = a[k];
            a[k] = t;

            int inv = pow(a[r][j], MOD - 2);
            for (int i = j; i < n; i++)
                a[r][i] = a[r][i] * inv % MOD;

            for (int u = r + 1; u < n; u++)
                for (int v = j + 1; v < n; v++)
                    a[u][v] = (a[u][v] - a[r][v] * a[u][j] % MOD + MOD) % MOD;
            ++r;
        }
        return r;
    }

    public static int maxMatching(boolean[][] d) {
        int n = d.length;
        int[][] a = new int[n][n];
        Random rnd = new Random(1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (d[i][j]) {
                    a[i][j] = rnd.nextInt(MOD - 1) + 1;
                    a[j][i] = MOD - a[i][j];
                }
            }
        }
        return rank(a) / 2;
    }

    // Usage example
    public static void main(String[] args) {
        int res = maxMatching(new boolean[][]{{false, true}, {true, false}});
        System.out.println(1 == res);
    }
}
