import java.util.*;

public class LCS {

	public static int[] getLCS(int[] x, int[] y) {
		int m = x.length;
		int n = y.length;
		int[][] len = new int[m + 1][n + 1];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (x[i] == y[j]) {
					len[i + 1][j + 1] = len[i][j] + 1;
				} else {
					len[i + 1][j + 1] = Math.max(len[i + 1][j], len[i][j + 1]);
				}
			}
		}
		int cnt = len[m][n];
		int[] res = new int[cnt];
		for (int i = m - 1, j = n - 1; i >= 0 && j >= 0;) {
			if (x[i] == y[j]) {
				res[--cnt] = x[i];
				--i;
				--j;
			} else if (len[i + 1][j] > len[i][j + 1]) {
				--j;
			} else {
				--i;
			}
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		int[] x = { 1, 5, 4, 2, 3, 7, 6 };
		int[] y = { 2, 7, 1, 3, 5, 4, 6 };
		int[] lcs = getLCS(x, y);
		System.out.println(Arrays.toString(lcs));
	}
}
