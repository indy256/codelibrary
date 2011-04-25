public class SegmentTreeMaxInterval {
	long[] t;
	long[] add;

	void createTree(int n) {
		int len = 1;
		while (len < n) {
			len *= 2;
		}
		t = new long[len + len];
		add = new long[len + len];
	}

	public SegmentTreeMaxInterval(int n) {
		createTree(n);
	}

	public SegmentTreeMaxInterval(long[] x) {
		createTree(x.length);
		for (int i = 0; i < x.length; i++) {
			t[i + t.length / 2] = x[i];
		}
		for (int i = t.length / 2 - 1; i > 0; i--) {
			t[i] = Math.max(t[i + i], t[i + i + 1]);
		}
	}

	void update(int i) {
		for (; i > 0; i /= 2) {
			t[i] = Math.max(t[i + i] + add[i + i], t[i + i + 1] + add[i + i + 1]);
		}
	}

	long sumAdd(int i) {
		long res = 0;
		for (; i > 0; i /= 2) {
			res += add[i];
		}
		return res;
	}

	public long get(int i) {
		i += t.length / 2;
		return t[i] + sumAdd(i);
	}

	// Sets value to the i-th element
	public void set(int i, long value) {
		add(i, -get(i) + value);
	}

	// Adds value to the i-th element
	public void add(int i, long value) {
		i += t.length / 2;
		t[i] += value;
		update(i / 2);
	}

	// Adds value to every element in range [a, b]
	public void add(int a, int b, long value) {
		a += t.length / 2;
		b += t.length / 2;
		int pa = a;
		int pb = b;
		while (a <= b) {
			if ((a & 1) != 0) {
				add[a] += value;
			}
			if ((b & 1) == 0) {
				add[b] += value;
			}
			a = (a + 1) / 2;
			b = (b - 1) / 2;
		}
		update(pa / 2);
		update(pb / 2);
	}

	// Returns maximum of elements in range [a, b]
	public long max(int a, int b) {
		long res = Long.MIN_VALUE;
		a += t.length / 2;
		b += t.length / 2;
		long sa = sumAdd(a / 2);
		long sb = sumAdd(b / 2);
		int pa = a;
		int pb = b;
		while (a <= b) {
			if ((a & 1) != 0) {
				res = Math.max(res, t[a] + add[a] + sa);
			}
			if ((b & 1) == 0) {
				res = Math.max(res, t[b] + add[b] + sb);
			}
			pa /= 2;
			pb /= 2;
			sa -= add[pa];
			sb -= add[pb];
			a = (a + 1) / 2;
			b = (b - 1) / 2;
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeMaxInterval sm = new SegmentTreeMaxInterval(new long[] { 1, 2, 3, 4 });
		sm.set(0, 2);
		sm.set(1, 1);
		sm.set(2, 5);
		sm.add(0, 3, 5);
		System.out.println(10 == sm.max(0, 3));
		System.out.println(9 == sm.get(3));
	}
}
