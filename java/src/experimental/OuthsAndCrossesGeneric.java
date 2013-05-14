package experimental;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OuthsAndCrossesGeneric {
	public static void main(String[] args) {
		String s1 = "XOX";
		String s2 = "...";
		String s3 = "..O";
		char[] t = (s1 + s2 + s3).toCharArray();
		System.out.println(dfs(new State(t, 0))[1] + 1);
	}

	static class State {
		char[] t;
		int player;

		public State(char[] t, int player) {
			this.t = t.clone();
			this.player = player;
		}

		public boolean equals(Object obj) {
			State s = (State) obj;
			if (!Arrays.equals(t, s.t))
				return false;
			return player == s.player;
		}

		public int hashCode() {
			int h = player;
			for (char x : t) {
				h = h * 3137 + x;
			}
			return h;
		}
	}

	static Map<State, int[]> cache = new HashMap<>();

	static int[] dfs(State s) {
		if (cache.containsKey(s))
			return cache.get(s);

		int outcome = getOutcome(s);
		if (outcome != Integer.MIN_VALUE) {
			return new int[] { outcome, -1 };
		}
		int bestOutcome = Integer.MIN_VALUE;
		int bestMove = -1;
		for (int i = 0; i < 9; i++) {
			if (s.t[i] == '.') {
				s.t[i] = "XO".charAt(s.player);
				s.player = 1 - s.player;
				int v = -dfs(s)[0];
				s.t[i] = '.';
				s.player = 1 - s.player;
				if (bestOutcome < v) {
					bestOutcome = v;
					bestMove = i;
					if (bestOutcome == 1) {
						break;
					}
				}
			}
		}
		cache.put(new State(s.t, s.player), new int[] { bestOutcome, bestMove });
		return cache.get(s);
	}

	static int getOutcome(State s) {
		char[] t = s.t;
		if (new String(t).indexOf('.') == -1) {
			return 0;
		}
		for (char z : "XO".toCharArray()) {
			int z3 = z * 3;
			if ((t[0] + t[1] + t[2] == z3) || (t[3] + t[4] + t[5] == z3) || (t[6] + t[7] + t[8] == z3)
					|| (t[0] + t[3] + t[6] == z3) || (t[1] + t[4] + t[7] == z3) || (t[2] + t[5] + t[8] == z3)
					|| (t[0] + t[4] + t[8] == z3) || (t[2] + t[4] + t[6] == z3)) {
				return (z == 'X' ? 1 : -1) * (1 - 2 * s.player);
			}
		}
		return Integer.MIN_VALUE;
	}
}
