public class SegmentTreeFastAddMax {
	final int n;
	final int[] t;

	public SegmentTreeFastAddMax(int n) {
		this.n = n;
		t = new int[n + n];
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
		for (; i > 1; i >>= 1)
			t[i >> 1] = Math.max(t[i], t[i ^ 1]);
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
		SegmentTreeFastAddMax t = new SegmentTreeFastAddMax(11);
		t.set(1, 2);
		t.set(2, 1);
		t.add(1, 5);
		System.out.println(7 == t.max(1, 10));
	}
}
