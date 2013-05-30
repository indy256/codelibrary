package experimental.trees;
public class SegmentTree2DFastAddSum {
	int n;
	int m;
	int[][] t;

	public SegmentTree2DFastAddSum(int n, int m) {
		this.n = n;
		this.m = m;
		t = new int[2 * n][2 * m];
	}

	public int sum(int x1, int y1, int x2, int y2) {
		int res = 0;
		for (x1 += n, x2 += n; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
			for (int i1 = y1 + m, i2 = y2 + m; i1 <= i2; i1 = (i1 + 1) >> 1, i2 = (i2 - 1) >> 1) {
				if ((x1 & 1) != 0) {
					if ((i1 & 1) != 0)
						res += t[x1][i1];
					if ((i2 & 1) == 0)
						res += t[x1][i2];
				}
				if ((x2 & 1) == 0) {
					if ((i1 & 1) != 0)
						res += t[x2][i1];
					if ((i2 & 1) == 0)
						res += t[x2][i2];
				}
			}
		}
		return res;
	}

	public void add(int x, int y, int value) {
		for (x += n; x > 0; x >>= 1) {
			for (int i = y + m; i > 0; i >>= 1) {
				t[x][i] += value;
			}
		}
	}

	public int get(int x, int y) {
		return t[x + n][y + m];
	}

	public void set(int x, int y, int value) {
		add(x, y, -get(x, y) + value);
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTree2DFastAddSum t = new SegmentTree2DFastAddSum(3, 2);
		t.add(2, 1, 2);
		t.add(1, 1, 2);
		System.out.println(t.sum(0, 0, 2, 1));
	}
}
