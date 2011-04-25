public class SegmentTreeMaxIM {
	long[] t;

	void createTree(int n) {
		int len = 1;
		while (len < n) {
			len *= 2;
		}
		t = new long[len + len];
	}

	public SegmentTreeMaxIM(int n) {
		createTree(n);
	}

	public SegmentTreeMaxIM(long[] a) {
		createTree(a.length);
		for (int i = 0; i < a.length; i++) {
			t[i + t.length / 2] = a[i];
		}
		for (int i = t.length / 2 - 1; i > 0; i--) {
			t[i] = Math.max(t[i + i], t[i + i + 1]);
		}
	}

	void updateUp(int i) {
		for (; i > 0; i /= 2) {
			t[i] = Math.max(t[i + i], t[i + i + 1]);
		}
	}

	public long get(int i) {
		return t[i + t.length / 2];
	}

	// Sets value to the i-th element
	public void set(int i, long value) {
		i += t.length / 2;
		t[i] = value;
		updateUp(i / 2);
	}

	// Adds value to the i-th element
	public void add(int i, long value) {
		i += t.length / 2;
		t[i] += value;
		updateUp(i / 2);
	}
	
	void add(int i, int j, int v)
	{
		update(i-1);
		update(j);
		for (i += maxn-1, j += maxn; i+1 < j; i >>= 1, j >>= 1)
		{
			if (!(i & 1))
				a[i+1] += v;
			if (j & 1)
				a[j-1] += v;
			//a[i/2] = min(a[i/2*2], a[i/2*2+1]);
			//a[j/2] = min(a[j/2*2], a[j/2*2+1]);
		}
	}


	// Returns maximum of elements in range [a, b]
	public long max(int a, int b) {
		long res = Long.MIN_VALUE;
		a += t.length / 2;
		b += t.length / 2;
		while (a <= b) {
			res = Math.max(res, t[a]);
			res = Math.max(res, t[b]);
			a = (a + 1) / 2;
			b = (b - 1) / 2;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeMaxIM sm = new SegmentTreeMaxIM(new long[] { 1, 2, 3, 4 });
		sm.set(0, 2);
		sm.set(1, 1);
		sm.set(2, 3);
		System.out.println(4 == sm.max(0, 3));
	}
}
