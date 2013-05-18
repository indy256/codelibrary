public class FenwickTreeExtended {

	public static void add(int[] t, int i, int value) {
		for (; i < t.length; i += (i + 1) & -(i + 1))
			t[i] += value;
	}

	// sum[0..i]
	public static int sum(int[] t, int i) {
		int res = 0;
		for (; i >= 0; i -= (i + 1) & -(i + 1))
			res += t[i];
		return res;
	}

	public static int[] createTreeFromArray(int[] a) {
		int[] res = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			res[i] += a[i];
			int j = i + ((i + 1) & -(i + 1)); // i | (i + 1);
			if (j < a.length)
				res[j] += res[i];
		}
		return res;
	}

	// sum[a..b]
	public static int sum(int[] t, int a, int b) {
		return sum(t, b) - sum(t, a - 1);
	}

	public static int get(int[] t, int i) {
		return sum(t, i) - sum(t, i - 1);
//		int res = t[i];
//		if (i > 0) {
//			int lca = i - ((i + 1) & -(i + 1));
//			for (--i; i != lca; i -= (i + 1) & -(i + 1))
//				res -= t[i];
//		}
//		return res;
	}

	public static void set(int[] t, int i, int value) {
		add(t, i, -get(t, i) + value);
	}

	// interval add trick
	public void rev_add(int[] t, int a, int b, int value) {
		add(t, a, value);
		add(t, b + 1, -value);
	}

	// get for interval add trick
	public int rev_get(int[] t, int i) {
		return sum(t, i);
	}

	// Returns min(p|sum[0,p]>=sum)
	public static int lower_bound(int[] t, int sum) {
		--sum;
		int pos = -1;
		for (int blockSize = Integer.highestOneBit(t.length); blockSize != 0; blockSize >>= 1) {
			int nextPos = pos + blockSize;
			if (nextPos < t.length && sum >= t[nextPos]) {
				sum -= t[nextPos];
				pos = nextPos;
			}
		}
		return pos + 1;
	}

	// Usage example
	public static void main(String[] args) {
		int[] t = new int[10];
		set(t, 0, 1);
		add(t, 9, -2);
		System.out.println(-1 == sum(t, 0, 9));

		t = createTreeFromArray(new int[]{1, 2, 3, 4, 5, 6});
		for (int i = 0; i < t.length; i++)
			System.out.print(get(t, i) + " ");
		System.out.println();
		t = createTreeFromArray(new int[]{0, 0, 1, 0, 0, 1, 0, 0});
		System.out.println(5 == lower_bound(t, 2));
	}
}
