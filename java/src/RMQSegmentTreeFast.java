public class RMQSegmentTreeFast {
	final int n = 1 << 18;
	int[] t = new int[n + n];

	public RMQSegmentTreeFast() {
	}

	public RMQSegmentTreeFast(int[] a) {
		System.arraycopy(a, 0, t, n, a.length);
		for (int i = n - 1; i > 0; i--)
			t[i] = Math.max(t[i + i], t[i + i + 1]);
	}

	public int get(int i) {
		return t[i + n];
	}

	// Sets value to the i-th element
	public void set(int i, int value) {
		i += n;
		t[i] = value;
		for (i >>= 1; i > 0; i >>= 1)
			t[i] = Math.max(t[i + i], t[i + i + 1]);
	}

	// Adds value to the i-th element
	public void add(int i, int value) {
		i += n;
		t[i] += value;
		for (i >>= 1; i > 0; i >>= 1)
			t[i] = Math.max(t[i + i], t[i + i + 1]);
	}

	// Returns maximum of elements in range [a, b]
	public int max(int a, int b) {
		int res = Integer.MIN_VALUE;
		for (a += n, b += n; a <= b;) {
			res = Math.max(res, t[a]);
			res = Math.max(res, t[b]);
			a = (a + 1) >> 1;
			b = (b - 1) >> 1;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		RMQSegmentTreeFast rmq = new RMQSegmentTreeFast(new int[] { 1, 2, 3, 4 });
		rmq.set(0, 2);
		rmq.set(1, 1);
		rmq.set(2, 3);
		rmq.add(1, 5);
		System.out.println(6 == rmq.max(0, 3));
	}
}
