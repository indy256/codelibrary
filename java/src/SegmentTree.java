public class SegmentTree {

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

	static int joinValueDelta(int value, int delta, int length) {
		return value + delta;
	}

	// generic code
	int n;
	int[] value;
	int[] delta; // affects only child roots

	public SegmentTree(int n) {
		this.n = n;
		value = new int[4 * n];
		delta = new int[4 * n];
		init(0, 0, n - 1);
	}

	public void init(int root, int left, int right) {
		if (left == right) {
			value[root] = INIT_VALUE;
			delta[root] = NEUTRAL_DELTA;
		} else {
			init(2 * root + 1, left, (left + right) / 2);
			init(2 * root + 2, (left + right) / 2 + 1, right);
			value[root] = joinValues(value[2 * root + 1], value[2 * root + 2]);
			delta[root] = NEUTRAL_DELTA;
		}
	}

	void pushDelta(int root, int left, int right) {
		delta[2 * root + 1] = joinDeltas(delta[2 * root + 1], delta[root]);
		delta[2 * root + 2] = joinDeltas(delta[2 * root + 2], delta[root]);
		int middle = (left + right) / 2;
		value[2 * root + 1] = joinValueDelta(value[2 * root + 1], delta[root], middle - left + 1);
		value[2 * root + 2] = joinValueDelta(value[2 * root + 2], delta[root], right - middle);
		delta[root] = NEUTRAL_DELTA;
	}

	public int query(int a, int b) {
		return query(a, b, 0, 0, n - 1);
	}

	int query(int a, int b, int root, int left, int right) {
		if (a > right || b < left)
			return NEUTRAL_VALUE;
		if (a <= left && right <= b)
			return value[root];
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
			value[root] = joinValueDelta(value[root], delta, right - left + 1);
			return;
		}
		pushDelta(root, left, right);
		modify(a, b, delta, 2 * root + 1, left, (left + right) / 2);
		modify(a, b, delta, 2 * root + 2, (left + right) / 2 + 1, right);
		value[root] = joinValues(value[2 * root + 1], value[2 * root + 2]);
	}

	// Usage example
	public static void main(String[] args) {
		int n = 10;
		SegmentTree t = new SegmentTree(n);
		t.modify(4, 6, 1);
		System.out.println(0 == t.query(0, 3));
		t.modify(3, 7, 2);
		System.out.println(3 == t.query(6, 6));
		t.modify(0, 1, 9);
		System.out.println(3 == t.query(5, 7));
	}
}
