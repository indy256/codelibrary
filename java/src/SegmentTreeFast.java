import java.util.*;

public class SegmentTreeFast {

	// specific code
	static final int INIT_VALUE = 0;
	static final int NEUTRAL_VALUE = Integer.MIN_VALUE;
	static final int NEUTRAL_DELTA = 0;

	static int joinValues(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	static int joinDeltas(int oldDelta, int newDelta) {
		return oldDelta + newDelta;
	}

	static int joinValueWithDelta(int value, int delta, int length) {
		return value + delta;
	}

	// generic code
	int n;
	int[] value;
	int[] delta;
	int[] len;

	public SegmentTreeFast(int n) {
		this.n = n;
		value = new int[2 * n];
		Arrays.fill(value, INIT_VALUE);
		delta = new int[2 * n];
		len = new int[2 * n];
		for (int i = n; i < 2 * n; i++)
			len[i] = 1;
		for (int i = 2 * n - 1; i > 1; i -= 2)
			len[i >> 1] = len[i] + len[i ^ 1];
	}

	void applyDelta(int i, int delta) {
		value[i] = joinValueWithDelta(value[i], delta, len[i]);
		this.delta[i] = joinDeltas(this.delta[i], delta);
	}

	void push(int i) {
		applyDelta(i, delta[i >> 1]);
		applyDelta(i ^ 1, delta[i >> 1]);
		delta[i >> 1] = NEUTRAL_DELTA;
	}

	void pushDown(int i) {
		int k;
		for (k = 0; (i >> k) > 0; k++)
			;
		for (k -= 2; k >= 0; k--)
			push(i >> k);
	}

	void popUp(int i) {
		for (; i > 1; i >>= 1)
			value[i >> 1] = joinValues(value[i], value[i ^ 1]);
	}

	public void modify(int a, int b, int v) {
		a += n;
		b += n;
		pushDown(a);
		pushDown(b);
		int ta = -1;
		int tb = -1;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0) {
				applyDelta(a, v);
				if (ta == -1)
					ta = a;
			}
			if ((b & 1) == 0) {
				applyDelta(b, v);
				if (tb == -1)
					tb = b;
			}
		}
		popUp(ta);
		popUp(tb);
	}

	public int query(int a, int b) {
		a += n;
		b += n;
		pushDown(a);
		pushDown(b);
		int res = NEUTRAL_VALUE;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0)
				res = joinValues(res, value[a]);
			if ((b & 1) == 0)
				res = joinValues(res, value[b]);
		}
		return res;
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			int[] x = new int[n];
			Arrays.fill(x, INIT_VALUE);
			SegmentTreeFast t = new SegmentTreeFast(n);
			for (int i = 0; i < 1000; i++) {
				int b = rnd.nextInt(n);
				int a = rnd.nextInt(b + 1);
				int cmd = rnd.nextInt(3);
				if (cmd == 0) {
					int delta = rnd.nextInt(100) - 50;
					t.modify(a, b, delta);
					for (int j = a; j <= b; j++)
						x[j] = joinValueWithDelta(x[j], delta, 1);
				} else if (cmd == 1) {
					int res1 = t.query(a, b);
					int res2 = x[a];
					for (int j = a + 1; j <= b; j++)
						res2 = joinValues(res2, x[j]);
					if (res1 != res2) {
						System.err.println("error");
						return;
					}
				} else {
					for (int j = 0; j < n; j++) {
						if (t.query(j, j) != x[j]) {
							System.err.println("error");
							return;
						}
					}
				}
			}
		}
		System.out.println("Test passed");
	}
}
