public class SegmentTreeKth {

	public static void add(int[] tree, int i, int delta, int root, int left, int right) {
		if (left == right) {
			tree[root] += delta;
			return;
		}
		int mid = (left + right) >> 1;
		if (i <= mid)
			add(tree, i, delta, 2 * root + 1, left, mid);
		else
			add(tree, i, delta, 2 * root + 2, mid + 1, right);
		tree[root] = tree[2 * root + 1] + tree[2 * root + 2];
	}

	public static int getKth(int[] tree, int k, int root, int left, int right) {
		if (left == right)
			return left;
		int mid = (left + right) >> 1;
		if (k <= tree[2 * root + 1])
			return getKth(tree, k, 2 * root + 1, left, mid);
		else
			return getKth(tree, k - tree[2 * root + 1], 2 * root + 2, mid + 1, right);
	}

	// tests
	public static void main(String[] args) {
		int n = 10;
		int[] tree = new int[4 * n];
		add(tree, 5, 1, 0, 0, n - 1);
		System.out.println(getKth(tree, 1, 0, 0, n - 1));
	}
}
