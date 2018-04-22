package dp;

public class DominoFill {

    /**
     * .....
     * ..234
     * 01
     */
    public static int method1(int R, int C) {
        int[] prev = new int[1 << C];
        prev[(1 << C) - 1] = 1;

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                int[] cur = new int[1 << C];
                for (int mask = 0; mask < 1 << C; mask++) {
                    if ((mask & (1 << c)) != 0) {
                        cur[mask ^ (1 << c)] += prev[mask]; // do nothing

                        if (c > 0 && (mask & (1 << (c - 1))) == 0) {
                            cur[mask | (1 << (c - 1))] += prev[mask]; // horizontal
                        }
                    } else {
                        cur[mask | (1 << c)] += prev[mask];  // vertical
                    }
                }
                prev = cur;
            }
        }

        return prev[(1 << C) - 1];
    }

    /**
     * .....
     * ..012
     * 34
     */
    public static int method2(int R, int C) {
        int[] prev = new int[1 << C];
        prev[(1 << C) - 1] = 1;

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                int[] cur = new int[1 << C];
                for (int mask = 0; mask < 1 << C; mask++) {
                    int nmask = (mask << 1) & ((1 << C) - 1);
                    if ((mask & (1 << (C - 1))) != 0) {
                        cur[nmask] += prev[mask]; // do nothing

                        if (c > 0 && ((mask & 1) == 0)) {
                            cur[nmask | 3] += prev[mask]; // horizontal
                        }
                    } else {
                        cur[nmask | 1] += prev[mask]; // vertical
                    }
                }
                prev = cur;
            }
        }

        return prev[(1 << C) - 1];
    }

    // random test
    public static void main(String[] args) {
        for (int r = 1; r < 10; r++) {
            for (int c = 1; c < 10; c++) {
                int res1 = method1(r, c);
                int res2 = method2(r, c);
                if (res1 != res2)
                    throw new RuntimeException();
            }
        }
    }

    public static int method0(int R, int C) {
        int[][][] dp = new int[R + 1][C][1 << C];
        dp[0][C - 1][(1 << C) - 1] = 1;

        for (int r = 1; r <= R; r++) {
            for (int c = 0; c < C; c++) {
                int[] prev = c > 0 ? dp[r][c - 1] : dp[r - 1][C - 1];
                for (int mask = 0; mask < 1 << C; mask++) {
                    if ((mask & (1 << c)) != 0) {
                        dp[r][c][mask ^ (1 << c)] += prev[mask]; // do nothing

                        if (c > 0 && (mask & (1 << (c - 1))) == 0) {
                            dp[r][c][mask | (1 << (c - 1))] += prev[mask]; // horizontal
                        }
                    } else {
                        dp[r][c][mask | (1 << c)] += prev[mask];  // vertical
                    }
                }
            }
        }

        return dp[R][C - 1][(1 << C) - 1];
    }
}
