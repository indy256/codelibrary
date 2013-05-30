package experimental.trees;
public class SegmentTreeFastAddSum {
	int n;
	int[] t;

	public SegmentTreeFastAddSum(int n) {
		this.n = n;
		t = new int[n + n];
	}

	public int get(int i) {
		return t[i + n];
	}

	public void set(int i, int value) {
		add(i, value - t[i + n]);
	}

	public void add(int i, int value) {
		for (i += n; i > 0; i >>= 1)
			t[i] += value;
	}

	// sum[a, b]
	public int sum(int a, int b) {
		int res = 0;
		for (a += n, b += n; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			if ((a & 1) != 0)
				res += t[a];
			if ((b & 1) == 0)
				res += t[b];
		}
		return res;
	}

	// sum[0, b]
	public int sum(int b) {
		if (b == n - 1)
			return t[1];
		int res = 0;
		for (b += n; b > 0; b = (b - 1) >> 1)
			if ((b & 1) == 0)
				res += t[b];
		return res;
	}

	public int getPrevZero(int i) {
		i += n;
		int k = 1;
		for (; i > 0; i >>= 1, k <<= 1)
			if ((i & 1) != 0 && t[i - 1] < k)
				break;
		if (i <= 1)
			return -1;
		for (--i; k > 1;) {
			k >>= 1;
			i <<= 1;
			if (t[i + 1] < k)
				++i;
		}
		return i - n;
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTreeFastAddSum t1 = new SegmentTreeFastAddSum(10);
		t1.set(0, 2);
		t1.set(1, 1);
		t1.set(2, 3);
		t1.set(3, 4);
		t1.add(1, 5);
		System.out.println(15 == t1.sum(0, 3));
		System.out.println(6 == t1.get(1));
		System.out.println(t1.sum(0, 3) == t1.sum(3));

		SegmentTreeFastAddSum t2 = new SegmentTreeFastAddSum(10);
		t2.set(2, 1);
		t2.set(3, 1);
		System.out.println(-1 == t2.getPrevZero(0));
		System.out.println(0 == t2.getPrevZero(1));
		System.out.println(1 == t2.getPrevZero(2));
		System.out.println(1 == t2.getPrevZero(3));
	}
}
