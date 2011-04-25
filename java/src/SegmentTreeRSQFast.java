public class SegmentTreeRSQFast {
	final int n = 1 << 18;
	int[] t = new int[n + n];

	public SegmentTreeRSQFast() {
	}

	public SegmentTreeRSQFast(int[] a) {
		System.arraycopy(a, 0, t, n, a.length);
		for (int i = n - 1; i > 0; i--)
			t[i] = t[i + i] + t[i + i + 1];
	}

	public int get(int i) {
		return t[i + n];
	}

	public void set(int i, int value) {
		add(i, value - t[i + n]);
	}

	public void add(int i, int value) {
		for (i += n; i > 0; i >>= 1)
			t[i] += value;
	}

	// sum[a, b]
	public int sum(int a, int b) {
		int res = 0;
		for (a += n, b += n; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0)
				res += t[a];
			if ((b & 1) == 0)
				res += t[b];
		}
		return res;
	}

	// sum[0, b]
	public int sum(int b) {
		if (b == n - 1)
			return t[1];
		int res = 0;
		for (b += n; b > 0; b = (b - 1) >> 1)
			if ((b & 1) == 0)
				res += t[b];
		return res;
	}

	public int getPrevZero(int i) {
		i += n;
		int k = 1;
		for (; i > 0; i >>= 1, k <<= 1)
			if ((i & 1) != 0 && t[i - 1] < k)
				break;
		if (i <= 1)
			return -1;
		for (--i; k > 1;) {
			k >>= 1;
			i <<= 1;
			if (t[i + 1] < k)
				++i;
		}
		return i - n;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeRSQFast rsq1 = new SegmentTreeRSQFast(new int[] { 1, 2, 3, 4 });
		rsq1.set(0, 2);
		rsq1.set(1, 1);
		rsq1.set(2, 3);
		rsq1.add(1, 5);
		System.out.println(15 == rsq1.sum(0, 3));
		System.out.println(6 == rsq1.get(1));
		System.out.println(rsq1.sum(0, 3) == rsq1.sum(3));

		SegmentTreeRSQFast rsq2 = new SegmentTreeRSQFast();
		rsq2.set(2, 1);
		rsq2.set(3, 1);
		System.out.println(-1 == rsq2.getPrevZero(0));
		System.out.println(0 == rsq2.getPrevZero(1));
		System.out.println(1 == rsq2.getPrevZero(2));
		System.out.println(1 == rsq2.getPrevZero(3));
	}
}
