public class SegmentTreeKth {
	int n;
	int[] sum;

	public SegmentTreeKth(int n) {
		this.n = n;
		sum = new int[4 * n];
	}

	public void add(int i, int delta) {
		add(i, delta, 0, 0, n - 1);
	}

	void add(int i, int delta, int root, int left, int right) {
		if (left == right) {
			sum[root] += delta;
			return;
		}
		int mid = (left + right) >> 1;
		if (i <= mid)
			add(i, delta, 2 * root + 1, left, mid);
		else
			add(i, delta, 2 * root + 2, mid + 1, right);
		sum[root] = sum[2 * root + 1] + sum[2 * root + 2];
	}

	public int getKth(int k) {
		return getKth(k, 0, 0, n - 1);
	}

	int getKth(int k, int root, int left, int right) {
		if (left == right) {
			return left;
		}
		int mid = (left + right) >> 1;
		if (sum[2 * root + 1] >= k)
			return getKth(k, 2 * root + 1, left, mid);
		else
			return getKth(k - sum[2 * root + 1], 2 * root + 2, mid + 1, right);
	}

	// tests
	public static void main(String[] args) {
		SegmentTreeKth t = new SegmentTreeKth(10);
		t.add(1, 1);
		System.out.println(t.getKth(1));
	}
}
