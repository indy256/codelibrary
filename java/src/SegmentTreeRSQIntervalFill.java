import java.util.Arrays;

public class SegmentTreeRSQIntervalFill {

	final int n;
	final int[] fillv;
	final int[] tsum;

	public SegmentTreeRSQIntervalFill(int n) {
		this.n = n;
		fillv = new int[4 * n];
		Arrays.fill(fillv, -1);
		tsum = new int[4 * n];
	}

	void push(int node, int left, int right) {
		if (fillv[node] == -1)
			return;
		tsum[node] = fillv[node] * (right - left + 1);
		if (left < right) {
			fillv[node * 2] = fillv[node];
			fillv[node * 2 + 1] = fillv[node];
		}
		fillv[node] = -1;
	}

	void pullUp(int node, int left, int right) {
		int mid = (left + right) >> 1;
		tsum[node] = (fillv[node * 2] != -1) ? fillv[node * 2] * (mid - left + 1) : tsum[node * 2];
		tsum[node] += (fillv[node * 2 + 1] != -1) ? fillv[node * 2 + 1] * (right - mid) : tsum[node * 2 + 1];
	}

	public void add(int i, int value) {
		add(i, value, 1, 0, n - 1);
	}

	void add(int i, int value, int node, int left, int right) {
		push(node, left, right);
		if (left == right) {
			tsum[node] += value;
			return;
		}
		int mid = (left + right) >> 1;
		if (i <= mid)
			add(i, value, node * 2, left, mid);
		else
			add(i, value, node * 2 + 1, mid + 1, right);
		pullUp(node, left, right);
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
		pullUp(node, left, right);
		return res;
	}

	int get(int i) {
		return sum(i, i);
	}

	void set(int i, int value) {
		add(i, -get(i) + value);
	}

	void fill(int a, int b, int value) {
		fill(a, b, value, 1, 0, n - 1);
	}

	void fill(int a, int b, int value, int node, int left, int right) {
		push(node, left, right);
		if (left >= a && right <= b) {
			fillv[node] = value;
			return;
		}
		int mid = (left + right) >> 1;
		if (a <= mid)
			fill(a, b, value, node * 2, left, mid);
		if (b > mid)
			fill(a, b, value, node * 2 + 1, mid + 1, right);
		pullUp(node, left, right);
	}

	public static void main(String[] args) {
		SegmentTreeRSQIntervalFill t = new SegmentTreeRSQIntervalFill(10);
		t.set(0, 4);
		t.set(1, 5);
		t.add(1, 5);
		System.out.println(4 == t.get(0));
		System.out.println(10 == t.get(1));

		t.fill(0, 1, 1);
		System.out.println(1 == t.get(0));
		System.out.println(1 == t.get(1));
		System.out.println(0 == t.get(2));
	}
}
