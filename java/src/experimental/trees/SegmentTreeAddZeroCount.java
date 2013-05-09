package experimental.trees;
public class SegmentTreeAddZeroCount {
	int[] add;
	int[] minCount;
	int[] min;
	int n;

	public SegmentTreeAddZeroCount(int n) {
		this.n = n;
		add = new int[4 * n];
		minCount = new int[4 * n];
		min = new int[4 * n];
		buildTree(1, 0, n - 1);
	}

	void buildTree(int node, int left, int right) {
		minCount[node] = right - left + 1;
		if (left < right) {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
		}
	}

	// tree must not contain negative elements
	public void add(int a, int b, int value) {
		add(a, b, value, 1, 0, n - 1);
	}

	void add(int a, int b, int value, int node, int left, int right) {
		if (left > b || right < a)
			return;
		if (left >= a && right <= b) {
			add[node] += value;
			return;
		}
		int mid = (left + right) >> 1;
		int n0 = node * 2;
		int n1 = node * 2 + 1;
		add(a, b, value, n0, left, mid);
		add(a, b, value, n1, mid + 1, right);

		min[node] = Math.min(min[n0] + add[n0], min[n1] + add[n1]);
		minCount[node] = (min[node] == min[n0] + add[n0] ? minCount[n0] : 0)
				+ (min[node] == min[n1] + add[n1] ? minCount[n1] : 0);
	}

	public int zeroCount(int a, int b) {
		return zeroCount(a, b, 0, 1, 0, n - 1);
	}

	int zeroCount(int a, int b, int sumAdd, int node, int left, int right) {
		sumAdd += add[node];
		if (left >= a && right <= b)
			return min[node] + sumAdd == 0 ? minCount[node] : 0;
		int mid = (left + right) >> 1;
		int res = 0;
		if (a <= mid)
			res += zeroCount(a, b, sumAdd, node * 2, left, mid);
		if (b > mid)
			res += zeroCount(a, b, sumAdd, node * 2 + 1, mid + 1, right);
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeAddZeroCount t = new SegmentTreeAddZeroCount(4);
		t.add(0, 0, 1);
		t.add(3, 3, 1);
		System.out.println(2 == t.zeroCount(0, 3));
	}
}
