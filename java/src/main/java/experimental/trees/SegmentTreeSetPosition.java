package experimental.trees;
public class SegmentTreeSetPosition {
	int[] zeros;
	int n;

	public SegmentTreeSetPosition(int n) {
		this.n = n;
		zeros = new int[4 * n];
		buildTree(1, 0, n - 1);
	}

	void buildTree(int node, int left, int right) {
		zeros[node] = right - left + 1;
		if (left < right) {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
		}
	}

	public void setUsed(int pos) {
		setUsed(pos, 1, 0, n - 1);
	}

	void setUsed(int pos, int node, int left, int right) {
		--zeros[node];
		if (left == right)
			return;
		int mid = (left + right) >> 1;
		if (pos <= mid)
			setUsed(pos, node * 2, left, mid);
		else
			setUsed(pos, node * 2 + 1, mid + 1, right);
	}

	public int freeCount(int a, int b) {
		return freeCount(a, b, 1, 0, n - 1);
	}

	int freeCount(int a, int b, int node, int left, int right) {
		if (left >= a && right <= b)
			return zeros[node];
		int mid = (left + right) >> 1;
		int res = 0;
		if (a <= mid)
			res += freeCount(a, b, node * 2, left, mid);
		if (b > mid)
			res += freeCount(a, b, node * 2 + 1, mid + 1, right);
		return res;
	}

	public int kthUnused(int k) {
		return kthUnused(k, 1, 0, n - 1);
	}

	int kthUnused(int k, int node, int left, int right) {
		if (k > zeros[node])
			return -1;
		if (left == right)
			return left;
		int m = (left + right) >> 1;
		if (zeros[node * 2] >= k)
			return kthUnused(k, node * 2, left, m);
		else
			return kthUnused(k - zeros[node * 2], node * 2 + 1, m + 1, right);
	}

	public int firstUnused(int a, int b) {
		return firstUnused(1, 0, n - 1, a, b);
	}

	int firstUnused(int node, int left, int right, int a, int b) {
		if (a > right || b < left || zeros[node] == 0)
			return -1;
		if (left == right)
			return left;
		int mid = (left + right) >> 1;
		int res = firstUnused(node * 2, left, mid, a, b);
		return res < 0 ? firstUnused(node * 2 + 1, mid + 1, right, a, b) : res;
	}

	public int kthUnused(int a, int b, int k) {
		return kthUnused(1, 0, n - 1, a, b, k);
	}

	int kthUnused(int node, int left, int right, int a, int b, int k) {
		if (left > b || right < a)
			return ~0;
		if (left >= a && right <= b) {
			int unused = zeros[node];
			if (unused < k)
				return ~unused;
		}
		if (left == right)
			return left;
		int mid = (left + right) >> 1;
		int res1 = kthUnused(node * 2, left, mid, a, b, k);
		if (res1 >= 0)
			return res1;
		res1 = ~res1;
		int res2 = kthUnused(node * 2 + 1, mid + 1, right, a, b, k - res1);
		if (res2 >= 0)
			return res2;
		res2 = ~res2;
		return ~(res1 + res2);
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeSetPosition t = new SegmentTreeSetPosition(5);
		t.setUsed(1);
		t.setUsed(3);
		System.out.println(4 == t.kthUnused(3));
		System.out.println(2 == t.firstUnused(2, 4));
		System.out.println(4 == t.kthUnused(0, 4, 3));
	}
}
