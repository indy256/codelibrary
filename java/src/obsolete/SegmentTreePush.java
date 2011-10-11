package obsolete;
import java.util.*;

public class SegmentTreePush {
	int[] v;
	int[] s;
	int[] min;
	boolean[] isFilled;
	int[] fillv;
	int[] addv;
	int n;

	void init() {
		int len = 4 * n;
		s = new int[len];
		isFilled = new boolean[len];
		fillv = new int[len];
		addv = new int[len];
		min = new int[len];
		Arrays.fill(min, Integer.MAX_VALUE);
		buildTree(1, 0, n - 1);
	}

	void buildTree(int node, int left, int right) {
		if (left == right) {
			if (v != null)
				s[node] = min[node] = v[left];
		} else {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
			update(node, left, right);
		}
	}

	public SegmentTreePush(int n) {
		this.n = n;
		init();
	}

	public SegmentTreePush(int[] v) {
		this.v = v;
		this.n = v.length;
		init();
	}

	void update(int node, int left, int right) {
		int n0 = node * 2;
		int n1 = node * 2 + 1;
		int mid = (left + right) >> 1;
		s[node] = s[n0] + addv[n0] * (mid - left + 1) + s[n1] + addv[n1] * (right - mid);
		min[node] = Math.min(min[n0] + addv[n0], min[n1] + addv[n1]);
	}

	void push(int node, int left, int right) {
		int toAdd = addv[node];
		addv[node] = 0;
		s[node] += toAdd;
		min[node] += toAdd;
		if (left < right) {
			addv[node * 2] += toAdd;
			addv[node * 2 + 1] += toAdd;
		}
		if (!isFilled[node])
			return;
		isFilled[node] = false;
		fillv[node] += toAdd;
		s[node] = fillv[node] * (right - left + 1);
		min[node] = fillv[node];
		if (left < right) {
			fillv[node * 2] = fillv[node * 2 + 1] = fillv[node];
			addv[node * 2] = addv[node * 2 + 1] = 0;
			isFilled[node * 2] = isFilled[node * 2 + 1] = true;
		}
	}

	public int get(int i) {
		return get(1, 0, n - 1, i);
	}

	int get(int node, int left, int right, int i) {
		push(node, left, right);
		if (left == right)
			return s[node] + addv[node];
		int mid = (left + right) >> 1;
		if (i <= mid)
			return get(node * 2, left, mid, i);
		else
			return get(node * 2 + 1, mid + 1, right, i);
	}

	public void set(int i, int value) {
		add(i, i, -get(i) + value);
	}

	public void add(int a, int b, int value) {
		add(1, 0, n - 1, a, b, value);
	}

	void add(int node, int left, int right, int a, int b, int value) {
		if (left > b || right < a)
			return;
		push(node, left, right);
		if (left >= a && right <= b) {
			addv[node] += value;
			return;
		}
		int mid = (left + right) >> 1;
		add(node * 2, left, mid, a, b, value);
		add(node * 2 + 1, mid + 1, right, a, b, value);
		update(node, left, right);
	}

	public void fill(int a, int b, int value) {
		fill(1, 0, n - 1, a, b, value);
	}

	void fill(int node, int left, int right, int a, int b, int value) {
		if (left > b || right < a)
			return;
		if (left >= a && right <= b) {
			isFilled[node] = true;
			fillv[node] = value;
			addv[node] = 0;
			push(node, left, right);
			return;
		}
		int mid = (left + right) >> 1;
		fill(node * 2, left, mid, a, b, value);
		fill(node * 2 + 1, mid + 1, right, a, b, value);
		update(node, left, right);
	}

	public int sum(int a, int b) {
		return sum(1, 0, n - 1, a, b);
	}

	int sum(int node, int left, int right, int a, int b) {
		if (left > b || right < a)
			return 0;
		push(node, left, right);
		if (left >= a && right <= b)
			return s[node] + addv[node] * (right - left + 1);
		int mid = (left + right) >> 1;
		int l = sum(node * 2, left, mid, a, b);
		int r = sum(node * 2 + 1, mid + 1, right, a, b);
		return l + r;
	}

	public int minv(int a, int b) {
		return minv(1, 0, n - 1, a, b);
	}

	int minv(int node, int left, int right, int a, int b) {
		if (left > b || right < a)
			return Integer.MAX_VALUE;
		push(node, left, right);
		if (left >= a && right <= b)
			return min[node] + addv[node];
		int mid = (left + right) >> 1;
		int l = minv(node * 2, left, mid, a, b);
		int r = minv(node * 2 + 1, mid + 1, right, a, b);
		return Math.min(l, r);
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreePush tree = new SegmentTreePush(4);
		tree.set(0, 2);
		tree.set(1, 1);
		tree.set(2, 3);
		System.out.println(6 == tree.sum(0, 3));

		tree = new SegmentTreePush(new int[] { 1, 2, 3 });
		System.out.println(6 == tree.sum(0, 2));
		System.out.println(2 == tree.get(1));
		tree.fill(0, 2, 3);
		tree.add(0, 2, 10);
		tree.fill(1, 1, 3);
		tree.add(0, 1, 2);
		System.out.println(15 == tree.get(0));
	}
}
