public class SegmentTreeSumIntervalSetFast {
	final int n;
	final int[] t;
	final int[] m;
	final boolean[] h;
	final int[] q;

	public SegmentTreeSumIntervalSetFast(int n) {
		this.n = n;
		t = new int[n + n];
		m = new int[n + n];
		h = new boolean[n + n];
		q = new int[n + n];
		for (int i = n; i < 2 * n; i++)
			q[i] = 1;
		for (int i = 2 * n - 1; i > 1; i -= 2)
			q[i >> 1] = q[i] + q[i ^ 1];
	}

	void modifierHelper(int i, int p) {
		t[i] = p * q[i];
		m[i] = p;
		h[i] = true;
	}

	void pop(int i) {
		if (h[i >> 1]) {
			t[i >> 1] = m[i >> 1] * q[i >> 1];
		} else {
			t[i >> 1] = t[i] + t[i ^ 1];
		}
	}

	void popUp(int i) {
		for (; i > 1; i >>= 1)
			pop(i);
	}

	void push(int i) {
		if (h[i >> 1]) {
			modifierHelper(i, m[i >> 1]);
			modifierHelper(i ^ 1, m[i >> 1]);
			h[i >> 1] = false;
		}
	}

	void pushDown(int i) {
		int k;
		for (k = 0; (i >> k) > 0; k++)
			;
		for (k -= 2; k >= 0; k--)
			push(i >> k);
	}

	public void set(int a, int b, int v) {
		a += n;
		b += n;
		pushDown(a);
		pushDown(b);
		int ta = a;
		int tb = b;

		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0)
				modifierHelper(a, v);
			if ((b & 1) == 0)
				modifierHelper(b, v);
		}

		popUp(ta);
		popUp(tb);
	}

	public int sum(int a, int b) {
		a += n;
		b += n;
		pushDown(a);
		pushDown(b);

		int res = 0;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0)
				res += t[a];
			if ((b & 1) == 0)
				res += t[b];
		}
		return res;
	}

	public int get(int i) {
		return sum(i, i);
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeSumIntervalSetFast t = new SegmentTreeSumIntervalSetFast(10);
		t.set(0, 5, 1);
		t.set(2, 3, 2);
		System.out.println(t.sum(0, 5));
	}
}
