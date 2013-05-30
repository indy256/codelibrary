package experimental.trees;
import java.util.*;

public class SegmentTreeIntervalAddFill_Sum {
	int[] tfill;
	int[] tadd;
	int[] tsum;
	int[] tmin;
	int n;

	public SegmentTreeIntervalAddFill_Sum(int n) {
		this.n = n;
		tfill = new int[4 * n];
		Arrays.fill(tfill, -1);
		tadd = new int[4 * n];
		tsum = new int[4 * n];
		tmin = new int[4 * n];
	}

	void push(int node, int left, int right) {
		if (tfill[node] == -1)
			return;
		tsum[node] = tfill[node] * (right - left + 1);
		if (left < right) {
			tfill[node * 2] = tfill[node];
			tfill[node * 2 + 1] = tfill[node];
		}
		tfill[node] = -1;
		
		
		
	    tsum[node] += tadd[node] * (right - left + 1);
	    if (left < right) {
	        tadd[node * 2] += tadd[node];
	        tadd[node * 2 + 1] += tadd[node];
	    }
	    tadd[node] = 0;
	    
	    

		int toAdd = tadd[node];
		tadd[node] = 0;
		tsum[node] += toAdd;
		tmin[node] += toAdd;
		if (left < right) {
			tadd[node * 2] += toAdd;
			tadd[node * 2 + 1] += toAdd;
		}
//		if (!isFilled[node])
//			return;
//		isFilled[node] = false;
//		tfill[node] += toAdd;
//		s[node] = tfill[node] * (right - left + 1);
//		min[node] = tfill[node];
//		if (left < right) {
//			tfill[node * 2] = tfill[node * 2 + 1] = tfill[node];
//			tadd[node * 2] = tadd[node * 2 + 1] = 0;
//			isFilled[node * 2] = isFilled[node * 2 + 1] = true;
//		}
	}

	void pop(int node, int left, int right) {
		int mid = (left + right) >> 1;

		int a = (tfill[node * 2] != -1) ? tfill[node * 2] * (mid - left + 1) : tsum[node * 2] + tadd[node * 2]
				* (mid - left + 1);
		int b = (tfill[node * 2 + 1] != -1) ? tfill[node * 2 + 1] * (right - mid) : tsum[node * 2 + 1]
				+ tadd[node * 2 + 1] * (right - mid);

		tsum[node] = a + b;
		tmin[node] = Math.min(a, b);
	}

	public int get(int i) {
		return get(1, 0, n - 1, i);
	}

	int get(int node, int left, int right, int i) {
		push(node, left, right);
		if (left == right)
			return tsum[node];
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

	public void fill(int a, int b, int value) {
		fill(1, 0, n - 1, a, b, value);
	}

	void fill(int node, int left, int right, int a, int b, int value) {
		push(node, left, right);
		if (left >= a && right <= b) {
			tfill[node] = value;
			return;
		}
		int mid = (left + right) >> 1;
		if (a <= mid)
			fill(a, b, value, node * 2, left, mid);
		if (b > mid)
			fill(a, b, value, node * 2 + 1, mid + 1, right);
		pop(node, left, right);
	}

	public int sum(int a, int b) {
		return sum(1, 0, n - 1, a, b);
	}

	int sum(int node, int left, int right, int a, int b) {
		push(node, left, right);
		if (left >= a && right <= b)
			return tsum[node];
		int mid = (left + right) >> 1;
		int res = 0;
		if (a <= mid)
			res += sum(node * 2, left, mid, a, b);
		if (b > mid)
			res += sum(node * 2 + 1, mid + 1, right, a, b);
		pop(node, left, right);
		return res;
	}

	public int minv(int a, int b) {
		return minv(1, 0, n - 1, a, b);
	}

	int minv(int node, int left, int right, int a, int b) {
		push(node, left, right);
		if (left >= a && right <= b)
			return tmin[node];
		int mid = (left + right) >> 1;
		int res = Integer.MAX_VALUE;
		if (a <= mid)
			res = Math.min(res, minv(node * 2, left, mid, a, b));
		if (b > mid)
			res = Math.min(res, minv(node * 2 + 1, mid + 1, right, a, b));
		pop(node, left, right);
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeIntervalAddFill_Sum t = new SegmentTreeIntervalAddFill_Sum(4);
		t.set(0, 2);
		t.set(1, 1);
		t.set(2, 3);
		System.out.println(6 == t.sum(0, 3));

		t = new SegmentTreeIntervalAddFill_Sum(3);
		t.set(0, 1);
		t.set(1, 2);
		t.set(2, 3);
		System.out.println(6 == t.sum(0, 2));
		System.out.println(2 == t.get(1));
		t.fill(0, 2, 3);
		t.add(0, 2, 10);
		t.fill(1, 1, 3);
		t.add(0, 1, 2);
		System.out.println(15 == t.get(0));
	}
}
