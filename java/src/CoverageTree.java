public class CoverageTree {
	int[] add;
	int[] minCount;
	int[] min;
	int n;

	void buildTree(int node, int left, int right) {
		minCount[node] = right - left + 1;
		if (left < right) {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
		}
	}

	public CoverageTree(int n) {
		this.n = n;
		int len = 4 * n;
		add = new int[len];
		minCount = new int[len];
		min = new int[len];
		buildTree(1, 0, n - 1);
	}

	// tree must not contain negative elements
	public void add(int a, int b, int value) {
		add(1, 0, n - 1, a, b, value);
	}

	void add(int node, int left, int right, int a, int b, int value) {
		if (left > b || right < a)
			return;
		if (left >= a && right <= b) {
			add[node] += value;
			return;
		}
		int mid = (left + right) >> 1;
		int n0 = node * 2;
		int n1 = node * 2 + 1;
		add(n0, left, mid, a, b, value);
		add(n1, mid + 1, right, a, b, value);

		min[node] = Math.min(min[n0] + add[n0], min[n1] + add[n1]);
		minCount[node] = (min[node] == min[n0] + add[n0] ? minCount[n0] : 0)
				+ (min[node] == min[n1] + add[n1] ? minCount[n1] : 0);
	}

	// returns number of zeros in [a, b]
	public int coverage(int a, int b) {
		return coverage(1, 0, n - 1, a, b, 0);
	}

	int coverage(int node, int left, int right, int a, int b, int sumAdd) {
		if (left > b || right < a)
			return 0;
		sumAdd += add[node];
		if (left >= a && right <= b)
			return min[node] + sumAdd == 0 ? minCount[node] : 0;
		int mid = (left + right) >> 1;
		int l = coverage(node * 2, left, mid, a, b, sumAdd);
		int r = coverage(node * 2 + 1, mid + 1, right, a, b, sumAdd);
		return l + r;
	}

	// Usage example
	public static void main(String[] args) {
		CoverageTree tree = new CoverageTree(4);
		tree.add(0, 0, 1);
		tree.add(3, 3, 1);
		System.out.println(tree.coverage(0, 3));
	}
}
