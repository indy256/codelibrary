package experimental.trees;
public class SegmentTree2DSetMax {
	int[][] t;
	int n, m;

	public SegmentTree2DSetMax(int n, int m) {
		this.n = n;
		this.m = m;
		t = new int[4 * n][4 * m];
		build_x(1, 0, n - 1);
	}

	void build_x(int node_x, int left_x, int right_x) {
		if (left_x != right_x) {
			int mid = (left_x + right_x) >> 1;
			build_x(node_x * 2, left_x, mid);
			build_x(node_x * 2 + 1, mid + 1, right_x);
		}
		build_y(node_x, left_x, right_x, 1, 0, m - 1);
	}

	void build_y(int node_x, int left_x, int right_x, int node_y, int left_y, int right_y) {
		if (left_y == right_y) {
			if (left_x == right_x) {
				t[node_x][node_y] = 0; // Integer.MIN_VALUE;
			} else {
				t[node_x][node_y] = Math.max(t[node_x * 2][node_y], t[node_x * 2 + 1][node_y]);
			}
		} else {
			int my = (left_y + right_y) >> 1;
			build_y(node_x, left_x, right_x, node_y * 2, left_y, my);
			build_y(node_x, left_x, right_x, node_y * 2 + 1, my + 1, right_y);
			t[node_x][node_y] = Math.max(t[node_x][node_y * 2], t[node_x][node_y * 2 + 1]);
		}
	}

	public int max(int x1, int y1, int x2, int y2) {
		return max_x(x1, y1, x2, y2, 1, 0, n - 1);
	}

	int max_x(int x1, int y1, int x2, int y2, int node_x, int left_x, int right_x) {
		if (left_x > x2 || right_x < x1)
			return Integer.MIN_VALUE;
		if (left_x >= x1 && right_x <= x2)
			return max_y(y1, y2, node_x, 1, 0, m - 1);
		int mx = (left_x + right_x) >> 1;
		return Math.max(max_x(x1, y1, x2, y2, node_x * 2, left_x, mx),
				max_x(x1, y1, x2, y2, node_x * 2 + 1, mx + 1, right_x));
	}

	int max_y(int y1, int y2, int node_x, int node_y, int left_y, int right_y) {
		if (left_y > y2 || right_y < y1)
			return Integer.MIN_VALUE;
		if (left_y >= y1 && right_y <= y2)
			return t[node_x][node_y];
		int my = (left_y + right_y) >> 1;
		return Math.max(max_y(y1, y2, node_x, node_y * 2, left_y, my),
				max_y(y1, y2, node_x, node_y * 2 + 1, my + 1, right_y));
	}

	public void set(int x, int y, int value) {
		set_x(x, y, value, 1, 0, n - 1);
	}

	void set_x(int x, int y, int value, int node_x, int left_x, int right_x) {
		if (left_x != right_x) {
			int mx = (left_x + right_x) >> 1;
			if (x <= mx)
				set_x(x, y, value, node_x * 2, left_x, mx);
			else
				set_x(x, y, value, node_x * 2 + 1, mx + 1, right_x);
		}
		set_y(x, y, value, node_x, left_x, right_x, 1, 0, m - 1);
	}

	void set_y(int x, int y, int value, int node_x, int left_x, int right_x, int node_y, int left_y, int right_y) {
		if (left_y == right_y) {
			if (left_x == right_x)
				t[node_x][node_y] = value;
			else
				t[node_x][node_y] = Math.max(t[node_x * 2][node_y], t[node_x * 2 + 1][node_y]);
		} else {
			int my = (left_y + right_y) >> 1;
			if (y <= my)
				set_y(x, y, value, node_x, left_x, right_x, node_y * 2, left_y, my);
			else
				set_y(x, y, value, node_x, left_x, right_x, node_y * 2 + 1, my + 1, right_y);
			t[node_x][node_y] = Math.max(t[node_x][node_y * 2], t[node_x][node_y * 2 + 1]);
		}
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTree2DSetMax t = new SegmentTree2DSetMax(3, 2);
		t.set(2, 1, 1);
		System.out.println(t.max(0, 0, 2, 1));
	}
}
