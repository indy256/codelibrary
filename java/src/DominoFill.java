public class DominoFill {

	public static void main(String[] args) {
		int R = 4;
		int C = 3;
		int[][][] dp = new int[R][C][1 << C];
		for (int r = 0; r < R; r++) {
			for (int c = 0; c < C; c++) {
				for (int mask = 0; mask < 1 << C; mask++) {
					int cur = c > 0 ? dp[r][c - 1][mask] : r > 0 ? dp[r - 1][C - 1][mask] : mask == (1 << C) - 1 ? 1 : 0;
					dp[r][c][mask ^ (1 << c)] += cur;
					if (c + 1 < C && (mask & (1 << c)) != 0 && (mask & (1 << (c + 1))) != 0) {
						dp[r][c + 1][mask] += cur;
					}
				}
			}
		}
		System.out.println(dp[R - 1][C - 1][(1 << C) - 1]);
	}
}
