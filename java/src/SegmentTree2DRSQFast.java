public class SegmentTree2DRSQFast {
	final int n = 1024;
	int[][] t = new int[n + n][n + n];

	public SegmentTree2DRSQFast() {
	}

	public SegmentTree2DRSQFast(int[][] a) {
		// System.arraycopy(a, 0, t, n, a.length);
		// for (int i = n - 1; i > 0; i--)
		// t[i] = t[i + i] + t[i + i + 1];
	}

	public int sum(int x1, int y1, int x2, int y2) {
		int res = 0;
		for (x1 += n, x2 += n; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
			for (int i1 = y1 + n, i2 = y2 + n; i1 <= i2; i1 = (i1 + 1) >> 1, i2 = (i2 - 1) >> 1) {
				if ((x1 & 1) != 0) {
					if ((i1 & 1) != 0)
						res += t[x1][i1];
					if ((i2 & 1) == 0)
						res += t[x1][i2];
				}
				if ((x2 & 1) == 0) {
					if ((i1 & 1) == 0)
						res += t[x2][i1];
					if ((i2 & 1) != 0)
						res += t[x2][i2];
				}
			}
		}
		return res;
	}

	public int get(int x, int y) {
		return t[x + n][y + n];
	}

	public void set(int i, int value) {
		// int x = value - get(i);
		// for (i += n; i > 0; i >>= 1)
		// t[i] += x;
	}

	public void add(int i, int value) {
		// for (i += n; i > 0; i >>= 1)
		// t[i] += value;
	}

	// Usage example
	public static void main(String[] args) {
	}
}
