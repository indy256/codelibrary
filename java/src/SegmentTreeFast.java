import java.util.Arrays;
import java.util.Random;

public class SegmentTreeFast {

	// Modify the following 6 methods to implement your custom operations on the tree. Pay attention to contracts
	int queryOperation(int x, int y) {
		return Math.max(x, y);
	}

	int modifyOperation(int x, int y) {
		return x + y; // return y == getNeutralDelta() ? x : y; (for modifyOperation "set")
	}

	int totalDeltaEffect(int delta, int count) {
		// contract: totalDeltaEffect(delta, count) == queryOperation(delta, queryOperation(delta, ...count times))
		return delta; // delta * count (for queryOperation "sum")
	}

	int getNeutralValue() {
		// contract: queryOperation(x, getNeutralValue()) == x
		return Integer.MIN_VALUE;
	}

	int getNeutralDelta() {
		// contract: modifyOperation(x, getNeutralDelta()) == x
		return 0;
	}

	int getInitValue() {
		return 0;
	}

	// generic code
	int[] value;
	int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]
	int[] len;

	public SegmentTreeFast(int n) {
		value = new int[2 * n];
		for (int i = 0; i < n; i++) {
			value[i + n] = getInitValue();
		}
		for (int i = 2 * n - 1; i > 1; i -= 2) {
			value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
		}

		delta = new int[2 * n];
		Arrays.fill(delta, getNeutralDelta());

		len = new int[2 * n];
		Arrays.fill(len, n, 2 * n, 1);
		for (int i = 2 * n - 1; i > 1; i -= 2) {
			len[i >> 1] = len[i] + len[i ^ 1];
		}
	}

	void pushDelta(int i) {
		int d = 0;
		for (; (i >> d) > 0; d++) {
			;
		}
		for (d -= 2; d >= 0; d--) {
			int x = i >> d;
			value[x >> 1] = joinValueWithDelta(x >> 1);
			delta[x] = modifyOperation(delta[x], delta[x >> 1]);
			delta[x ^ 1] = modifyOperation(delta[x ^ 1], delta[x >> 1]);
			delta[x >> 1] = getNeutralDelta();
		}
	}

	int joinValueWithDelta(int a) {
		return modifyOperation(value[a], totalDeltaEffect(delta[a], len[a]));
	}

	public int query(int a, int b) {
		a += value.length >> 1;
		b += value.length >> 1;
		pushDelta(a);
		pushDelta(b);
		int res = getNeutralValue();
		for (; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0) {
				res = queryOperation(res, joinValueWithDelta(a));
			}
			if ((b & 1) == 0) {
				res = queryOperation(res, joinValueWithDelta(b));
			}
		}
		return res;
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
				this.delta[a] = modifyOperation(this.delta[a], delta);
				if (ta == -1) {
					ta = a;
				}
			}
			if ((b & 1) == 0) {
				this.delta[b] = modifyOperation(this.delta[b], delta);
				if (tb == -1) {
					tb = b;
				}
			}
		}
		for (int i = ta; i > 1; i >>= 1) {
			value[i >> 1] = queryOperation(joinValueWithDelta(i), joinValueWithDelta(i ^ 1));
		}
		for (int i = tb; i > 1; i >>= 1) {
			value[i >> 1] = queryOperation(joinValueWithDelta(i), joinValueWithDelta(i ^ 1));
		}
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			int[] x = new int[n];
			SegmentTreeFast t = new SegmentTreeFast(n);
			Arrays.fill(x, t.getInitValue());
			for (int i = 0; i < 1000; i++) {
				int b = rnd.nextInt(n);
				int a = rnd.nextInt(b + 1);
				int cmd = rnd.nextInt(3);
				if (cmd == 0) {
					int delta = rnd.nextInt(100) - 50;
					t.modify(a, b, delta);
					for (int j = a; j <= b; j++) {
						x[j] = t.modifyOperation(x[j], t.totalDeltaEffect(delta, 1));
					}
				} else if (cmd == 1) {
					int res1 = t.query(a, b);
					int res2 = x[a];
					for (int j = a + 1; j <= b; j++) {
						res2 = t.queryOperation(res2, x[j]);
					}
					if (res1 != res2) {
						throw new RuntimeException("error");
					}

				} else {
					for (int j = 0; j < n; j++) {
						if (t.query(j, j) != x[j]) {
							throw new RuntimeException("error");
						}
					}
				}
			}
		}
		System.out.println("Test passed");
	}
}
