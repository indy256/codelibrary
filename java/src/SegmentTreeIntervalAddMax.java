
public class SegmentTreeIntervalAddMax {
	int n;
	int[] tmax;
	int[] tadd;

	public SegmentTreeIntervalAddMax(int n) {
		this.n = n;
		tmax = new int[4 * n];
		tadd = new int[4 * n];
	}

	public void add(int a, int b, int value, int node, int left, int right) {
		if (a > right || b < left)
			return;
		if (a <= left && right <= b) {
			tadd[node] += value;
			return;
		}
		tadd[node * 2] += tadd[node];
		tadd[node * 2 + 1] += tadd[node];
		tadd[node] = 0;
		add(a, b, value, node * 2, left, (left + right) / 2);
		add(a, b, value, node * 2 + 1, (left + right) / 2 + 1, right);
		tmax[node] = Math.max(tmax[node * 2] + tadd[node * 2], tmax[node * 2 + 1] + tadd[node * 2 + 1]);
	}

	public int max(int a, int b, int node, int left, int right) {
		if (a > right || b < left)
			return Integer.MIN_VALUE;
		if (a <= left && right <= b)
			return tmax[node] + tadd[node];
		tadd[node * 2] += tadd[node];
		tadd[node * 2 + 1] += tadd[node];
		tadd[node] = 0;
		int res1 = max(a, b, node * 2, left, (left + right) / 2);
		int res2 = max(a, b, node * 2 + 1, (left + right) / 2 + 1, right);
		return Math.max(res1, res2);
	}

	// Usage example
	public static void main(String[] args) {
		int n = 10;
		SegmentTreeIntervalAddMax t = new SegmentTreeIntervalAddMax(n);
		t.add(1, 1, 5, 1, 0, n - 1);
		System.out.println(5 == t.max(0, n - 1, 1, 0, n - 1));
		t.add(3, 4, 4, 1, 0, n - 1);
		System.out.println(5 == t.max(0, n - 1, 1, 0, n - 1));
		t.add(1, 1, -2, 1, 0, n - 1);
		System.out.println(4 == t.max(0, n - 1, 1, 0, n - 1));
	}
}
