public class SegmentTree {
	int[] v;
	int[] s;
	int n;

	void init(int n) {
		this.n = n;
		s = new int[4 * n];
		buildTree(1, 0, n - 1);
	}

	void buildTree(int node, int left, int right) {
		if (left == right) {
			if (v != null)
				s[node] = v[left];
		} else {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
			s[node] = s[node * 2] + s[node * 2 + 1];
		}
	}

	public SegmentTree(int n) {
		init(n);
	}

	public SegmentTree(int[] v) {
		this.v = v;
		init(v.length);
	}

	// Returns the smallest pos <= b such that sum[a,pos] >= sum
	public int search(int a, int b, int sum) {
		return search(1, 0, n - 1, a, b, sum);
	}

	int search(int node, int left, int right, int a, int b, int sum) {
		if (left > b || right < a)
			return ~0;
		if (left >= a && right <= b && s[node] < sum)
			return ~s[node];
		if (left == right)
			return left;
		int mid = (left + right) >> 1;
		int res1 = search(node * 2, left, mid, a, b, sum);
		if (res1 >= 0)
			return res1;
		res1 = ~res1;
		int res2 = search(node * 2 + 1, mid + 1, right, a, b, sum - res1);
		if (res2 >= 0)
			return res2;
		res2 = ~res2;
		return ~(res1 + res2);
	}

	public void add(int i, int value) {
		add(1, 0, n - 1, i, value);
	}

	void add(int node, int left, int right, int i, int value) {
		if (left == right) {
			s[node] += value;
			return;
		}
		int mid = (left + right) >> 1;
		if (i <= mid)
			add(node * 2, left, mid, i, value);
		else
			add(node * 2 + 1, mid + 1, right, i, value);
		s[node] = s[node * 2] + s[node * 2 + 1];
	}

	public int sum(int a, int b) {
		return sum(1, 0, n - 1, a, b);
	}

	int sum(int node, int left, int right, int a, int b) {
		if (left > b || right < a)
			return 0;
		if (left >= a && right <= b)
			return s[node];
		int mid = (left + right) >> 1;
		int l = sum(node * 2, left, mid, a, b);
		int r = sum(node * 2 + 1, mid + 1, right, a, b);
		return l + r;
	}

	public int get(int i) {
		return sum(i, i);
	}

	public void set(int i, int value) {
		add(i, -get(i) + value);
	}

	public static void main(String[] args) {
		int[] a = { 1, 5, 2, 3 };
		SegmentTree tree = new SegmentTree(a);
		System.out.println(1 == tree.search(1, 3, 5));
		tree.set(1, 3);
		System.out.println(3 == tree.get(1));
		System.out.println(2 == tree.search(1, 3, 5));
	}
}
