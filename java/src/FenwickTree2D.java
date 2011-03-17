public class FenwickTree2D {
	int[][] t;

	public FenwickTree2D(int R, int C) {
		t = new int[R][C];
	}

	// Adds value to the (r,c) element
	public void add(int r, int c, long value) {
		for (int i = r; i < t.length; i += (i + 1) & -(i + 1))
			for (int j = c; j < t[0].length; j += (j + 1) & -(j + 1))
				t[i][j] += value;
	}

	// Sum of elements in rectangle [(0, 0), (r, c)]
	public int sum(int r, int c) {
		int res = 0;
		for (int i = r; i >= 0; i -= (i + 1) & -(i + 1))
			for (int j = c; j >= 0; j -= (j + 1) & -(j + 1))
				res += t[i][j];
		return res;
	}

	// Sum of elements in rectangle [(r1, c1), (r2, c2)]
	public int sum(int r1, int c1, int r2, int c2) {
		return sum(r2, c2) - sum(r1 - 1, c2) - sum(r2, c1 - 1) + sum(r1 - 1, c1 - 1);
	}

	public int get(int r, int c) {
		return sum(r, c, r, c);
	}

	public void set(int r, int c, int value) {
		add(r, c, -get(r, c) + value);
	}
}
