import java.util.*;

public class SegmentTree2DRMQ {
	int[][] v;
	int[][] t;
	int n, m;

	public SegmentTree2DRMQ(int n, int m) {
		v = new int[n][m];
		for (int[] a : v)
			Arrays.fill(a, Integer.MAX_VALUE);
		init();
	}

	public SegmentTree2DRMQ(int[][] v) {
		this.v = v;
		init();
	}

	void init() {
		n = v.length;
		m = v[0].length;
		v = new int[n][m];
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
				t[node_x][node_y] = v[left_x][left_y];
			} else {
				t[node_x][node_y] = Math.min(t[node_x * 2][node_y], t[node_x * 2 + 1][node_y]);
			}
		} else {
			int my = (left_y + right_y) >> 1;
			build_y(node_x, left_x, right_x, node_y * 2, left_y, my);
			build_y(node_x, left_x, right_x, node_y * 2 + 1, my + 1, right_y);
			t[node_x][node_y] = Math.min(t[node_x][node_y * 2], t[node_x][node_y * 2 + 1]);
		}
	}

	public int min(int x1, int y1, int x2, int y2) {
		return min_x(1, 0, n - 1, x1, y1, x2, y2);
	}

	int min_x(int node_x, int left_x, int right_x, int x1, int y1, int x2, int y2) {
		if (left_x > x2 || right_x < x1)
			return Integer.MAX_VALUE;
		if (left_x >= x1 && right_x <= x2)
			return min_y(node_x, 1, 0, m - 1, y1, y2);
		int mx = (left_x + right_x) >> 1;
		return Math.min(min_x(node_x * 2, left_x, mx, x1, y1, x2, y2),
				min_x(node_x * 2 + 1, mx + 1, right_x, x1, y1, x2, y2));
	}

	int min_y(int node_x, int node_y, int left_y, int right_y, int y1, int y2) {
		if (left_y > y2 || right_y < y1)
			return Integer.MAX_VALUE;
		if (left_y >= y1 && right_y <= y2)
			return t[node_x][node_y];
		int my = (left_y + right_y) >> 1;
		return Math.min(min_y(node_x, node_y * 2, left_y, my, y1, y2),
				min_y(node_x, node_y * 2 + 1, my + 1, right_y, y1, y2));
	}

	public void set(int x, int y, int value) {
		set_x(1, 0, n - 1, x, y, value);
	}

	void set_x(int node_x, int left_x, int right_x, int x, int y, int value) {
		if (left_x != right_x) {
			int mx = (left_x + right_x) >> 1;
			if (x <= mx)
				set_x(node_x * 2, left_x, mx, x, y, value);
			else
				set_x(node_x * 2 + 1, mx + 1, right_x, x, y, value);
		}
		set_y(node_x, left_x, right_x, 1, 0, m - 1, x, y, value);
	}

	void set_y(int node_x, int left_x, int right_x, int node_y, int left_y, int right_y, int x, int y, int value) {
		if (left_y == right_y) {
			if (left_x == right_x)
				t[node_x][node_y] = value;
			else
				t[node_x][node_y] = t[node_x * 2][node_y] + t[node_x * 2 + 1][node_y];
		} else {
			int my = (left_y + right_y) >> 1;
			if (y <= my)
				set_y(node_x, left_x, right_x, node_y * 2, left_y, my, x, y, value);
			else
				set_y(node_x, left_x, right_x, node_y * 2 + 1, my + 1, right_y, x, y, value);
			t[node_x][node_y] = Math.min(t[node_x][node_y * 2], t[node_x][node_y * 2 + 1]);
		}
	}

	// Usage example
	public static void main(String[] args) {
		SegmentTree2DRMQ tree = new SegmentTree2DRMQ(2, 3);
		tree.set(2, 2, 1);
		System.out.println(tree.min(0, 0, 2, 3));
	}
}
