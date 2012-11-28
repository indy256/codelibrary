public class LinearEqaulity {

	// number of integer solutions of equality a[0]*x1 + ... + a[n-1]*xn = b, xi >= 0
	public static long countSolutions(int[] a, int b) {
		long[] dp = new long[b + 1];
		dp[0] = 1;
		for (int i = 0; i < a.length; i++) {
			for (int j = a[i]; j <= b; j++) {
				dp[j] += dp[j - a[i]];
			}
		}
		return dp[b];
	}

	public static void main(String[] args) {
		System.out.println(5 == countSolutions(new int[] { 1, 2, 3 }, 5));
	}
}
