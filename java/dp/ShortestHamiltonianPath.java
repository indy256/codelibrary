package dp;

import java.util.Arrays;

public class ShortestHamiltonianPath {

    public static int getShortestHamiltonianPath(int[][] dist) {
        int n = dist.length;
        int[][] dp = new int[1 << n][n];
        for (int[] d : dp)
            Arrays.fill(d, Integer.MAX_VALUE / 2);
        for (int i = 0; i < n; i++)
            dp[1 << i][i] = 0;
        for (int mask = 0; mask < 1 << n; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & 1 << i) != 0) {
                    for (int j = 0; j < n; j++) {
                        if ((mask & 1 << j) != 0) {
                            dp[mask][i] = Math.min(dp[mask][i], dp[mask ^ (1 << i)][j] + dist[j][i]);
                        }
                    }
                }
            }
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            res = Math.min(res, dp[(1 << n) - 1][i]);
        }

        // reconstruct path
        int cur = (1 << n) - 1;
        int[] order = new int[n];
        int last = -1;
        for (int i = n - 1; i >= 0; i--) {
            int bj = -1;
            for (int j = 0; j < n; j++) {
                if ((cur & 1 << j) != 0
                        && (bj == -1
                        || dp[cur][bj] + (last == -1 ? 0 : dist[bj][last]) > dp[cur][j] + (last == -1 ? 0 : dist[j][last]))) {
                    bj = j;
                }
            }
            order[i] = bj;
            cur ^= 1 << bj;
            last = bj;
        }
        System.out.println(Arrays.toString(order));
        return res;
    }

    // Usage example
    public static void main(String[] args) {
        int[][] dist = {{8, 1, 6}, {3, 5, 7}, {4, 9, 2}};
        System.out.println(5 == getShortestHamiltonianPath(dist));
    }
}
