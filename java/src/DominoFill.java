public class DominoFill {

	public static void main(String[] args) {
		System.out.println(method1(4, 3));
	}

	public static int method1(int R, int C) {
		int[][][] dp = new int[R][C][1 << C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				for (int mask = 0; mask < 1 << C; mask++) {
					int prev = c > 0 ? dp[r][c - 1][mask] : r > 0 ? dp[r - 1][C - 1][mask] : mask == (1 << C) - 1 ? 1 : 0;
					dp[r][c][mask ^ 1 << c] += prev;
					if ((mask & 1 << c) != 0 && (mask & 1 << (c + 1)) != 0) {
						dp[r][c + 1][mask] += prev;
					}
				}
			}
		}
		return dp[R - 1][C - 1][(1 << C) - 1];
	}

	public static int method2(int R, int C) {
		int[][][] dp = new int[R][C][1 << C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				for (int mask = 0; mask < 1 << C; mask++) {
					int prev = c > 0 ? dp[r][c - 1][mask] : r > 0 ? dp[r - 1][C - 1][mask] : mask == (1 << C) - 1 ? 1 : 0;
					dp[r][c][mask ^ 1 << c] += prev;
					if ((mask & 1 << c) != 0 && (mask & 1 << (c + 1)) != 0) {
						dp[r][c + 1][mask] += prev;
					}
				}
			}
		}
		return dp[R - 1][C - 1][(1 << C) - 1];
	}
}
