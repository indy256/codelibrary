public class ReferenceSegmentTree {
	long[] t;

	public ReferenceSegmentTree(int n) {
		t = new long[n];
	}

	public long get(int i) {
		return t[i];
	}

	public void set(int i, long value) {
		t[i] = value;
	}

	public void set(int a, int b, long value) {
		for (int i = a; i <= b; i++) {
			t[i] = value;
		}
	}

	public void add(int i, long value) {
		t[i] += value;
	}

	public void add(int a, int b, long value) {
		for (int i = a; i <= b; i++) {
			t[i] += value;
		}
	}

	public int cover(int a, int b) {
		int res = 0;
		for (int i = a; i <= b; i++) {
			if (t[i] == 0)
				res++;
		}
		return res;
	}

	public long sum(int a, int b) {
		long res = 0;
		for (int i = a; i <= b; i++) {
			res += t[i];
		}
		return res;
	}

	public long max(int a, int b) {
		long res = Long.MIN_VALUE;
		for (int i = a; i <= b; i++) {
			res = Math.max(res, t[i]);
		}
		return res;
	}

	public long min(int a, int b) {
		long res = Long.MAX_VALUE;
		for (int i = a; i <= b; i++) {
			res = Math.min(res, t[i]);
		}
		return res;
	}

	public int getKthZeroCyclic(int x1, int k) {
		int totFree = t.length - (int) sum(0, t.length - 1);
		k %= totFree;
		if (k == 0)
			k = totFree;
		for (int i = 0; i < t.length; i++)
			if (t[(x1 + i) % t.length] == 0)
				if (--k == 0)
					return (x1 + i) % t.length;
		return -1;
	}

	public int getPrevZero(int x1) {
		for (int i = x1 - 1; i >= 0; i--)
			if (t[i] == 0)
				return i;
		return -1;
	}
}
