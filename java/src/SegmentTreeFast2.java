import java.util.*;

public class SegmentTreeFast2 {

	// Modify the following 5 methods to implement your custom operations on the tree.
	// This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
	int modifyOperation(int x, int y) {
		// treat neutral delta separately
		if (x == getNeutralDelta()) {
			return y;
		}
		if (y == getNeutralDelta()) {
			return x;
		}
		// define operation here
		return x + y;
	}

	int queryOperation(int x, int y) {
		return Math.max(x, y);
	}

	int deltaEffectOnSegment(int delta, int segmentLength) {
		// Here you must write a fast equivalent of following slow code:
		// int result = delta;
		// for (int i = 0; i < segmentLength - 1; i++) result = queryOperation(result, delta);
		// return result;
		return delta;
	}

	int getNeutralDelta() {
		return Integer.MIN_VALUE;
	}

	int getInitValue() {
		return 0;
	}

	// generic code
	int[] value;
	int[] delta; // delta[i] affects value[2*i+1], value[2*i+2], delta[2*i+1] and delta[2*i+2]
	int[] len;

	void pushDelta(int i) {
		int d = 0;
		for (; (i >> d) > 0; d++)
			;
		for (d -= 2; d >= 0; d--) {
			int x = i >> d;
			value[x] = modifyOperation(value[x], deltaEffectOnSegment(delta[x >> 1], len[x]));
			value[(x ^ 1)] = modifyOperation(value[(x ^ 1)], deltaEffectOnSegment(delta[x >> 1], len[(x ^ 1)]));
			delta[x] = modifyOperation(this.delta[x], delta[x >> 1]);
			delta[(x ^ 1)] = modifyOperation(this.delta[(x ^ 1)], delta[x >> 1]);
			delta[x >> 1] = getNeutralDelta();
		}
	}

	public SegmentTreeFast2(int n) {
		value = new int[2 * n];
		for (int i = 0; i < n; i++)
			value[i + n] = getInitValue();
		for (int i = 2 * n - 1; i > 1; i -= 2)
			value[i >> 1] = queryOperation(value[i], value[i ^ 1]);

		delta = new int[2 * n];
		Arrays.fill(delta, getNeutralDelta());

		len = new int[2 * n];
		Arrays.fill(len, n, 2 * n, 1);
		for (int i = 2 * n - 1; i > 1; i -= 2)
			len[i >> 1] = len[i] + len[i ^ 1];
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
				value[a] = modifyOperation(value[a], deltaEffectOnSegment(delta, len[a]));
				this.delta[a] = modifyOperation(this.delta[a], delta);
				if (ta == -1)
					ta = a;
			}
			if ((b & 1) == 0) {
				value[b] = modifyOperation(value[b], deltaEffectOnSegment(delta, len[b]));
				this.delta[b] = modifyOperation(this.delta[b], delta);
				if (tb == -1)
					tb = b;
			}
		}
		for (int i = ta; i > 1; i >>= 1)
			value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
		for (int i = tb; i > 1; i >>= 1)
			value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
	}

	public int query(int a, int b) {
		a += value.length >> 1;
		b += value.length >> 1;
		pushDelta(a);
		pushDelta(b);
		int res = 0;
		boolean found = false;
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0) {
				res = found ? queryOperation(res, value[a]) : value[a];
				found = true;
			}
			if ((b & 1) == 0) {
				res = found ? queryOperation(res, value[b]) : value[b];
				found = true;
			}
		}
		if (!found)
			throw new RuntimeException();
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
						x[j] = t.modifyOperation(x[j], delta);
				} else if (cmd == 1) {
					int res1 = t.query(a, b);
					int res2 = x[a];
					for (int j = a + 1; j <= b; j++)
						res2 = t.queryOperation(res2, x[j]);
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
