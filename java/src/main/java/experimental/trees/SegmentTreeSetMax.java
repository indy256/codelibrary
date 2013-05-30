package experimental.trees;
import java.util.*;

public class SegmentTreeSetMax {
	int[] v;
	int[] maxPos;
	int n;

	public SegmentTreeSetMax(int n) {
		this.n = n;
		v = new int[n * 4];
		Arrays.fill(v, Integer.MIN_VALUE);
		maxPos = new int[n * 4];
		buildTree(1, 0, n - 1);
	}

	void buildTree(int node, int left, int right) {
		if (left == right) {
			maxPos[node] = left;
		} else {
			int mid = (left + right) >> 1;
			buildTree(node * 2, left, mid);
			buildTree(node * 2 + 1, mid + 1, right);
			maxPos[node] = v[maxPos[node * 2]] >= v[maxPos[node * 2 + 1]] ? maxPos[node * 2] : maxPos[node * 2 + 1];
		}
	}

	public int maxPos(int a, int b) {
		return maxPos(a, b, 1, 0, n - 1);
	}

	int maxPos(int a, int b, int node, int left, int right) {
		if (left >= a && right <= b)
			return maxPos[node];
		int mid = (left + right) >> 1;
		int p1 = a <= mid ? maxPos(a, b, node * 2, left, mid) : -1;
		int p2 = b > mid ? maxPos(a, b, node * 2 + 1, mid + 1, right) : -1;
		if (p1 == -1)
			return p2;
		if (p2 == -1)
			return p1;
		return v[maxPos[p1]] >= v[maxPos[p2]] ? p1 : p2;
	}

	public void set(int i, int value) {
		set(i, value, 1, 0, n - 1);
	}

	void set(int i, int value, int node, int left, int right) {
		if (left == right) {
			v[left] = value;
			return;
		}
		int mid = (left + right) >> 1;
		if (i <= mid)
			set(i, value, node * 2, left, mid);
		else
			set(i, value, node * 2 + 1, mid + 1, right);
		maxPos[node] = v[maxPos[node * 2]] >= v[maxPos[node * 2 + 1]] ? maxPos[node * 2] : maxPos[node * 2 + 1];
	}

	public int get(int i) {
		return v[i];
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeSetMax t = new SegmentTreeSetMax(4);
		t.set(0, -1);
		t.set(1, -5);
		t.set(2, 2);
		t.set(3, -3);
		System.out.println(2 == t.maxPos(0, 3));
		t.set(2, -2);
		System.out.println(0 == t.maxPos(0, 3));
		System.out.println(-2 == t.get(2));
	}
}
