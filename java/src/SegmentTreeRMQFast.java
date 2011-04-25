public class SegmentTreeRMQFast {
	final int n = 1 << 18;
	int[] t = new int[n + n];

	public SegmentTreeRMQFast() {
	}

	public SegmentTreeRMQFast(int[] a) {
		System.arraycopy(a, 0, t, n, a.length);
		for (int i = n - 1; i > 0; i--)
			t[i] = Math.max(t[i + i], t[i + i + 1]);
	}

	public int get(int i) {
		return t[i + n];
	}

	public void set(int i, int value) {
		add(i, value - t[i + n]);
	}

	public void add(int i, int value) {
		i += n;
		t[i] += value;
		for (i >>= 1; i > 0; i >>= 1)
			t[i] = Math.max(t[i + i], t[i + i + 1]);
	}

	// max[a, b]
	public int max(int a, int b) {
		int res = Integer.MIN_VALUE;
		for (a += n, b += n; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			res = Math.max(res, t[a]);
			res = Math.max(res, t[b]);
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeRMQFast rmq = new SegmentTreeRMQFast(new int[] { 1, 2, 3, 4 });
		rmq.set(0, 2);
		rmq.set(1, 1);
		rmq.set(2, 3);
		rmq.add(1, 5);
		System.out.println(6 == rmq.max(0, 3));
	}
}
