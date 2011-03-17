import java.util.*;

public class SegmentTreeRMQ {
	int[] v;
	int[] minPos;
	int n;

	void init() {
		this.n = v.length;
		minPos = new int[4 * n];
		buildTree(1, 0, n - 1);
	}

	void buildTree(int node, int left, int right) {
		if (left == right) {
			minPos[node] = left;
		} else {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
			minPos[node] = v[minPos[node * 2]] <= v[minPos[node * 2 + 1]] ? minPos[node * 2] : minPos[node * 2 + 1];
		}
	}

	public SegmentTreeRMQ(int n) {
		v = new int[n];
		Arrays.fill(v, Integer.MAX_VALUE);
		init();
	}

	public SegmentTreeRMQ(int[] v) {
		this.v = v;
		init();
	}

	public int minPos(int a, int b) {
		return minPos(1, 0, n - 1, a, b);
	}

	int minPos(int node, int left, int right, int a, int b) {
		if (left > b || right < a)
			return -1;
		if (left >= a && right <= b)
			return minPos[node];
		int mid = (left + right) >> 1;
		int p1 = minPos(node * 2, left, mid, a, b);
		int p2 = minPos(node * 2 + 1, mid + 1, right, a, b);
		if (p1 == -1)
			return p2;
		if (p2 == -1)
			return p1;
		return v[minPos[p1]] <= v[minPos[p2]] ? p1 : p2;
	}

	public void set(int i, int value) {
		set(1, 0, n - 1, i, value);
	}

	void set(int node, int left, int right, int i, int value) {
		if (left > i || right < i)
			return;
		if (left == right) {
			v[left] = value;
			return;
		}
		int mid = (left + right) >> 1;
		set(node * 2, left, mid, i, value);
		set(node * 2 + 1, mid + 1, right, i, value);
		minPos[node] = v[minPos[node * 2]] <= v[minPos[node * 2 + 1]] ? minPos[node * 2] : minPos[node * 2 + 1];
	}

	public int get(int i) {
		return v[i];
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeRMQ tree = new SegmentTreeRMQ(new int[] { 1, 5, -2, 3 });
		System.out.println(2 == tree.minPos(0, 3));
		tree.set(2, 2);
		System.out.println(0 == tree.minPos(0, 3));
		System.out.println(2 == tree.get(2));
	}
}
