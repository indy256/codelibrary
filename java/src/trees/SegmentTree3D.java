package trees;

public class SegmentTree3D {
	int[][][] t;

	public int max(int[][][] t, int x1, int y1, int z1, int x2, int y2, int z2) {
		int n = t.length / 2;
		int m = t[0].length / 2;
		int k = t[0][0].length / 2;
		int res = Integer.MIN_VALUE;
		for (x1 += n, x2 += n; x1 <= x2; x1 = (x1 + 1) >> 1, x2 = (x2 - 1) >> 1) {
			for (int i1 = y1 + m, i2 = y2 + m; i1 <= i2; i1 = (i1 + 1) >> 1, i2 = (i2 - 1) >> 1) {
				for (int j1 = y1 + m, i2 = y2 + m; j1 <= i2; j1 = (j1 + 1) >> 1, i2 = (i2 - 1) >> 1) {
					res = Math.max(res, t[x1][j1]);
					res = Math.max(res, t[x1][i2]);
					res = Math.max(res, t[x2][j1]);
					res = Math.max(res, t[x2][i2]);
				}
			}
		}
		return res;
	}

	public void add(int x, int y, int z, int value) {
		int n = t.length / 2;
		int m = t[0].length / 2;
		int k = t[0][0].length / 2;
		t[x + n][y + m][z + k] += value;
		for (x += n; x > 0; x >>= 1) {
			if (x > 1)
				t[x >> 1][y + m][z + k] = Math.max(t[x][y + m][z + k], t[x ^ 1][y + m][z + k]);
			for (int i = y + m; i > 1; i >>= 1) {
				t[x][i >> 1] = Math.max(t[x][i], t[x][i ^ 1]);
			}
		}
	}

	// Usage example
	public static void main(String[] args) {
		int[][][] t = new int[20][40][60];
		add(t, 0, 0, 1);
		System.out.println(max(t, 0, 0, 1, 0));
	}
}
