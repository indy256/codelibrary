public class FenwickTreeExtra {

	public static void add(int[] t, int i, int value) {
		for (; i < t.length; i += (i + 1) & -(i + 1))
			t[i] += value;
	}

	// sum[0,i]
	public static int sum(int[] t, int i) {
		int res = 0;
		for (; i >= 0; i -= (i + 1) & -(i + 1))
			res += t[i];
		return res;
	}

	public static int[] createTree(int[] a) {
		int[] res = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			res[i] += a[i];
			int j = i + ((i + 1) & -(i + 1)); // i | (i + 1);
			if (j < a.length)
				res[j] += res[i];
		}
		return res;
	}

	// sum[a,b]
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

	// interval update trick
	public void rev_add(int[] t, int a, int b, int value) {
		add(t, a, value);
		add(t, b + 1, -value);
	}

	// get for interval update
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

	// number of free places in [0, x]
	public static int getZeros(int[] t, int x) {
		return x < 0 ? 0 : x + 1 - sum(t, x);
	}

	// number of free places in [x1, x2], cyclically
	public static int getZeros(int[] t, int x1, int x2) {
		int s = getZeros(t, x2) - getZeros(t, x1 - 1);
		return x1 <= x2 ? s : s + getZeros(t, t.length - 1);
	}

	// position of the k'th free element starting from x1
	// precondition: k > 0
	// log^2(n) complexity
	public static int getKthZeroCyclic(int[] t, int x1, int k) {
		int n = t.length;
		int totZeros = getZeros(t, n - 1);
		k = (k + totZeros - 1) % totZeros + 1;
		int lo = -1;
		int hi = n;
		while (hi - lo > 1) {
			int mid = (lo + hi) >> 1;
			int zeros = getZeros(t, x1, (x1 + mid) % n);
			if (zeros < k)
				lo = mid;
			else
				hi = mid;
		}
		return (x1 + hi) % n;
	}

	// Usage example
	public static void main(String[] args) {
		int[] t = new int[10];
		set(t, 0, 1);
		System.out.println(2 == getZeros(t, 0, 2));
		System.out.println(1 == getKthZeroCyclic(t, 0, 1));
		add(t, 0, 3);
		System.out.println(4 == sum(t, 0, 0));

		t = createTree(new int[] { 1, 2, 3, 4, 5, 6 });
		for (int i = 0; i < t.length; i++)
			System.out.print(get(t, i) + " ");
		System.out.println();
		t = createTree(new int[] { 0, 0, 1, 0, 0, 1, 0, 0 });
		System.out.println(5 == lower_bound(t, 2));
	}
}
