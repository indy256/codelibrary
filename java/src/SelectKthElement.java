import java.util.*;

public class SelectKthElement {

	static Random rnd = new Random(1);

	static void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	static int randomizedPartition(int[] a, int low, int high) {
		swap(a, low + rnd.nextInt(high - low), high - 1);
		int x = a[high - 1];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (a[j] <= x) {
				++i;
				swap(a, i, j);
			}
		}
		return i;
	}

	public static int randomizedSelect(int[] a, int low, int high, int i) {
		if (low == high - 1)
			return low;
		int q = randomizedPartition(a, low, high);
		int k = q - low;
		if (i < k)
			return randomizedSelect(a, low, q, i);
		if (i > k)
			return randomizedSelect(a, q + 1, high, i - k - 1);
		return q;
	}

	static int[] getRandomPermutation(int n, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++)
			res[i] = i;
		for (int i = res.length - 1; i > 0; i--)
			swap(res, i, rnd.nextInt(i + 1));
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		for (int step = 0; step < 100000; step++) {
			int n = rnd.nextInt(10) + 1;
			int[] p = getRandomPermutation(n, rnd);
			int k = rnd.nextInt(n);
			int res = randomizedSelect(p, 0, n, k);
			if (res != k) {
				System.err.println(res + " " + k);
			}
		}
	}
}
