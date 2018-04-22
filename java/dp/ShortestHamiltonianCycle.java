package dp;

import java.util.Arrays;

public class ShortestHamiltonianCycle {

    public static int getShortestHamiltonianCycle(int[][] dist) {
        int n = dist.length;
        int[][] dp = new int[1 << n][n];
        for (int[] d : dp)
            Arrays.fill(d, Integer.MAX_VALUE / 2);
        dp[1][0] = 0;
        for (int mask = 1; mask < 1 << n; mask += 2) {
            for (int i = 1; i < n; i++) {
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
        for (int i = 1; i < n; i++) {
            res = Math.min(res, dp[(1 << n) - 1][i] + dist[i][0]);
        }

        // reconstruct path
        int cur = (1 << n) - 1;
        int[] order = new int[n];
        int last = 0;
        for (int i = n - 1; i >= 1; i--) {
            int bj = -1;
            for (int j = 1; j < n; j++) {
                if ((cur & 1 << j) != 0 && (bj == -1 || dp[cur][bj] + dist[bj][last] > dp[cur][j] + dist[j][last])) {
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
        int[][] dist = {{0, 1, 10, 1, 10}, {1, 0, 10, 10, 1}, {10, 10, 0, 1, 1}, {1, 10, 1, 0, 10},
                {10, 1, 1, 10, 0}};
        System.out.println(5 == getShortestHamiltonianCycle(dist));
    }
}
