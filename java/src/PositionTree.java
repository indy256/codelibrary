public class PositionTree {
	int[] s;
	int n;

	public PositionTree(int n) {
		this.n = n;
		s = new int[4 * n];
	}

	public void setUsed(int pos) {
		setUsed(1, 0, n - 1, pos);
	}

	void setUsed(int node, int left, int right, int pos) {
		++s[node];
		if (left == right)
			return;
		int mid = (left + right) >> 1;
		if (pos <= mid)
			setUsed(node * 2, left, mid, pos);
		else
			setUsed(node * 2 + 1, mid + 1, right, pos);
	}

	public int firstUnused(int a, int b) {
		return firstUnused(1, 0, n - 1, a, b);
	}

	int firstUnused(int node, int left, int right, int a, int b) {
		if (a > right || b < left || s[node] == right - left + 1)
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
			int unused = right - left + 1 - s[node];
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
		PositionTree tree = new PositionTree(5);
		tree.setUsed(1);
		tree.setUsed(3);
		int pos = tree.firstUnused(2, 4);
		System.out.println(2 == pos);
		pos = tree.kthUnused(0, 4, 3);
		System.out.println(4 == pos);
	}
}
