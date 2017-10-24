import java.util.*;

public class ShortestHamiltonianCycle2 {
	public static int getShortestHamiltonianCycle(int[][] dist) {
		int n = dist.length;
		int[][] dp = new int[1 << n][n];
		for (int[] d : dp)
			Arrays.fill(d, Integer.MAX_VALUE / 2);
		dp[0][0] = 0;
		for (int mask = 0; mask < 1 << n; mask++) {
			for (int next = 0; next < n; next++) {
				if ((mask & 1 << next) == 0) {
					for (int cur = 0; cur < n; cur++) {
						dp[mask | 1 << next][next] = Math.min(dp[mask | 1 << next][next], dp[mask][cur] + dist[cur][next]);
					}
				}
			}
		}
		return dp[(1 << n) - 1][0];
	}

	// Usage example
	public static void main(String[] args) {
		int[][] dist = {{0, 1, 1}, {1, 0, 10}, {1, 10, 0}};
		int tourLength = getShortestHamiltonianCycle(dist);
		System.out.println(12 == tourLength);
	}
}
