import java.util.*;

public class Partition {

	static Random rnd = new Random(1);

	public static int randomizedPartition(int[] a, int low, int high) {
		swap(a, low + rnd.nextInt(high - low), high - 1);
		int separator = a[high - 1];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (a[j] <= separator) {
				swap(a, ++i, j);
			}
		}
		return i;
	}

	public static int partition(int[] a, int low, int high) {
		swap(a, (low + high) >>> 1, high - 1);
		int separator = a[high - 1];
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (a[j] <= separator) {
				swap(a, ++i, j);
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
		for (int step = 0; step < 100_000; step++) {
			int n = rnd.nextInt(10) + 1;
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = rnd.nextInt(10);
			int[] a1 = a.clone();
			check(a1, randomizedPartition(a1, 0, n));
			int[] a2 = a.clone();
			check(a2, partition(a2, 0, n));
		}
	}

	static void check(int[] a, int k) {
		if (k < 0 || k >= a.length)
			throw new RuntimeException();
		for (int i = 0; i < a.length; i++)
			if (i < k && a[i] > a[k] || i > k && a[i] < a[k])
				throw new RuntimeException();
	}
}
