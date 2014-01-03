package experimental.trees;
public class SegmentTreeIntervalAddSum {

	int n;
	int[] tsum;
	int[] tadd;

	public SegmentTreeIntervalAddSum(int n) {
		this.n = n;
		tsum = new int[4 * n];
		tadd = new int[4 * n];
	}

	void push(int node, int left, int right) {
		tsum[node] += tadd[node] * (right - left + 1);
		if (left < right) {
			tadd[node * 2] += tadd[node];
			tadd[node * 2 + 1] += tadd[node];
		}
		tadd[node] = 0;
	}

	void pop(int node, int left, int right) {
		int mid = (left + right) >> 1;
		tsum[node] = tsum[node * 2] + tadd[node * 2] * (mid - left + 1);
		tsum[node] += tsum[node * 2 + 1] + tadd[node * 2 + 1] * (right - mid);
	}

	void add(int a, int b, int value) {
		add(a, b, value, 1, 0, n - 1);

	}

	void add(int a, int b, int value, int node, int left, int right) {
		push(node, left, right);
		if (left >= a && right <= b) {
			tadd[node] += value;
			return;
		}
		int mid = (left + right) >> 1;
		if (a <= mid)
			add(a, b, value, node * 2, left, mid);
		if (b > mid)
			add(a, b, value, node * 2 + 1, mid + 1, right);
		pop(node, left, right);
	}

	int sum(int a, int b) {
		return sum(a, b, 1, 0, n - 1);
	}

	int sum(int a, int b, int node, int left, int right) {
		push(node, left, right);
		if (left >= a && right <= b)
			return tsum[node];
		int mid = (left + right) >> 1;
		int res = 0;
		if (a <= mid)
			res += sum(a, b, node * 2, left, mid);
		if (b > mid)
			res += sum(a, b, node * 2 + 1, mid + 1, right);
//		pop(node, left, right);
		return res;
	}

	int get(int i) {
		return get(i, 1, 0, n - 1);
	}

	int get(int i, int node, int left, int right) {
		push(node, left, right);
		if (left == right)
			return tsum[node];
		int mid = (left + right) >> 1;
		int res = i <= mid ? get(i, node * 2, left, mid) : get(i, node * 2 + 1, mid + 1, right);
//		pop(node, left, right);
		return res;
	}

	void set(int i, int value) {
		add(i, i, -get(i) + value);
	}

	public static void main(String[] args) {
		SegmentTreeIntervalAddSum t = new SegmentTreeIntervalAddSum(10);
		t.add(0, 0, 1);
		t.set(0, 4);
		t.set(1, 5);
		t.set(2, 5);
		t.add(2, 2, 5);
		t.add(0, 2, 1);

		System.out.println(5 == t.get(0));
		System.out.println(22 == t.sum(0, 2));
		System.out.println(17 == t.sum(1, 2));
	}
}
