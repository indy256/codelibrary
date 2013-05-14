package experimental;

public class TicTacToe {

	static boolean win(char[] p, char v) {
		return p[0] == v && p[1] == v && p[2] == v || p[3] == v && p[4] == v && p[5] == v || p[6] == v && p[7] == v
				&& p[8] == v || p[0] == v && p[3] == v && p[6] == v || p[1] == v && p[4] == v && p[7] == v || p[2] == v
				&& p[5] == v && p[8] == v || p[0] == v && p[4] == v && p[8] == v || p[2] == v && p[4] == v && p[6] == v;
	}

	static int[] solve(char[] gamePosition) {
		if (win(gamePosition, 'X') || win(gamePosition, 'O'))
			return new int[] { -1, 0 };
		int xCnt = 0;
		int oCnt = 0;
		for (char a : gamePosition) {
			if (a == 'X')
				++xCnt;
			if (a == 'O')
				++oCnt;
		}
		if (xCnt + oCnt == 9)
			return new int[] { 0, 0 };
		int[] bestOutcome = { Integer.MIN_VALUE, -1 };
		for (int i = 0; i < 9; i++) {
			if (gamePosition[i] == '.') {
				gamePosition[i] = xCnt > oCnt ? 'O' : 'X';
				int[] enemyOutcome = solve(gamePosition);
				if (bestOutcome[1] == -1 || bestOutcome[0] < -enemyOutcome[0]) {
					bestOutcome[0] = -enemyOutcome[0];
					bestOutcome[1] = i;
				}
				gamePosition[i] = '.';
			}
		}
		return bestOutcome;
	}

	public static void main(String[] args) {
		String line1 = "XO.";
		String line2 = "...";
		String line3 = "...";
		int[] bestOutcome = solve((line1 + line2 + line3).toCharArray());
		final int result = bestOutcome[0];
		final int move = bestOutcome[1];
		System.out.println((result == 1 ? "WIN" : result == -1 ? "LOSE" : "DRAW") + " bestMove:" + move);
	}
}
