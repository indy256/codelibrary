import java.util.*;

public class SegmentTreeFast2 {

	// Modify the following 5 methods to implement your custom operations on the tree.
	// This example implements Add/Max operations. Operations like Add/Sum, Set/Max can also be implemented.
	int modifyOperation(int x, int y) {
		return x + y;
	}

	// query (or combine) operation
	int queryOperation(int leftValue, int rightValue) {
		return Math.max(leftValue, rightValue);
	}

	int deltaEffectOnSegment(int delta, int segmentLength) {
		// Here you must write a fast equivalent of following slow code:
		// int result = delta;
		// for (int i = 1; i < segmentLength; i++) result = queryOperation(result, delta);
		// return result;
		return delta;
	}

	int getNeutralDelta() {
		return 0;
	}

	int getInitValue() {
		return 0;
	}

	// generic code
	int[] value;
	int[] delta; // delta[i] affects value[2*i+1], value[2*i+2], delta[2*i+1] and delta[2*i+2]
	int[] len;

	int joinValueWithDelta(int value, int delta) {
		if (delta == getNeutralDelta()) return value;
		return modifyOperation(value, delta);
	}

	int joinDeltas(int delta1, int delta2) {
		if (delta1 == getNeutralDelta()) return delta2;
		if (delta2 == getNeutralDelta()) return delta1;
		return modifyOperation(delta1, delta2);
	}

	void pushDelta(int i) {
		int d = 0;
		for (; (i >> d) > 0; d++)
			;
		for (d -= 2; d >= 0; d--) {
			int x = i >> d;
			value[x] = joinValueWithDelta(value[x], deltaEffectOnSegment(delta[x >> 1], len[x]));
			value[(x ^ 1)] = joinValueWithDelta(value[(x ^ 1)], deltaEffectOnSegment(delta[x >> 1], len[(x ^ 1)]));
			delta[x] = joinDeltas(this.delta[x], delta[x >> 1]);
			delta[(x ^ 1)] = joinDeltas(this.delta[(x ^ 1)], delta[x >> 1]);
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

	public void modify(int from, int to, int delta) {
		from += value.length >> 1;
		to += value.length >> 1;
		pushDelta(from);
		pushDelta(to);
		int ta = -1;
		int tb = -1;
		for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
			if ((from & 1) != 0) {
				value[from] = joinValueWithDelta(value[from], deltaEffectOnSegment(delta, len[from]));
				this.delta[from] = joinDeltas(this.delta[from], delta);
				if (ta == -1)
					ta = from;
			}
			if ((to & 1) == 0) {
				value[to] = joinValueWithDelta(value[to], deltaEffectOnSegment(delta, len[to]));
				this.delta[to] = joinDeltas(this.delta[to], delta);
				if (tb == -1)
					tb = to;
			}
		}
		for (int i = ta; i > 1; i >>= 1)
			value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
		for (int i = tb; i > 1; i >>= 1)
			value[i >> 1] = queryOperation(value[i], value[i ^ 1]);
	}

	public int query(int from, int to) {
		from += value.length >> 1;
		to += value.length >> 1;
		pushDelta(from);
		pushDelta(to);
		int res = 0;
		boolean found = false;
		for (; from <= to; from = (from + 1) >> 1, to = (to - 1) >> 1) {
			if ((from & 1) != 0) {
				res = found ? queryOperation(res, value[from]) : value[from];
				found = true;
			}
			if ((to & 1) == 0) {
				res = found ? queryOperation(res, value[to]) : value[to];
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
						x[j] = t.joinValueWithDelta(x[j], delta);
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
