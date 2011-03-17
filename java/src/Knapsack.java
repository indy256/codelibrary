import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Knapsack {
	int[] knapsack(int[] weights, int[] costs, int capacity) {
		int[] dp = new int[capacity + 1];
		Arrays.fill(dp, -1);
		dp[0] = 0;
		int[][] last = new int[weights.length][capacity + 1];
		for (int[] x : last) {
			Arrays.fill(x, -1);
		}
		for (int i = 0; i < weights.length; i++) {
			for (int j = capacity; j >= weights[i]; j--) {
				if (dp[j - weights[i]] >= 0) {
					int nv = dp[j - weights[i]] + costs[i];
					if (dp[j] < nv) {
						dp[j] = nv;
						last[i][j] = i;
					}
				}
			}
		}
		int j = 0;
		int bestV = 0;
		for (int i = capacity; i >= 1; i--) {
			if (bestV < dp[i]) {
				bestV = dp[i];
				j = i;
			}
		}

		// List<Book> res = new ArrayList<Book>();
		// for (int i = fbooks.size() - 1; j != 0; --i) {
		// if (last[i][j] != -1) {
		// Book b = fbooks.get(last[i][j]);
		// res.add(b);
		// j -= b.w;
		// }
		// }
		return null;
	}

}
