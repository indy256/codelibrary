import java.util.*;

public class NthElement {

	// See: http://www.cplusplus.com/reference/algorithm/nth_element
	public static void nth_element(int[] a, int low, int high, int n) {
		while (true) {
			int k = randomizedPartition(a, low, high);
			if (n < k)
				high = k;
			else if (n > k)
				low = k + 1;
			else
				return;
		}
	}

	static Random rnd = new Random();

	static int randomizedPartition(int[] a, int low, int high) {
		swap(a, low + rnd.nextInt(high - low), high - 1);
		int separator = a[high - 1];
		int i = low - 1;
		for (int j = low; j < high; j++)
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
		for (int step = 0; step < 100_000; step++) {
			int n = rnd.nextInt(10) + 1;
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = rnd.nextInt(10);
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
