
import java.io.PrintStream;
import java.util.*;

public class AntiQSort {
	public static void main(String[] args) throws Exception {
		long starttime = new Date().getTime();
		int[] scrambled = AntiQuicksort.killQuicksort(500000);
		System.out.println("Scrambling ended in " + (new Date().getTime() - starttime) + "ms with "
				+ AntiQuicksort.pivots.size() + "pivots");
		PrintStream out = new PrintStream(scrambled.length + ".txt");
		for (int i = 0; i < scrambled.length; i++) {
			out.println(scrambled[i]);
		}
		out.close();
		starttime = new Date().getTime();
		Arrays.sort(scrambled);
		System.out.println("Sorting ended in " + (new Date().getTime() - starttime) + "ms");
	}
}

class AntiQuicksort {
	static int max;
	static int[] positions;
	static ArrayList<Integer> pivots;

	public static int[] killQuicksort(int size) throws Exception {
		max = size;
		positions = new int[size];
		int[] sorted = new int[size];
		for (int i = 0; i < size; i++) {
			positions[i] = i;
			sorted[i] = Integer.MIN_VALUE;
		}
		pivots = new ArrayList<Integer>();
		sort1(sorted, 0, size);
		int[] result = new int[size];
		for (int i = 0; i < size; i++) {
			result[positions[i]] = sorted[i];
			if (result[positions[i]] == Integer.MIN_VALUE) {
				result[positions[i]] = max;
				max--;
			}
		}
		return result;
	}

	/**
	 * Sorts the specified sub-array of integers into ascending order.
	 */
	private static void sort1(int x[], int off, int len) throws Exception {
		// Insertion sort on smallest arrays
		if (len < 7) {
			for (int i = off; i < len + off; i++)
				for (int j = i; j > off && x[j - 1] > x[j]; j--)
					swap(x, j, j - 1);
			return;
		}

		// Choose a partition element, v
		int m = off + (len >> 1); // Small arrays, middle element
		if (len > 7) {
			int l = off;
			int n = off + len - 1;
			if (len > 40) { // Big arrays, pseudomedian of 9
				int s = len / 8;

				// Hack start
				if (x[l] != Integer.MIN_VALUE || x[l + s] != Integer.MIN_VALUE || x[l + 2 * s] != Integer.MIN_VALUE
						|| x[m - s] != Integer.MIN_VALUE || x[m] != Integer.MIN_VALUE || x[m + s] != Integer.MIN_VALUE
						|| x[n - 2 * s] != Integer.MIN_VALUE || x[n - s] != Integer.MIN_VALUE
						|| x[n] != Integer.MIN_VALUE) {
					// Do nothing
				} else {
					set(x, l, max - 4);
					set(x, l + s, max - 5);
					set(x, l + 2 * s, max - 3);
					set(x, m - s, max - 1);
					set(x, m, max - 2);
					set(x, m + s, max);
					set(x, n - 2 * s, max - 7);
					set(x, n - s, max - 8);
					set(x, n, max - 6);
					max -= 9;

					pivots.add(x[l]);
					if (pivots.size() % 1000 == 0)
						System.out.println(pivots.size()); // Report progress
				}
				// Hack end

				l = med3(x, l, l + s, l + 2 * s);
				m = med3(x, m - s, m, m + s);
				n = med3(x, n - 2 * s, n - s, n);
			}
			// Hack start
			else {
				if (x[l] != Integer.MIN_VALUE || x[m] != Integer.MIN_VALUE || x[n] != Integer.MIN_VALUE) {
					// Do nothing
				} else {
					set(x, l, max - 1);
					set(x, m, max - 2);
					set(x, n, max);
					max -= 3;

					pivots.add(x[l]);
					if (pivots.size() % 1000 == 0)
						System.out.println(pivots.size()); // Report progress
				}
			}
			// Hack end

			m = med3(x, l, m, n); // Mid-size, med of 3
		}
		int v = x[m];

		// Establish Invariant: v* (<v)* (>v)* v*
		int a = off, b = a, c = off + len - 1, d = c;
		while (true) {
			while (b <= c && x[b] <= v) {
				if (x[b] == v)
					swap(x, a++, b);
				b++;
			}
			while (c >= b && x[c] >= v) {
				if (x[c] == v)
					swap(x, c, d--);
				c--;
			}
			if (b > c)
				break;
			swap(x, b++, c--);
		}

		// Swap partition elements back to middle
		int s, n = off + len;
		s = Math.min(a - off, b - a);
		vecswap(x, off, b - s, s);
		s = Math.min(d - c, n - d - 1);
		vecswap(x, b, n - s, s);

		// Recursively sort non-partition-elements
		if ((s = b - a) > 1)
			sort1(x, off, s);
		if ((s = d - c) > 1)
			sort1(x, n - s, s);
	}

	/**
	 * Swaps x[a] with x[b].
	 */
	private static void swap(int x[], int a, int b) {
		int t = x[a];
		x[a] = x[b];
		x[b] = t;
		int tpos = positions[a];
		positions[a] = positions[b];
		positions[b] = tpos;
	}

	/**
	 * Swaps x[a .. (a+n-1)] with x[b .. (b+n-1)].
	 */
	private static void vecswap(int x[], int a, int b, int n) {
		for (int i = 0; i < n; i++, a++, b++)
			swap(x, a, b);
	}

	/**
	 * Returns the index of the median of the three indexed integers.
	 */
	private static int med3(int x[], int a, int b, int c) {
		return (x[a] < x[b] ? (x[b] < x[c] ? b : x[a] < x[c] ? c : a) : (x[b] > x[c] ? b : x[a] > x[c] ? c : a));
	}

	private static void set(int x[], int pos, int value) throws Exception {
		if (x[pos] == Integer.MIN_VALUE)
			x[pos] = value;
		else
			throw new Exception();
	}
}