import java.util.Random;

public class Partition {

	static Random rnd = new Random(1);

	public static int randomizedPartition(int[] a, int i, int j) {
		int high = j;
		int separator = a[i + rnd.nextInt(j - i + 1)];
		do {
			while (a[i] < separator)
				++i;
			while (a[j] > separator)
				--j;
			if (i > j)
				break;
			int t = a[i];
			a[i] = a[j];
			a[j] = t;
			++i;
			--j;
		} while (i <= j);
		return Math.max(0, j);
	}

	public static int partition(int[] a, int fromInclusive, int toExclusive, int separatorIndex) {
		int i = fromInclusive;
		int j = toExclusive - 1;
		if (i >= j)
			return j;

		int separator = a[separatorIndex];
		a[separatorIndex] = a[i];
		a[i] = separator;
		++i;
		while (i <= j) {
			while (i <= j && a[i] < separator)
				++i;
			while (i <= j && a[j] > separator)
				--j;
			if (i >= j)
				break;
			int t = a[j];
			a[j] = a[i];
			a[i] = t;
			++i;
			--j;
		}
		a[fromInclusive] = a[j];
		a[j] = separator;
		return j;
	}

	// Random test
	public static void main(String[] args) {
		for (int step = 0; step < 100_000; step++) {
			int n = rnd.nextInt(10) + 2;
			int[] a = rnd.ints(n, 0, 10).toArray();
			int[] b = a.clone();
			check(a, randomizedPartition(a, 0, n - 1), n - 2);
			for (int i = 0; i < n; i++) {
				int[] c = b.clone();
				check(c, partition(c, 0, n, i), n - 1);
			}
		}
	}

	static void check(int[] a, int k, int maxpos) {
		if (k < 0 || k > maxpos)
			throw new RuntimeException();
		for (int i = 0; i < k; i++)
			for (int j = k + 1; j < a.length; j++)
				if (a[i] > a[j])
					throw new RuntimeException();
	}
}
