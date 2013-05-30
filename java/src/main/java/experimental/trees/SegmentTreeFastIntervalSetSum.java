package experimental.trees;
public class SegmentTreeFastIntervalSetSum {
	int n;
	int[] t;
	int[] m;
	boolean[] h;
	int[] q;

	public SegmentTreeFastIntervalSetSum(int n) {
		this.n = n;
		t = new int[2 * n];
		m = new int[2 * n];
		h = new boolean[2 * n];
		q = new int[2 * n];
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

	public void modifySet(int a, int b, int v) {
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

	public int querySum(int a, int b) {
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
		return querySum(i, i);
	}

	public void set(int i, int v) {
		modifySet(i, i, v);
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeFastIntervalSetSum t = new SegmentTreeFastIntervalSetSum(10);
		t.modifySet(0, 5, 1);
		t.modifySet(2, 3, 2);
		System.out.println(t.querySum(0, 5));
	}
}
