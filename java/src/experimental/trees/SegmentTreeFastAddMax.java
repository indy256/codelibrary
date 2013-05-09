package experimental.trees;

public class SegmentTreeFastAddMax {

	public static int get(int[] t, int i) {
		return t[i + t.length / 2];
	}

	public static void set(int[] t, int i, int value) {
		add(t, i, value - t[i + t.length / 2]);
	}

	public static void add(int[] t, int i, int value) {
		i += t.length / 2;
		t[i] += value;
		for (; i > 1; i >>= 1)
			t[i >> 1] = Math.max(t[i], t[i ^ 1]);
	}

	// max[a, b]
	public static int max(int[] t, int a, int b) {
		int res = Integer.MIN_VALUE;
		for (a += t.length / 2, b += t.length / 2; a <= b; a = (a + 1) >> 1, b = (b - 1) >> 1) {
			res = Math.max(res, t[a]);
			res = Math.max(res, t[b]);
		}
		return res;
	}

	// Usage example
	public static void main(String[] args) {
		int n = 11;
		int[] t = new int[n + n];
		set(t, 1, 2);
		set(t, 2, 1);
		add(t, 1, 5);
		System.out.println(7 == max(t, 1, 10));
	}
}
