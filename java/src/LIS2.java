import java.util.*;

public class LIS2 {

	static int upper_bound(int[] a, int key) {
		int lo = -1;
		int hi = a.length;
		while (hi - lo > 1) {
			int mid = (lo + hi) / 2;
			int midVal = a[mid];
			if (midVal <= key) {
				lo = mid;
			} else {
				hi = mid;
			}
		}
		return hi;
	}

	public static int[] getLIS(int[] x) {
		int n = x.length;
		int[] pred = new int[n];
		int[] antichains = new int[n + 1];
		Arrays.fill(antichains, Integer.MAX_VALUE);
		antichains[0] = Integer.MIN_VALUE;
		int[] tail = new int[n + 1];
		tail[0] = -1;
		for (int i = 0; i < n; i++) {
			int j = upper_bound(antichains, x[i]);
			if (antichains[j - 1] < x[i]) {
				antichains[j] = x[i];
				tail[j] = i;
				pred[i] = tail[j - 1];
			}
		}
		for (int pos = n;; pos--) {
			if (antichains[pos] != Integer.MAX_VALUE) {
				int[] res = new int[pos];
				for (int j = tail[pos]; j != -1; j = pred[j]) {
					res[--pos] = x[j];
				}
				return res;
			}
		}
	}

	// Usage example
	public static void main(String[] args) {
		int[] a = { 1, 5, 4, 2, 3, 7, 6 };
		int[] lis = getLIS(a);
		System.out.println(Arrays.toString(lis));
	}
}
