import java.util.*;

public class SegmentTreeFast2 {

	// Modify these 6 methods to implement your custom operation on the tree
	int getInitValue() {
		return 0;
	}

	int getNeutralValue() {
		return Integer.MIN_VALUE;
	}

	int getNeutralDelta() {
		return 0;
	}

	int joinValues(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	int joinDeltas(int oldDelta, int newDelta) {
		return oldDelta + newDelta;
	}

	int joinValueWithDelta(int value, int delta, int length) {
		return value + delta; // value + delta * length (for sum)
	}

	// generic code
	int[] value;
	int[] delta; // delta[i] affects value[2*i+1], value[2*i+2], delta[2*i+1] and delta[2*i+2]
	int[] len;

	public SegmentTreeFast2(int n) {
		value = new int[2 * n];
		for (int i = 0; i < n; i++)
			value[i + n] = getInitValue();
		for (int i = 2 * n - 1; i > 1; i -= 2)
			value[i >> 1] = joinValues(value[i], value[i ^ 1]);

		delta = new int[2 * n];
		Arrays.fill(delta, getNeutralDelta());

		len = new int[2 * n];
		Arrays.fill(len, n, 2 * n, 1);
		for (int i = 2 * n - 1; i > 1; i -= 2)
			len[i >> 1] = len[i] + len[i ^ 1];
	}

	void applyDelta(int i, int delta) {
		value[i] = joinValueWithDelta(value[i], delta, len[i]);
		this.delta[i] = joinDeltas(this.delta[i], delta);
	}

	void pushDelta(int i) {
		int d = 0;
		for (; (i >> d) > 0; d++)
			;
		for (d -= 2; d >= 0; d--) {
			int x = i >> d;
			applyDelta(x, delta[x >> 1]);
			applyDelta(x ^ 1, delta[x >> 1]);
			delta[x >> 1] = getNeutralDelta();
		}
	}

	public void modify(int a, int b, int delta) {
		a += value.length >> 1;
		b += value.length >> 1;
		pushDelta(a);
		pushDelta(b);
		int ta = -1;
		int tb = -1;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0) {
				applyDelta(a, delta);
				if (ta == -1)
					ta = a;
			}
			if ((b & 1) == 0) {
				applyDelta(b, delta);
				if (tb == -1)
					tb = b;
			}
		}
		for (int i = ta; i > 1; i >>= 1)
			value[i >> 1] = joinValues(value[i], value[i ^ 1]);
		for (int i = tb; i > 1; i >>= 1)
			value[i >> 1] = joinValues(value[i], value[i ^ 1]);
	}

	public int query(int a, int b) {
		a += value.length >> 1;
		b += value.length >> 1;
		pushDelta(a);
		pushDelta(b);
		int res = getNeutralValue();
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
			SegmentTreeFast2 t = new SegmentTreeFast2(n);
			Arrays.fill(x, t.getInitValue());
			for (int i = 0; i < 1000; i++) {
				int b = rnd.nextInt(n);
				int a = rnd.nextInt(b + 1);
				int cmd = rnd.nextInt(3);
				if (cmd == 0) {
					int delta = rnd.nextInt(100) - 50;
					t.modify(a, b, delta);
					for (int j = a; j <= b; j++)
						x[j] = t.joinValueWithDelta(x[j], delta, 1);
				} else if (cmd == 1) {
					int res1 = t.query(a, b);
					int res2 = x[a];
					for (int j = a + 1; j <= b; j++)
						res2 = t.joinValues(res2, x[j]);
					if (res1 != res2)
						throw new RuntimeException("error");

				} else {
					for (int j = 0; j < n; j++) {
						if (t.query(j, j) != x[j])
							throw new RuntimeException("error");
					}
				}
			}
		}
		System.out.println("Test passed");
	}
}
