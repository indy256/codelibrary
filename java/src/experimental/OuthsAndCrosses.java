package experimental;

public class OuthsAndCrosses {
	public static void main(String[] args) {
		String s1 = "XOX";
		String s2 = "...";
		String s3 = "..O";
		char[] t = (s1 + s2 + s3).toCharArray();
		System.out.println(dfs(t, 0)[1] + 1);
	}

	static int[] dfs(char[] t, int player) {
		int outcome = getOutcome(t, player);
		if (outcome != Integer.MIN_VALUE) {
			return new int[] { outcome, -1 };
		}
		int bestOutcome = Integer.MIN_VALUE;
		int bestMove = -1;
		for (int i = 0; i < 9; i++) {
			if (t[i] == '.') {
				t[i] = "XO".charAt(player);
				int v = -dfs(t, 1 - player)[0];
				t[i] = '.';
				if (bestOutcome < v) {
					bestOutcome = v;
					bestMove = i;
				}
			}
		}
		return new int[] { bestOutcome, bestMove };
	}

	static int getOutcome(char[] t, int player) {
		if (new String(t).indexOf('.') == -1) {
			return 0;
		}
		for (char z : "XO".toCharArray()) {
			int z3 = z * 3;
			if ((t[0] + t[1] + t[2] == z3) || (t[3] + t[4] + t[5] == z3) || (t[6] + t[7] + t[8] == z3)
					|| (t[0] + t[3] + t[6] == z3) || (t[1] + t[4] + t[7] == z3) || (t[2] + t[5] + t[8] == z3)
					|| (t[0] + t[4] + t[8] == z3) || (t[2] + t[4] + t[6] == z3)) {
				return (z == 'X' ? 1 : -1) * (1 - 2 * player);
			}
		}
		return Integer.MIN_VALUE;
	}
}
