package experimental;

import java.util.Random;

public class SegmentTreeSum {

	int n;
	int[] sum;

	public SegmentTreeSum(int n) {
		this.n = n;
		sum = new int[Integer.highestOneBit(n) << 2];
	}

	void pushDelta(int root, int left, int right) {
		int middle = (left + right) / 2;
		int len = (right - left + 1);
		int delta = (sum[root] - sum[2 * root + 1] - sum[2 * root + 2]) / len;
		sum[2 * root + 1] += delta * (middle - left + 1);
		sum[2 * root + 2] += delta * (right - middle);
	}

	public int sum(int a, int b) {
		return sum(a, b, 0, 0, n - 1);
	}

	int sum(int a, int b, int root, int left, int right) {
		if (a > right || b < left)
			return 0;
		if (a <= left && right <= b)
			return sum[root];
		pushDelta(root, left, right);
		return sum(a, b, root * 2 + 1, left, (left + right) / 2) + sum(a, b, root * 2 + 2, (left + right) / 2 + 1, right);
	}

	public void add(int a, int b, int value) {
		add(a, b, value, 0, 0, n - 1);
	}

	void add(int a, int b, int value, int root, int left, int right) {
		if (a > right || b < left)
			return;
		if (a <= left && right <= b) {
			sum[root] += value * (right - left + 1);
			return;
		}
		pushDelta(root, left, right);
		int middle = (left + right) / 2;
		add(a, b, value, 2 * root + 1, left, middle);
		add(a, b, value, 2 * root + 2, middle + 1, right);
		sum[root] = sum[2 * root + 1] + sum[2 * root + 2];
	}

	// Random test
	public static void main(String[] args) {
		Random rnd = new Random();
		for (int step = 0; step < 1000; step++) {
			int n = rnd.nextInt(50) + 1;
			int[] x = new int[n];
			SegmentTreeSum t = new SegmentTreeSum(n);
			for (int i = 0; i < 1000; i++) {
				int b = rnd.nextInt(n);
				int a = rnd.nextInt(b + 1);
				int cmd = rnd.nextInt(3);
				if (cmd == 0) {
					int delta = rnd.nextInt(100) - 50;
					t.add(a, b, delta);
					for (int j = a; j <= b; j++)
						x[j] += delta;
				} else if (cmd == 1) {
					int res1 = t.sum(a, b);
					int res2 = x[a];
					for (int j = a + 1; j <= b; j++)
						res2 += x[j];
					if (res1 != res2)
						throw new RuntimeException("error");
				} else {
					for (int j = 0; j < n; j++) {
						if (t.sum(j, j) != x[j])
							throw new RuntimeException("error");
					}
				}
			}
		}
		System.out.println("Test passed");
	}
}
