import java.util.*;

public class SegmentTree {

	// Modify the following 6 methods to implement your custom operations on the tree. Pay attention to contracts
	int queryOperation(int x, int y) {
		return Math.max(x, y);
	}

	int modifyOperation(int x, int y) {
		return x + y;
	}

	int totalDeltaEffect(int delta, int count) {
		// contract: totalDeltaEffect(delta, count) == queryOperation(delta, queryOperation(delta, ...count times))
		return delta; // delta * count (for sum queryOperation)
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

	// generic tree code
	int joinValues(int leftValue, int rightValue) {
		return queryOperation(leftValue, rightValue);
	}

	int joinDeltas(int oldDelta, int newDelta) {
		return modifyOperation(oldDelta, newDelta);
	}

	int joinValueWithDelta(int value, int delta, int length) {
		return modifyOperation(value, totalDeltaEffect(delta, length));
	}

	int n;
	int[] value;
	int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

	public SegmentTree(int n) {
		this.n = n;
		value = new int[4 * n];
		delta = new int[4 * n];
		init(0, 0, n - 1);
	}

	void init(int root, int left, int right) {
		if (left == right) {
			value[root] = getInitValue();
			delta[root] = getNeutralDelta();
		} else {
			init(2 * root + 1, left, (left + right) / 2);
			init(2 * root + 2, (left + right) / 2 + 1, right);
			value[root] = joinValues(value[2 * root + 1], value[2 * root + 2]);
			delta[root] = getNeutralDelta();
		}
	}

	void pushDelta(int root, int left, int right) {
		value[root] = joinValueWithDelta(value[root], delta[root], right - left + 1);
		delta[2 * root + 1] = joinDeltas(delta[2 * root + 1], delta[root]);
		delta[2 * root + 2] = joinDeltas(delta[2 * root + 2], delta[root]);
		delta[root] = getNeutralDelta();
	}

	public int query(int a, int b) {
		return query(a, b, 0, 0, n - 1);
	}

	int query(int a, int b, int root, int left, int right) {
		if (a > right || b < left)
			return getNeutralValue();
		if (a <= left && right <= b)
			return joinValueWithDelta(value[root], delta[root], right - left + 1);
		pushDelta(root, left, right);
		return joinValues(query(a, b, root * 2 + 1, left, (left + right) / 2),
				query(a, b, root * 2 + 2, (left + right) / 2 + 1, right));
	}

	public void modify(int a, int b, int delta) {
		modify(a, b, delta, 0, 0, n - 1);
	}

	void modify(int a, int b, int delta, int root, int left, int right) {
		if (a > right || b < left)
			return;
		if (a <= left && right <= b) {
			this.delta[root] = joinDeltas(this.delta[root], delta);
			return;
		}
		pushDelta(root, left, right);
		int middle = (left + right) / 2;
		modify(a, b, delta, 2 * root + 1, left, middle);
		modify(a, b, delta, 2 * root + 2, middle + 1, right);
		value[root] = joinValues(joinValueWithDelta(value[2 * root + 1], this.delta[2 * root + 1], middle - left + 1),
				joinValueWithDelta(value[2 * root + 2], this.delta[2 * root + 2], right - middle));
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			int[] x = new int[n];
			SegmentTree t = new SegmentTree(n);
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
