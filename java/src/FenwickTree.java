public class FenwickTree {
	int[] t;
	int n;

	public FenwickTree(int n) {
		this.n = n;
		t = new int[n];
	}

	public FenwickTree(int[] a) {
		n = a.length;
		t = new int[n];
		for (int i = 0; i < n; i++) {
			t[i] += a[i];
			int j = i | i + 1;
			if (j < n)
				t[j] += t[i];
		}
	}

	// Adds value to the i-th element
	public void add(int i, int value) {
		for (; i < n; i |= i + 1 /* i += (i + 1) & -(i + 1) */)
			t[i] += value;
	}

	// Sum of elements in range [0, i]
	public int sum(int i) {
		int res = 0;
		for (; i >= 0; i -= (i + 1) & -(i + 1) /* i = (i & (i + 1)) - 1 */)
			res += t[i];
		return res;
	}

	// Sum of elements in range [a, b]
	public int sum(int a, int b) {
		return sum(b) - sum(a - 1);
	}

	public int get(int i) {
		// return sum(i) - sum(i - 1);
		int res = t[i];
		if (i > 0) {
			int lca = i - ((i + 1) & -(i + 1));
			--i;
			while (i != lca) {
				res -= t[i];
				i -= (i + 1) & -(i + 1);
			}
		}
		return res;
	}

	public void set(int i, int value) {
		add(i, -get(i) + value);
	}

	// Returns the largest b such that sum(0,b) >= sum and has minimal value
	// TODO: not very useful - reimplement
	public int search(long sum) {
		int pos = -1;
		for (int blockSize = Integer.highestOneBit(n); blockSize != 0; blockSize >>= 1) {
			int nextPos = pos + blockSize;
			if (nextPos < n && sum >= t[nextPos]) {
				pos = nextPos;
				sum -= t[nextPos];
			}
		}
		return pos;
	}

	// number of free places in [0, x]
	public int getZeros(int x) {
		return x < 0 ? 0 : x + 1 - sum(x);
	}

	// number of free places in [x1, x2], cyclically
	public int getZeros(int x1, int x2) {
		int s = getZeros(x2) - getZeros(x1 - 1);
		return x1 <= x2 ? s : s + getZeros(n - 1);
	}

	// position of the k'th free element starting from x1
	// assumption: k > 0
	public int getKthZeroCyclic(int x1, int k) {
		int totZeros = getZeros(n - 1);
		k = (k + totZeros - 1) % totZeros + 1;
		int lo = -1;
		int hi = n;
		while (hi - lo > 1) {
			int mid = (lo + hi) >> 1;
			int zeros = getZeros(x1, (x1 + mid) % n);
			if (zeros < k)
				lo = mid;
			else
				hi = mid;
		}
		return (x1 + hi) % n;
	}

	// Usage example
	public static void main(String[] args) {
		FenwickTree t = new FenwickTree(10);
		t.set(0, 1);
		System.out.println(t.getZeros(0, 2));
		System.out.println(t.getKthZeroCyclic(0, 1));
		t.add(0, 3);
		System.out.println(t.sum(0, 0));

		t = new FenwickTree(new int[] { 1, 2, 3, 4, 5, 6 });
		for (int i = 0; i < t.n; i++)
			System.out.print(t.get(i) + " ");
		System.out.println();
	}
}
