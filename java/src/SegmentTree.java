import java.util.*;

public class SegmentTree {

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
	int n;
	int[] value;
	int[] delta; // delta[i] affects value[i], delta[2*i+1] and delta[2*i+2]

	void pushDelta(int root, int left, int right) {
		value[root] = modifyOperation(value[root], deltaEffectOnSegment(delta[root], right - left + 1));
		delta[2 * root + 1] = modifyOperation(delta[2 * root + 1], delta[root]);
		delta[2 * root + 2] = modifyOperation(delta[2 * root + 2], delta[root]);
		delta[root] = getNeutralDelta();
	}

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
			int mid = (left + right) >> 1;
			init(2 * root + 1, left, mid);
			init(2 * root + 2, mid + 1, right);
			value[root] = queryOperation(value[2 * root + 1], value[2 * root + 2]);
			delta[root] = getNeutralDelta();
		}
	}

	public int query(int a, int b) {
		return query(a, b, 0, 0, n - 1);
	}

	int query(int a, int b, int root, int left, int right) {
		if (a == left && b == right)
			return modifyOperation(value[root], deltaEffectOnSegment(delta[root], right - left + 1));
		pushDelta(root, left, right);
		int mid = (left + right) >> 1;
		if (a <= mid && b > mid)
			return queryOperation(
					query(a, Math.min(b, mid), root * 2 + 1, left, mid),
					query(Math.max(a, mid + 1), b, root * 2 + 2, mid + 1, right));
		else if (a <= mid)
			return query(a, Math.min(b, mid), root * 2 + 1, left, mid);
		else if (b > mid)
			return query(Math.max(a, mid + 1), b, root * 2 + 2, mid + 1, right);
		throw new RuntimeException("Incorrect query from " + a + " to " + b);
	}

	public void modify(int a, int b, int delta) {
		modify(a, b, delta, 0, 0, n - 1);
	}

	void modify(int a, int b, int delta, int root, int left, int right) {
		if (a == left && b == right) {
			this.delta[root] = modifyOperation(this.delta[root], delta);
			return;
		}
		pushDelta(root, left, right);
		int mid = (left + right) >> 1;
		if (a <= mid)
			modify(a, Math.min(b, mid), delta, 2 * root + 1, left, mid);
		if (b > mid)
			modify(Math.max(a, mid + 1), b, delta, 2 * root + 2, mid + 1, right);
		value[root] = queryOperation(
				modifyOperation(value[2 * root + 1], deltaEffectOnSegment(this.delta[2 * root + 1], mid - left + 1)),
				modifyOperation(value[2 * root + 2], deltaEffectOnSegment(this.delta[2 * root + 2], right - mid)));
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
