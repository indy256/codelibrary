public class FenwickTree {
	int[] t;
	int n;

	public void add(int i, int value) {
		for (; i < n; i += (i + 1) & -(i + 1))
			t[i] += value;
	}

	// sum[0,i]
	public int sum(int i) {
		int res = 0;
		for (; i >= 0; i -= (i + 1) & -(i + 1))
			res += t[i];
		return res;
	}

	public FenwickTree(int n) {
		this.n = n;
		t = new int[n];
	}

	// Usage example
	public static void main(String[] args) {
		FenwickTree t = new FenwickTree(10);
		t.add(1, 2);
		System.out.println(2 == t.sum(3));
	}
}
