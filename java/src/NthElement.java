import java.util.*;

public class NthElement {

	static Random rnd = new Random(1);

	// analog of C++ nth_element()
	public static int nth_element(int[] a, int low, int high, int n) {
		if (low == high - 1)
			return low;
		int q = randomizedPartition(a, low, high);
		int k = q - low;
		if (n < k)
			return nth_element(a, low, q, n);
		if (n > k)
			return nth_element(a, q + 1, high, n - k - 1);
		return q;
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

	static void swap(int[] a, int i, int j) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	// Random test
	public static void main(String[] args) {
		for (int step = 0; step < 100000; step++) {
			int n = rnd.nextInt(10) + 1;
			int[] p = getRandomPermutation(n, rnd);
			int k = rnd.nextInt(n);
			int res = nth_element(p, 0, n, k);
			if (res != k) {
				System.err.println(res + " " + k);
			}
		}
	}

	static int[] getRandomPermutation(int n, Random rnd) {
		int[] res = new int[n];
		for (int i = 0; i < n; i++)
			res[i] = i;
		for (int i = res.length - 1; i > 0; i--)
			swap(res, i, rnd.nextInt(i + 1));
		return res;
	}
}
