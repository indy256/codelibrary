import java.util.*;

public class NthElement2 {

	static Random rnd = new Random(1);

	// See: http://www.cplusplus.com/reference/algorithm/nth_element
	public static void nth_element(int[] a, int low, int high, int n) {
		if (low + 1 == high)
			return;
		int q = randomizedPartition(a, low, high);
		int k = q - low;
		if (n < k)
			nth_element(a, low, q, n);
		else if (n > k)
			nth_element(a, q + 1, high, n - k - 1);
	}

	static int randomizedPartition(int[] a, int low, int high) {
		swap(a, low + rnd.nextInt(high - low), high - 1);
		int i = low - 1;
		for (int j = low, separator = a[high - 1]; j < high; j++)
			if (a[j] <= separator)
				swap(a, ++i, j);
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
			int n = rnd.nextInt(3) + 1;
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = rnd.nextInt(5);
			int k = rnd.nextInt(n);
			nth_element(a, 0, n, k);
			int[] s = a.clone();
			Arrays.sort(s);
			if (a[k] != s[k])
				throw new RuntimeException();
			for (int i = 0; i < n; i++)
				if (i < k && a[i] > a[k] || i > k && a[i] < a[k])
					throw new RuntimeException();
		}
	}
}
